package com.biglakesystems.common.web;

import com.biglakesystems.common.impl.web.ServletInitializationUtilsImpl;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;

import javax.servlet.ServletContext;
import java.util.Map;

/**
 * {@link ServletInitializationUtils} defines the public interface to an object which provides utility functionality
 * related to programmatic initialization of servlets.
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
public interface ServletInitializationUtils
{
    /**
     * Runtime implementation.
     */
    ServletInitializationUtils INSTANCE = ServletInitializationUtilsImpl.INSTANCE;

    /**
     * Get a {@link PropertyResolver} wrapping the <em>bootstrap</em> configuration for a given servlet context. This
     * configuration will contain items such as system properties and servlet context init parameters, but will
     * <em>not</em> contain servlet config parameters, as those items are not available until after the servlet has
     * been initialized.
     *
     * @param context the servlet context.
     * @return {@link PropertyResolver} instance.
     */
    PropertyResolver loadBootstrapConfig(ServletContext context);

    /**
     * Load the configuration properties file(s) from an array of locations. Same-named properties in locations listed
     * <em>later</em> in the location array will override properties in locations listed <em>earlier</em> in the
     * location array.
     * <p/>
     * A {@link ServletContextResourcePatternResolver} instance is used to resolve each individual location; locations
     * can be in any format supported by that resolver.
     *
     * @param context the servlet context.
     * @param resolver the resource resolver.
     * @param locations the properties file locations, as a comma-delimited list.
     * @return {@link Map} of {@link String} property names to {@link Object} property values.
     */
    Map<String, Object> loadMergedConfiguration(ServletContext context, ResourcePatternResolver resolver,
                                                String locations);
}
