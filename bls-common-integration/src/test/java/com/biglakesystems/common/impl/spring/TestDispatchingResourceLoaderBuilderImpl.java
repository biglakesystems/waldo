package com.biglakesystems.common.impl.spring;

import com.biglakesystems.common.spring.DispatchingResourceLoaderBuilder;
import org.junit.Test;
import org.springframework.core.io.ResourceLoader;

import java.util.Map;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * {@link TestDispatchingResourceLoaderBuilderImpl} provides unit test coverage for
 * {@link DispatchingResourceLoaderBuilderImpl}.
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
public class TestDispatchingResourceLoaderBuilderImpl
{
    /**
     * Construct a {@link TestDispatchingResourceLoaderBuilderImpl} instance.
     */
    public TestDispatchingResourceLoaderBuilderImpl()
    {
        super();
    }

    /**
     * Test the implementation of {@link DispatchingResourceLoaderBuilder#addPrefixedLoader(String, ResourceLoader)}.
     */
    @Test
    public void testAddPrefixedLoader()
    {
        final ClassLoader mockClassLoader = createMock(ClassLoader.class);
        final ResourceLoader mockDefaultLoader = createMock(ResourceLoader.class);
        final ResourceLoader mockPrefixedLoader1 = createMock(ResourceLoader.class);
        final ResourceLoader mockPrefixedLoader2 = createMock(ResourceLoader.class);

        /* Run the test and verify expectations. */
        replay(mockClassLoader, mockDefaultLoader, mockPrefixedLoader1, mockPrefixedLoader2);
        final DispatchingResourceLoaderBuilder instance =
                new DispatchingResourceLoaderBuilderImpl(mockClassLoader, mockDefaultLoader);
        assertSame(instance, instance.addPrefixedLoader("test1", mockPrefixedLoader1));
        assertSame(instance, instance.addPrefixedLoader("test2", mockPrefixedLoader2));
        final DispatchingResourceLoader loader = (DispatchingResourceLoader) instance.toResourceLoader();
        assertSame(mockClassLoader, loader.getClassLoader());
        assertSame(mockDefaultLoader, loader.getDefaultLoader());
        final Map<String, ResourceLoader> loadersByPrefix = loader.getLoadersByPrefix();
        assertEquals(2, loadersByPrefix.size());
        assertSame(mockPrefixedLoader1, loadersByPrefix.get("test1"));
        assertSame(mockPrefixedLoader2, loadersByPrefix.get("test2"));
        verify(mockClassLoader, mockDefaultLoader, mockPrefixedLoader1, mockPrefixedLoader2);
    }
}
