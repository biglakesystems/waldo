package com.biglakesystems.common.impl.spring;

import com.biglakesystems.common.Assert;
import com.biglakesystems.common.spring.DispatchingResourceLoaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link DispatchingResourceLoaderBuilderImpl} is the concrete implementation of the
 * {@link DispatchingResourceLoaderBuilder} interface.
 * <p/>
 * <strong>Thread Safety:</strong> instances of this class are not thread-safe. All operations must be performed on a
 * single thread or appropriate publication and synchronization constructs must be applied externally.
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
class DispatchingResourceLoaderBuilderImpl implements DispatchingResourceLoaderBuilder
{
    private static final Logger LOG = LoggerFactory.getLogger(DispatchingResourceLoaderBuilderImpl.class);

    private final ClassLoader m_classLoader;
    private final ResourceLoader m_defaultLoader;
    private final Map<String, ResourceLoader> m_loadersByPrefix = new HashMap<>();

    /**
     * Construct a {@link DispatchingResourceLoaderBuilderImpl} instance.
     *
     * @param classLoader the class loader to be returned by {@link ResourceLoader#getClassLoader()}.
     * @param defaultLoader the default resource loader.
     */
    DispatchingResourceLoaderBuilderImpl(final ClassLoader classLoader, final ResourceLoader defaultLoader)
    {
        super();
        Assert.argumentNotNull("classLoader", m_classLoader = classLoader);
        Assert.argumentNotNull("defaultLoader", m_defaultLoader = defaultLoader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DispatchingResourceLoaderBuilder addPrefixedLoader(final String prefix, final ResourceLoader loader)
    {
        Assert.argumentNotNull("prefix", prefix);
        Assert.argumentNotNull("loader", loader);
        if (m_loadersByPrefix.containsKey(prefix))
        {
            throw new IllegalStateException(String.format(
                    "Cannot add resource loader %s for prefix [%s] because another resource loader is associated with that prefix.",
                    loader, prefix));
        }
        m_loadersByPrefix.put(prefix, loader);
        LOG.debug("Added resource loader {} for prefix [{}].", loader, prefix);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceLoader toResourceLoader()
    {
        final ResourceLoader result = new DispatchingResourceLoader(m_classLoader, m_defaultLoader, m_loadersByPrefix);
        LOG.info("Returning dispatching resource loader {}.", result);
        return result;
    }

    /**
     * For testing purposes only.
     *
     * @return {@link ClassLoader} instance.
     */
    ClassLoader getClassLoader()
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
}
