package com.biglakesystems.common.impl.spring;

import com.biglakesystems.common.Assert;
import com.biglakesystems.common.spring.PropertySourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.*;

/**
 * {@link PropertySourceUtilsImpl} ...
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
public class PropertySourceUtilsImpl implements PropertySourceUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(PropertySourceUtilsImpl.class);

    /**
     * Stateless singleton instance.
     */
    public static final PropertySourceUtils INSTANCE = new PropertySourceUtilsImpl();

    private final StaticHelper m_staticHelper;

    /**
     * Construct a {@link PropertySourceUtilsImpl} instance.
     */
    PropertySourceUtilsImpl()
    {
        this(StaticHelper.INSTANCE);
    }

    /**
     * Construct a {@link PropertySourceUtilsImpl} instance.
     *
     * @param staticHelper the {@link StaticHelper} component
     */
    PropertySourceUtilsImpl(final StaticHelper staticHelper)
    {
        super();
        Assert.argumentNotNull("staticHelper", m_staticHelper = staticHelper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getEnumerablePropertyNames(final PropertySources sources)
    {
        Assert.argumentNotNull("sources", sources);
        final Set<String> result = new HashSet<String>();
        for (final PropertySource nextSource : sources)
        {
            if (!(nextSource instanceof EnumerablePropertySource))
            {
                LOG.debug("Skipping property source {} because it is not of type {}.", nextSource,
                        EnumerablePropertySource.class.getName());
                continue;
            }
            final String[] names = ((EnumerablePropertySource<?>) nextSource).getPropertyNames();
            if (names.length > 0)
            {
                final List<String> add = Arrays.asList(names);
                result.addAll(add);
                LOG.debug("Added {} property name(s) from property source {}: {}", add.size(), nextSource, add);
            }
        }
        LOG.info("Returning {} property name(s) from property sources {}: {}", result.size(), sources, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Properties loadProperties(final List<Resource> resources)
    {
        final Properties result;
        final PropertiesFactoryBean factory = m_staticHelper.PropertiesFactoryBean_new();
        factory.setLocations(resources.toArray(new Resource[resources.size()]));
        try
        {
            factory.afterPropertiesSet();
            result = factory.getObject();
        }
        catch (final IOException e)
        {
            throw new RuntimeException(String.format(
                    "An error of type %s occurred while attempting to load properties from resource(s) %s: %s",
                    e.getClass().getName(), resources, e.getMessage()), e);
        }
        LOG.debug("Returning {} properties loaded from resource(s) {}.", result.size(), resources);
        return result;
    }
}
