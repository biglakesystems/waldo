package com.biglakesystems.common.impl.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.biglakesystems.common.Assert;
import com.biglakesystems.common.impl.StaticHelper;
import com.biglakesystems.common.security.Credentials;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@link S3Resource} is an implementation of the {@link Resource} interface which can load a resource from an Amazon
 * S3 bucket. The bucket, file key (path within the S3 bucket without leading slash) and credentials are configured via
 * constructor.
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
class S3Resource extends AbstractResource
{
    private final String m_bucket;
    private final Credentials m_credentials;
    private final String m_key;
    private final StaticHelper m_staticHelper;

    /**
     * Construct an {@link S3Resource} instance.
     *
     * @param bucket the S3 bucket.
     * @param key the key within the S3 bucket.
     * @param credentials the credentials to use when accessing the bucket.
     */
    S3Resource(final String bucket, final String key, final Credentials credentials)
    {
        this(bucket, key, credentials, StaticHelper.INSTANCE);
    }

    /**
     * Construct an {@link S3Resource} instance.
     *
     * @param bucket the S3 bucket.
     * @param key the key within the S3 bucket.
     * @param credentials the credentials to use when accessing the bucket.
     * @param staticHelper the {@link StaticHelper} component.
     */
    S3Resource(final String bucket, final String key, final Credentials credentials, final StaticHelper staticHelper)
    {
        super();
        Assert.argumentNotNull("bucket", m_bucket = bucket);
        Assert.argumentNotNull("key", m_key = key);
        Assert.argumentNotNull("credentials", m_credentials = credentials);
        Assert.argumentNotNull("staticHelper", m_staticHelper = staticHelper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription()
    {
        return String.format("Amazon S3 Resource %s:%s", m_bucket, m_key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists()
    {
        return null != openObject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getInputStream() throws IOException
    {
        final S3Object object = openObject();
        if (null == object)
        {
            throw new IOException(
                    String.format("Object [%s] does not exist in Amazon S3 bucket [%s].", m_key, m_bucket));
        }
        return object.getObjectContent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isReadable()
    {
        final boolean result;
        final S3Object object = openObject();
        if (null == object)
        {
            result = false;
        }
        else
        {
            final InputStream stream = object.getObjectContent();
            try
            {
                result = true;
            }
            finally
            {
                m_staticHelper.IOUtils_closeQuietly(stream);
            }
        }
        return result;
    }

    /**
     * Open the S3 object if it exists.
     *
     * @return {@link S3Object} instance or {@code null} if the object does not exist.
     * @throws AmazonS3Exception if an error occurs while accessing the resource.
     */
    private S3Object openObject() throws AmazonS3Exception
    {
        final AWSCredentials credentials = m_staticHelper.BasicAWSCredentials_new(
                m_credentials.getPrincipal().toString(), m_credentials.getCredentials().toString());
        final AmazonS3 client = m_staticHelper.AmazonS3Client_new(credentials);
        S3Object result = null;
        try
        {
            result = client.getObject(m_bucket, m_key);
        }
        catch (final AmazonS3Exception e)
        {
            if (!"NoSuchKey".equals(e.getErrorCode()))
            {
                throw e;
            }
        }
        return result;
    }

    /**
     * For testing purposes only.
     *
     * @return {@link String} instance.
     */
    String getBucket()
    {
        return m_bucket;
    }

    /**
     * For testing purposes only.
     *
     * @return {@link Credentials} instance.
     */
    Credentials getCredentials()
    {
        return m_credentials;
    }

    /**
     * For testing purposes only.
     *
     * @return {@link String} instance.
     */
    String getKey()
    {
        return m_key;
    }
}
