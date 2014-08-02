package com.biglakesystems.common.impl.spring;

import com.biglakesystems.common.Assert;
import com.biglakesystems.common.spring.PropertySourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;

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

    /**
     * Construct a {@link PropertySourceUtilsImpl} instance.
     */
    PropertySourceUtilsImpl()
    {
        super();
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
}
