package com.biglakesystems.common.impl.web;

import org.springframework.web.context.support.StandardServletEnvironment;
import org.springframework.web.util.ServletContextPropertyUtils;

import javax.servlet.ServletContext;

/**
 * {@link StaticHelper} defines the public interface to an object which provides wrapper methods for static methods
 * and constructors in third party libraries, for the purpose of simplifying unit testing.
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
interface StaticHelper
{
    /**
     * Runtime implementation.
     */
    StaticHelper INSTANCE = new StaticHelper()
    {
        /** {@inheritDoc} */
        @Override
        public String ServletContextPropertyUtils_resolvePlaceholders(final String value, final ServletContext context,
                                                                      final boolean required)
        {
            return ServletContextPropertyUtils.resolvePlaceholders(value, context, required);
        }

        /** {@inheritDoc} */
        @Override
        public StandardServletEnvironment StandardServletEnvironment_new()
        {
            return new StandardServletEnvironment();
        }
    };

    /**
     * @see ServletContextPropertyUtils#resolvePlaceholders(String, ServletContext, boolean)
     */
    String ServletContextPropertyUtils_resolvePlaceholders(String value, ServletContext context, boolean required);

    /**
     * @see StandardServletEnvironment#StandardServletEnvironment()
     */
    StandardServletEnvironment StandardServletEnvironment_new();
}
