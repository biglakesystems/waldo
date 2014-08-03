package com.biglakesystems.common.impl.spring;

import com.biglakesystems.common.Assert;
import com.biglakesystems.common.impl.aws.S3ResourceLoader;
import com.biglakesystems.common.security.CredentialsSource;
import com.biglakesystems.common.spring.DispatchingResourceLoaderBuilder;
import com.biglakesystems.common.spring.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;

/**
 * {@link ResourceUtilsImpl} is the concrete implementation of the {@link ResourceUtils} interface.
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
public class ResourceUtilsImpl implements ResourceUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(ResourceUtilsImpl.class);

    /**
     * Stateless singleton instance.
     */
    public static final ResourceUtils INSTANCE = new ResourceUtilsImpl();

    /**
     * Construct a {@link ResourceUtilsImpl} instance.
     */
    ResourceUtilsImpl()
    {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DispatchingResourceLoaderBuilder buildDispatchingResourceLoader(final ClassLoader classLoader,
                                                                           final ResourceLoader defaultLoader)
    {
        Assert.argumentNotNull("classLoader", classLoader);
        Assert.argumentNotNull("defaultLoader", defaultLoader);
        final DispatchingResourceLoaderBuilder result =
                new DispatchingResourceLoaderBuilderImpl(classLoader, defaultLoader);
        LOG.debug("Returning resource loader builder {}.", result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceLoader createAmazonS3ResourceLoader(final ClassLoader classLoader, final CredentialsSource source)
    {
        Assert.argumentNotNull("classLoader", classLoader);
        Assert.argumentNotNull("source", source);
        final ResourceLoader result = new S3ResourceLoader(source, classLoader);
        LOG.debug("Returning Amazon S3 resource loader {} with credentials source {}.", result, source);
        return result;
    }
}
