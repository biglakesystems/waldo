package com.biglakesystems.common.impl.web;

import com.biglakesystems.common.Assert;
import com.biglakesystems.common.spring.PropertySourceUtils;
import com.biglakesystems.common.web.ServletInitializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.context.support.StandardServletEnvironment;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.*;

/**
 * {@link ServletInitializationUtilsImpl} is the concrete implementation of the {@link ServletInitializationUtils}
 * interface.
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
public class ServletInitializationUtilsImpl implements ServletInitializationUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(ServletInitializationUtilsImpl.class);

    /**
     * Stateless singleton instance.
     */
    public static final ServletInitializationUtils INSTANCE = new ServletInitializationUtilsImpl();

    private final PropertiesLoader m_propertiesLoader;
    private final PropertySourceUtils m_sourceUtils;
    private final StaticHelper m_staticHelper;

    /**
     * Construct a {@link ServletInitializationUtilsImpl} instance.
     */
    ServletInitializationUtilsImpl()
    {
        this(PropertySourceUtils.INSTANCE, PropertiesLoader.INSTANCE, StaticHelper.INSTANCE);
    }

    /**
     * Construct a {@link ServletInitializationUtilsImpl} instance.
     *
     * @param sourceUtils the {@link PropertySourceUtils} utilities.
     * @param propertiesLoader the {@link PropertiesLoader} component.
     * @param staticHelper the {@link StaticHelper} component.
     */
    ServletInitializationUtilsImpl(final PropertySourceUtils sourceUtils, final PropertiesLoader propertiesLoader,
                                   final StaticHelper staticHelper)
    {
        super();
        Assert.argumentNotNull("propertiesLoader", m_propertiesLoader = propertiesLoader);
        Assert.argumentNotNull("sourceUtils", m_sourceUtils = sourceUtils);
        Assert.argumentNotNull("staticHelper", m_staticHelper = staticHelper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyResolver loadBootstrapConfig(final ServletContext context)
    {
        /* Build a default servlet environment and initialize it from the servlet context. Note that we don't have a
        ServletConfig at bootstrap time, as the servlet has not been created yet, but StandardServletEnvironment allows
        this. */
        final StandardServletEnvironment result = m_staticHelper.StandardServletEnvironment_new();
        result.initPropertySources(context, null);
        LOG.debug("Returning bootstrap configuration {} for servlet context {}.", result, context);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> loadMergedConfiguration(final ServletContext context,
                                                       final ResourcePatternResolver resolver,
                                                       final String locations)
    {
        final Map<String, Object> result;
        try
        {
            /* Create a pattern resolver and use it to locate all configuration resources. */
            final List<Resource> allResources = new ArrayList<>();
            for (final String nextLocation : locations.split("\\s*,\\s*"))
            {
                final String location = m_staticHelper.ServletContextPropertyUtils_resolvePlaceholders(nextLocation,
                        context, false);
                final Resource[] resources = resolver.getResources(location);
                if (null != resources && 0 != resources.length)
                {
                    allResources.addAll(Arrays.asList(resources));
                }
            }

            /* Load and merge the configuration. */
            final Properties properties = m_propertiesLoader.load(allResources);
            result = Collections.unmodifiableMap(new HashMap<String, Object>((Map) properties));
            if (LOG.isInfoEnabled())
            {
                /* Note: iterate over a throwaway TreeMap to sort by configuration key. */
                final StringBuilder builder = new StringBuilder();
                builder.append("Loaded the following configuration from resource(s) {}:");
                for (final Map.Entry<String, Object> nextEntry : new TreeMap<>(result).entrySet())
                {
                    builder.append("\n  [").append(nextEntry.getKey()).append("] => [").append(nextEntry.getValue())
                            .append("]");
                }
                LOG.info(builder.toString(), allResources);
            }
        }
        catch (final IOException e)
        {
            throw new RuntimeException(String.format(
                    "An error of type %s occurred while attempting to load the application configuration: %s",
                    e.getClass().getName(), e.getMessage()), e);
        }
        LOG.info("Loaded {} configuration item(s) from locations [{}]: {}.", result.size(), locations, result);
        return result;
    }
}
