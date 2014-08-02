package com.biglakesystems.common.impl.spring;

import com.biglakesystems.common.Assert;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link DispatchingResourceLoader} is an implementation of the {@link ResourceLoader} interface which dispatches to
 * zero or more constructor-configured resource loaders based upon a resource location prefix. If no resource loader
 * matches the prefix for a requested resource, or if no prefix is present, then a default resource loader is used.
 * <p/>
 * Prefixed resource loaders are configured via constructor, via a map with the prefix as its key. These prefixes
 * should <em>not</em> include a trailing colon, which separates the prefix from the rest of the resource location.
 * <p/>
 * When {@link ResourceLoader#getResource(String)} is invoked on a prefixed resource loader, the prefix itself is
 * <em>not</em> included. If the default resource loader is used because no matching prefixed resource loader is found,
 * the prefix <em>is</em> included.
 * <p/>
 * <strong>Thread Safety:</strong> instances of this class contain no mutable state and are therefore safe for
 * multithreaded access, provided the same is true of all dependencies provided via constructor.
 * <p/>
 * Copyright 2014 Big Lake Systems, LLC.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
class DispatchingResourceLoader implements ResourceLoader
{
    private static final Logger LOG = LoggerFactory.getLogger(DispatchingResourceLoader.class);

    private final ClassLoader m_classLoader;
    private final ResourceLoader m_defaultLoader;
    private final Map<String, ResourceLoader> m_loadersByPrefix;

    /**
     * Construct a {@link DispatchingResourceLoader} instance.
     *
     * @param classLoader the class loader to be returned by {@link ResourceLoader#getClassLoader()}.
     * @param defaultLoader the default resource loader for unmatched or absent prefixes.
     * @param loadersByPrefix the map of resource loaders by prefix.
     */
    DispatchingResourceLoader(final ClassLoader classLoader, final ResourceLoader defaultLoader,
                              final Map<String, ResourceLoader> loadersByPrefix)
    {
        super();
        Assert.argumentNotNull("classLoader", m_classLoader = classLoader);
        Assert.argumentNotNull("defaultLoader", m_defaultLoader = defaultLoader);
        Assert.argumentNotNull("loadersByPrefix", loadersByPrefix);
        m_loadersByPrefix = Collections.unmodifiableMap(new HashMap<>(loadersByPrefix));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("classLoader", m_classLoader).append("defaultLoader", m_defaultLoader)
                .append("loadersByPrefix", m_loadersByPrefix).toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource getResource(final String location)
    {
        /* First, look for a matching prefixed loader (if the location contains a prefix.) */
        Resource result = null;
        boolean foundPrefixedLoader = false;
        if (!m_loadersByPrefix.isEmpty())
        {
            final int prefixStart = location.indexOf(':');
            if (-1 == prefixStart)
            {
                LOG.debug("No prefixed resource loader was found because location [{}] does not include a prefix.",
                        location);
            }
            else
            {
                final String prefix = location.substring(0, prefixStart);
                if (!m_loadersByPrefix.containsKey(prefix))
                {
                    LOG.debug("No prefixed resource loader was found for prefix [{}] from location [{}].", prefix,
                            location);
                }
                else
                {
                    final ResourceLoader loader = m_loadersByPrefix.get(prefix);
                    result = loader.getResource(location);
                    foundPrefixedLoader = true;
                    LOG.debug("Found prefixed resource loader {} for prefix [{}] from location [{}].", loader, prefix,
                            location);
                }
            }
        }

        /* If no prefixed loader was found, use the default loader. */
        if (!foundPrefixedLoader)
        {
            result = m_defaultLoader.getResource(location);
            LOG.debug("Used default resource loader {} for location [{}].", m_defaultLoader, location);
        }
        LOG.info("Returning resource {} for location [{}].", result, location);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClassLoader getClassLoader()
    {
        return m_classLoader;
    }

    /**
     * For testing purposes only.
     *
     * @return {@link ResourceLoader} instance.
     */
    ResourceLoader getDefaultLoader()
    {
        return m_defaultLoader;
    }

    /**
     * For testing purposes only.
     *
     * @return {@link Map} instance.
     */
    Map<String, ResourceLoader> getLoadersByPrefix()
    {
        return m_loadersByPrefix;
    }
}
