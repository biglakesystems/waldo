package com.biglakesystems.common.spring;

import com.biglakesystems.common.impl.spring.ResourceUtilsImpl;
import com.biglakesystems.common.security.CredentialsSource;
import org.springframework.core.io.ResourceLoader;

/**
 * {@link ResourceUtils} defines the public interface to an object which provides utility functionality related to
 * Spring resources and resource loaders, including custom resource loader implementations.
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
public interface ResourceUtils
{
    /**
     * Runtime implementation.
     */
    ResourceUtils INSTANCE = ResourceUtilsImpl.INSTANCE;

    /**
     * Get a {@link DispatchingResourceLoaderBuilder} instance through which a dispatching resource loader can be
     * configured and created. Use a provided default loader for any resource location which does not include a prefix
     * matching any of the configured loaders.
     *
     * @param classLoader the class loader to be returned by {@link ResourceLoader#getClassLoader()}.
     * @param defaultLoader the default resource loader.
     * @return {@link DispatchingResourceLoaderBuilder} instance.
     */
    DispatchingResourceLoaderBuilder buildDispatchingResourceLoader(ClassLoader classLoader,
                                                                    ResourceLoader defaultLoader);

    /**
     * Get a resource loader which is capable of loading resources from Amazon S3. Credentials for accessing resources
     * will be obtained from a given {@link CredentialsSource}. Credentials are obtained by passing the S3 bucket name
     * to the credentials source.
     * <p/>
     * The resource loader location string format is as follows::
     * <p/>
     * <code>/<em>bucket-name</em>/path/to/file.txt</code>
     * <p/>
     * Where <em>bucket-name</em> is the name of the Amazon S3 bucket.
     *
     * @param classLoader the class loader to be returned by {@link ResourceLoader#getClassLoader()}.
     * @param source the credentials source.
     * @return {@link ResourceLoader} instance.
     */
    ResourceLoader createAmazonS3ResourceLoader(ClassLoader classLoader, CredentialsSource source);
}
