package com.biglakesystems.common.security;

/**
 * {@link Credentials} defines the public interface to an object which contains a set of named credentials, typically a
 * user name and password pair, for use in accessing some protected resource.
 * <p>
 * Copyright 2014 Big Lake Systems, LLC.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
public interface Credentials
{
    /**
     * Get the name of the credentials. This is usually not significant with regard to the credentials themselves or
     * the resource they protect, but is simply an identifier which is used to locate the credentials within a data
     * source which may hold many sets of credentials.
     * <p/>
     * For example, if the credentials are related to an Amazon IAM principal, then this method may return the name of
     * the Amazon S3 bucket with which the credentials are associated in configuration.
     *
     * @return {@link String} name of the credentials.
     */
    String getName();

    /**
     * Get the principal.
     *
     * @return {@link Object} principal.
     */
    Object getPrincipal();

    /**
     * Get the credentials.
     *
     * @return {@link Object} credentials.
     */
    Object getCredentials();
}
