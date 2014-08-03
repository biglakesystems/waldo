package com.biglakesystems.common.impl.aws;

import com.biglakesystems.common.Assert;
import com.biglakesystems.common.EntryNotFoundException;
import com.biglakesystems.common.security.Credentials;
import com.biglakesystems.common.security.CredentialsSource;
import com.biglakesystems.common.spring.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link S3ResourceLoader} is an implementation of the {@link ResourceLoader} interface which can load resources from
 * Amazon S3. See {@link ResourceUtils#createAmazonS3ResourceLoader(CredentialsSource)}.
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
public class S3ResourceLoader implements ResourceLoader
{
    private static final Logger LOG = LoggerFactory.getLogger(S3ResourceLoader.class);

    private final ClassLoader m_classLoader;
    private final CredentialsSource m_source;

    /**
     * Construct a {@link S3ResourceLoader} instance.
     *
     * @param source the source for Amazon IAM credentials.
     * @param classLoader the class loader to return from {@link ResourceLoader#getClassLoader()}.
     */
    public S3ResourceLoader(final CredentialsSource source, final ClassLoader classLoader)
    {
        super();
        Assert.argumentNotNull("classLoader", m_classLoader = classLoader);
        Assert.argumentNotNull("source", m_source = source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClassLoader getClassLoader()
    {
        return m_classLoader;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource getResource(final String location)
    {
        /* Parse the S3 bucket name from the resource path. */
        final Matcher matcher = s_location.matcher(location);
        if (!matcher.matches())
        {
            throw new IllegalArgumentException(
                    String.format("Location [%s] does not match the expected format.", location));
        }
        final String bucket = matcher.group(1);

        /* Load credentials from the application configuration. */
        final Credentials credentials;
        try
        {
            credentials = m_source.getCredentials(bucket);
            LOG.debug("Will use credentials {} to access resource [{}] in Amazon S3 bucket [{}].", credentials,
                    location, bucket);
        }
        catch (final EntryNotFoundException e)
        {
            throw new IllegalStateException(String.format(
                    "Credentials for Amazon S3 bucket [%s] not found in credentials source %s.", bucket, m_source));
        }
        final String key = matcher.group(2);
        final Resource result = new S3Resource(bucket, key, credentials);
        LOG.info("Returning Amazon S3 resource {} for location [{}].", result, location);
        return result;
    }

    /**
     * Pattern used to parse the bucket name from the path in an S3 resource path.
     */
    private static final Pattern s_location = Pattern.compile("^/([^/]+)/(.+)$");
}
