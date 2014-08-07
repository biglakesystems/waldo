package com.biglakesystems.common.impl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.config.PropertiesFactoryBean;

import java.io.Closeable;

/**
 * {@link StaticHelper} defines the public interface to an object which provides wrapper methods for static methods
 * and constructors in third party libraries, for the purpose of simplifying unit testing.
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
public interface StaticHelper
{
    /**
     * Runtime implementation.
     */
    StaticHelper INSTANCE = new StaticHelper()
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public AmazonS3Client AmazonS3Client_new(final AWSCredentials credentials)
        {
            return new AmazonS3Client(credentials);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BasicAWSCredentials BasicAWSCredentials_new(final String accessKey, final String secretKey)
        {
            return new BasicAWSCredentials(accessKey, secretKey);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void IOUtils_closeQuietly(final Closeable closeable)
        {
            IOUtils.closeQuietly(closeable);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public PropertiesFactoryBean PropertiesFactoryBean_new()
        {
            return new PropertiesFactoryBean();
        }
    };

    /**
     * @see AmazonS3Client#AmazonS3Client(AWSCredentials)
     */
    AmazonS3Client AmazonS3Client_new(AWSCredentials credentials);

    /**
     * @see BasicAWSCredentials#BasicAWSCredentials(String, String)
     */
    BasicAWSCredentials BasicAWSCredentials_new(String accessKey, String secretKey);

    /**
     * @see IOUtils#closeQuietly(Closeable)
     */
    void IOUtils_closeQuietly(Closeable closeable);

    /**
     * @see PropertiesFactoryBean#PropertiesFactoryBean()
     */
    PropertiesFactoryBean PropertiesFactoryBean_new();
}
