package com.biglakesystems.common.impl.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.biglakesystems.common.impl.StaticHelper;
import com.biglakesystems.common.security.Credentials;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;

import java.io.InputStream;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * {@link TestS3Resource} provides unit test coverage for {@link S3Resource}.
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
public class TestS3Resource
{
    private final BasicAWSCredentials m_mockAWSCredentials;
    private final Credentials m_mockCredentials;
    private final StaticHelper m_mockStaticHelper;
    private final AmazonS3Client m_mockS3Client;

    /**
     * Construct a {@link TestS3Resource} instance.
     */
    public TestS3Resource()
    {
        super();
        m_mockAWSCredentials = createMock(BasicAWSCredentials.class);
        m_mockCredentials = createMock(Credentials.class);
        m_mockStaticHelper = createMock(StaticHelper.class);
        m_mockS3Client = createMock(AmazonS3Client.class);
    }

    @Before
    public void resetCommonMocks()
    {
        reset(m_mockAWSCredentials, m_mockCredentials, m_mockStaticHelper, m_mockS3Client);
    }

    /**
     * Test the implementation of {@link Resource#exists()} when the resource does not exist.
     */
    @Test
    public void testExists_doesNotExist()
    {
        final AmazonS3Exception exception = new AmazonS3Exception("blah");
        exception.setErrorCode("NoSuchKey");

        /* Should create the AWS credentials and S3 client. */
        expect(m_mockStaticHelper.BasicAWSCredentials_new(eq("testAccessKey"), eq("testSecretKey")))
                .andReturn(m_mockAWSCredentials).once();
        expect(m_mockStaticHelper.AmazonS3Client_new(same(m_mockAWSCredentials))).andReturn(m_mockS3Client).once();

        /* Should get credentials. */
        expect(m_mockCredentials.getPrincipal()).andReturn("testAccessKey").atLeastOnce();
        expect(m_mockCredentials.getCredentials()).andReturn("testSecretKey").atLeastOnce();

        /* Should attempt to load the resource. */
        expect(m_mockS3Client.getObject(eq("testBucket"), eq("testResource.txt"))).andThrow(exception).once();

        /* Run the test and verify expectations. */
        replayCommonMocks();
        final Resource instance = new S3Resource("testBucket", "testResource.txt", m_mockCredentials,
                m_mockStaticHelper);
        assertFalse(instance.exists());
        verifyCommonMocks();
    }

    /**
     * Test the implementation of {@link Resource#exists()} when the resource does exist.
     */
    @Test
    public void testExists_exists()
    {
        final S3Object mockObject = createMock(S3Object.class);

        /* Should create the AWS credentials and S3 client. */
        expect(m_mockStaticHelper.BasicAWSCredentials_new(eq("testAccessKey"), eq("testSecretKey")))
                .andReturn(m_mockAWSCredentials).once();
        expect(m_mockStaticHelper.AmazonS3Client_new(same(m_mockAWSCredentials))).andReturn(m_mockS3Client).once();

        /* Should get credentials. */
        expect(m_mockCredentials.getPrincipal()).andReturn("testAccessKey").atLeastOnce();
        expect(m_mockCredentials.getCredentials()).andReturn("testSecretKey").atLeastOnce();

        /* Should attempt to load the resource. */
        expect(m_mockS3Client.getObject(eq("testBucket"), eq("testResource.txt"))).andReturn(mockObject).once();

        /* Run the test and verify expectations. */
        replayCommonMocks();
        replay(mockObject);
        final Resource instance = new S3Resource("testBucket", "testResource.txt", m_mockCredentials,
                m_mockStaticHelper);
        assertTrue(instance.exists());
        verifyCommonMocks();
        verify(mockObject);
    }

    /**
     * Test the implementation of {@link Resource#isReadable()}.
     */
    @Test
    public void testIsReadable()
    {
        final S3Object mockObject = createMock(S3Object.class);
        final S3ObjectInputStream mockStream = createMock(S3ObjectInputStream.class);

        /* Should create the AWS credentials and S3 client. */
        expect(m_mockStaticHelper.BasicAWSCredentials_new(eq("testAccessKey"), eq("testSecretKey")))
                .andReturn(m_mockAWSCredentials).once();
        expect(m_mockStaticHelper.AmazonS3Client_new(same(m_mockAWSCredentials))).andReturn(m_mockS3Client).once();

        /* Should get credentials. */
        expect(m_mockCredentials.getPrincipal()).andReturn("testAccessKey").atLeastOnce();
        expect(m_mockCredentials.getCredentials()).andReturn("testSecretKey").atLeastOnce();

        /* Should attempt to load the resource. */
        expect(m_mockS3Client.getObject(eq("testBucket"), eq("testResource.txt"))).andReturn(mockObject).once();

        /* Should attempt to open a stream on the resource, and should close that stream. */
        expect(mockObject.getObjectContent()).andReturn(mockStream).once();
        m_mockStaticHelper.IOUtils_closeQuietly(same(mockStream));
        expectLastCall().once();

        /* Run the test and verify expectations. */
        replayCommonMocks();
        replay(mockObject, mockStream);
        final Resource instance = new S3Resource("testBucket", "testResource.txt", m_mockCredentials,
                m_mockStaticHelper);
        assertTrue(instance.isReadable());
        verifyCommonMocks();
        verify(mockObject, mockStream);
    }

    /**
     * Test the implementation of {@link Resource#getInputStream()}.
     *
     * @throws Throwable on unexpected error.
     */
    @Test
    public void testGetInputStream() throws Throwable
    {
        final S3Object mockObject = createMock(S3Object.class);
        final S3ObjectInputStream mockStream = createMock(S3ObjectInputStream.class);

        /* Should create the AWS credentials and S3 client. */
        expect(m_mockStaticHelper.BasicAWSCredentials_new(eq("testAccessKey"), eq("testSecretKey")))
                .andReturn(m_mockAWSCredentials).once();
        expect(m_mockStaticHelper.AmazonS3Client_new(same(m_mockAWSCredentials))).andReturn(m_mockS3Client).once();

        /* Should get credentials. */
        expect(m_mockCredentials.getPrincipal()).andReturn("testAccessKey").atLeastOnce();
        expect(m_mockCredentials.getCredentials()).andReturn("testSecretKey").atLeastOnce();

        /* Should attempt to load the resource. */
        expect(m_mockS3Client.getObject(eq("testBucket"), eq("testResource.txt"))).andReturn(mockObject).once();

        /* Should attempt to open a stream on the resource, and should close that stream. */
        expect(mockObject.getObjectContent()).andReturn(mockStream).once();

        /* Run the test and verify expectations. */
        replayCommonMocks();
        replay(mockObject, mockStream);
        final Resource instance = new S3Resource("testBucket", "testResource.txt", m_mockCredentials,
                m_mockStaticHelper);
        assertSame(mockStream, instance.getInputStream());
        verifyCommonMocks();
        verify(mockObject, mockStream);
    }

    private void replayCommonMocks()
    {
        replay(m_mockAWSCredentials, m_mockCredentials, m_mockStaticHelper, m_mockS3Client);
    }

    private void verifyCommonMocks()
    {
        verify(m_mockAWSCredentials, m_mockCredentials, m_mockStaticHelper, m_mockS3Client);
    }
}
