package com.biglakesystems.common.impl.web;

import com.biglakesystems.common.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * {@link PropertiesLoaderImpl} is the concrete implementation of the {@link PropertiesLoader} interface.
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
class PropertiesLoaderImpl implements PropertiesLoader
{
    private static final Logger LOG = LoggerFactory.getLogger(PropertiesLoaderImpl.class);

    /**
     * Stateless singleton instance.
     */
    static final PropertiesLoader INSTANCE = new PropertiesLoaderImpl();

    private final StaticHelper m_staticHelper;

    /**
     * Construct a {@link PropertiesLoaderImpl} instance.
     */
    PropertiesLoaderImpl()
    {
        this(StaticHelper.INSTANCE);
    }

    /**
     * Construct a {@link PropertiesLoaderImpl} instance.
     *
     * @param staticHelper the {@link StaticHelper} component.
     */
    PropertiesLoaderImpl(final StaticHelper staticHelper)
    {
        super();
        Assert.argumentNotNull("staticHelper", m_staticHelper = staticHelper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Properties load(final List<Resource> resources)
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
