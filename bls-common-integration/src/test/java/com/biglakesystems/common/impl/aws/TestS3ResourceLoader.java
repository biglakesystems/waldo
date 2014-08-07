package com.biglakesystems.common.impl.aws;

import com.biglakesystems.common.EntryNotFoundException;
import com.biglakesystems.common.security.Credentials;
import com.biglakesystems.common.security.CredentialsSource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ResourceLoader;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * {@link TestS3ResourceLoader} provides unit test coverage for {@link S3ResourceLoader}.
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
public class TestS3ResourceLoader
{
    private final ClassLoader m_mockClassLoader;
    private final CredentialsSource m_mockCredentialsSource;

    /**
     * Construct a {@link TestS3ResourceLoader} instance.
     */
    public TestS3ResourceLoader()
    {
        super();
        m_mockClassLoader = createMock(ClassLoader.class);
        m_mockCredentialsSource = createMock(CredentialsSource.class);
    }

    @Before
    public void resetCommonMocks()
    {
        reset(m_mockClassLoader, m_mockCredentialsSource);
    }

    /**
     * Test the implementation of {@link ResourceLoader#getClassLoader()}.
     */
    @Test
    public void testGetClassLoader()
    {
        replayCommonMocks();
        final ResourceLoader instance = new S3ResourceLoader(m_mockCredentialsSource, m_mockClassLoader);
        assertSame(m_mockClassLoader, instance.getClassLoader());
        verifyCommonMocks();
    }

    /**
     * Test the implementation of {@link ResourceLoader#getResource(String)}.
     */
    @Test
    public void testGetResource()
    {
        final Credentials mockCredentials = createMock(Credentials.class);

        /* Should get credentials for the bucket. */
        expect(m_mockCredentialsSource.getCredentials(eq("testBucket"))).andReturn(mockCredentials).atLeastOnce();

        /* Run the test and verify expectations. */
        replayCommonMocks();
        replay(mockCredentials);
        final ResourceLoader instance = new S3ResourceLoader(m_mockCredentialsSource, m_mockClassLoader);
        final S3Resource resource = (S3Resource) instance.getResource("/testBucket/testFolder/testResource.txt");
        assertNotNull(resource);
        assertEquals("testBucket", resource.getBucket());
        assertEquals("testFolder/testResource.txt", resource.getKey());
        assertSame(mockCredentials, resource.getCredentials());
        System.out.println(resource.getDescription());
        verifyCommonMocks();
        verify(mockCredentials);
    }

    /**
     * Test the implementation of {@link ResourceLoader#getResource(String)} when the format is not recognized.
     */
    @Test
    public void testGetResource_badFormat()
    {
        /* Run the test and verify expectations. */
        replayCommonMocks();
        final ResourceLoader instance = new S3ResourceLoader(m_mockCredentialsSource, m_mockClassLoader);
        try
        {
            instance.getResource("doesNotStartWithSlash/something.txt");
            fail("Invocation with invalid location did not throw.");
        }
        catch (final IllegalArgumentException e)
        {
            /* Good. */
            assertTrue(e.getMessage().contains("doesNotStartWithSlash/something.txt"));
        }
        verifyCommonMocks();
    }

    /**
     * Test the implementation of {@link ResourceLoader#getResource(String)} when the credentials are not found.
     */
    @Test
    public void testGetResource_noCredentials()
    {
        final EntryNotFoundException exception = new EntryNotFoundException("testBucket", "CredentialsNotFound");

        /* Should attempt to get credentials for the bucket. */
        expect(m_mockCredentialsSource.getCredentials(eq("testBucket"))).andThrow(exception).once();

        /* Run the test and verify expectations. */
        replayCommonMocks();
        final ResourceLoader instance = new S3ResourceLoader(m_mockCredentialsSource, m_mockClassLoader);
        try
        {
            instance.getResource("/testBucket/testFolder/testResource.txt");
            fail("Invocation with no matching credentials did not throw.");
        }
        catch (final IllegalStateException e)
        {
            /* Good. */
            assertTrue(e.getMessage().contains("testBucket"));
            assertSame(exception, e.getCause());
        }
        verifyCommonMocks();
    }

    private void replayCommonMocks()
    {
        replay(m_mockClassLoader, m_mockCredentialsSource);
    }

    private void verifyCommonMocks()
    {
        verify(m_mockClassLoader, m_mockCredentialsSource);
    }
}
