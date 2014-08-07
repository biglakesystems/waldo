package com.biglakesystems.common.impl.spring;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * {@link TestDispatchingResourceLoader} provides unit test coverage for {@link DispatchingResourceLoader}.
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
public class TestDispatchingResourceLoader
{
    private final ClassLoader m_mockClassLoader;
    private final ResourceLoader m_mockDefaultLoader;

    /**
     * Construct a {@link TestDispatchingResourceLoader} instance.
     */
    public TestDispatchingResourceLoader()
    {
        super();
        m_mockClassLoader = createMock(ClassLoader.class);
        m_mockDefaultLoader = createMock(ResourceLoader.class);
    }

    @Before
    public void resetCommonMocks()
    {
        reset(m_mockClassLoader, m_mockDefaultLoader);
    }

    /**
     * Test the implementation of {@link ResourceLoader#getClassLoader()}.
     */
    @Test
    public void testGetClassLoader()
    {
        replayCommonMocks();
        final ResourceLoader instance = new DispatchingResourceLoader(m_mockClassLoader, m_mockDefaultLoader,
                Collections.<String, ResourceLoader> emptyMap());
        assertSame(m_mockClassLoader, instance.getClassLoader());
        verifyCommonMocks();
    }

    /**
     * Test the implementation of {@link ResourceLoader#getResource(String)} when it should delegate to the default
     * resource loader.
     */
    @Test
    public void testGetResource_defaultLoader()
    {
        final Resource mockResource = createMock(Resource.class);

        /* Should delegate to the default loader. */
        expect(m_mockDefaultLoader.getResource(eq("testLocation"))).andReturn(mockResource).once();

        /* Run the test and verify expectations. */
        replayCommonMocks();
        replay(mockResource);
        final ResourceLoader instance = new DispatchingResourceLoader(m_mockClassLoader, m_mockDefaultLoader,
                Collections.<String, ResourceLoader> emptyMap());
        assertSame(mockResource, instance.getResource("testLocation"));
        verifyCommonMocks();
        verify(mockResource);
    }

    /**
     * Test the implementation of {@link ResourceLoader#getResource(String)} when it should use a prefixed resource
     * loader from the map supplied to its constructor.
     */
    @Test
    public void testGetResource_prefixedLoader()
    {
        final ResourceLoader mockLoader1 = createMock(ResourceLoader.class);
        final ResourceLoader mockLoader2 = createMock(ResourceLoader.class);
        final Resource mockLoader1Resource = createMock(Resource.class);
        final Resource mockLoader2Resource = createMock(Resource.class);
        final Resource mockDefaultLoaderResource = createMock(Resource.class);
        final Map<String, ResourceLoader> loaders = new HashMap<>(2);
        loaders.put("prefix1", mockLoader1);
        loaders.put("prefix2", mockLoader2);

        /* Should load resources based on prefix. */
        expect(mockLoader1.getResource(eq("/loader1Location"))).andReturn(mockLoader1Resource).once();
        expect(mockLoader2.getResource(eq("/loader2Location"))).andReturn(mockLoader2Resource).once();
        expect(m_mockDefaultLoader.getResource("prefix3:/defaultLoaderLocation")).andReturn(mockDefaultLoaderResource)
                .once();

        /* Run the test and verify expectations. */
        replayCommonMocks();
        replay(mockLoader1, mockLoader2, mockLoader1Resource, mockLoader2Resource, mockDefaultLoaderResource);
        final ResourceLoader instance = new DispatchingResourceLoader(m_mockClassLoader, m_mockDefaultLoader, loaders);
        assertSame(mockLoader1Resource, instance.getResource("prefix1:/loader1Location"));
        assertSame(mockLoader2Resource, instance.getResource("prefix2:/loader2Location"));
        assertSame(mockDefaultLoaderResource, instance.getResource("prefix3:/defaultLoaderLocation"));
        verifyCommonMocks();
        verify(mockLoader1, mockLoader2, mockLoader1Resource, mockLoader2Resource, mockDefaultLoaderResource);
    }

    private void replayCommonMocks()
    {
        replay(m_mockClassLoader, m_mockDefaultLoader);
    }

    private void verifyCommonMocks()
    {
        verify(m_mockClassLoader, m_mockDefaultLoader);
    }
}
