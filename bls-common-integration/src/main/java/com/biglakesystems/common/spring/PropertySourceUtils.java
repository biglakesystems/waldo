package com.biglakesystems.common.spring;

import com.biglakesystems.common.impl.spring.PropertySourceUtilsImpl;
import org.springframework.core.env.PropertySources;

import java.util.Set;

/**
 * {@link PropertySourceUtils} defines the public interface to an object which provides utility functionality related
 * to Spring property sources.
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
public interface PropertySourceUtils
{
    /**
     * Runtime implementation.
     */
    PropertySourceUtils INSTANCE = PropertySourceUtilsImpl.INSTANCE;

    /**
     * Get the names of all <em>enumerable</em> properties in a set of property sources. This will include static items
     * such as system properties, but will not include dynamic items such as JNDI entries.
     *
     * @param sources the property sources.
     * @return {@link Set} of {@link String} property names.
     */
    Set<String> getEnumerablePropertyNames(PropertySources sources);
}
