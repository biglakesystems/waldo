package com.biglakesystems.common.impl.spring;

import com.biglakesystems.common.spring.ResourceUtils;
import org.junit.Test;
import org.springframework.core.io.ResourceLoader;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * {@link TestResourceUtilsImpl} provides unit test coverage for {@link ResourceUtilsImpl}.
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
public class TestResourceUtilsImpl
{
    /**
     * Construct a {@link TestResourceUtilsImpl} instance.
     */
    public TestResourceUtilsImpl()
    {
        super();
    }

    /**
     * Test the implementation of {@link ResourceUtils#buildDispatchingResourceLoader(ClassLoader, ResourceLoader)}.
     */
    @Test
    public void testBuildDispatchingResourceLoader()
    {
        final ClassLoader mockClassLoader = createMock(ClassLoader.class);
        final ResourceLoader mockDefaultLoader = createMock(ResourceLoader.class);

        /* Run the test and verify expectations. */
        replay(mockClassLoader);
        final ResourceUtils instance = new ResourceUtilsImpl();
        final DispatchingResourceLoaderBuilderImpl builder =
                (DispatchingResourceLoaderBuilderImpl) instance.buildDispatchingResourceLoader(mockClassLoader,
                        mockDefaultLoader);
        assertSame(mockClassLoader, builder.getClassLoader());
        assertSame(mockDefaultLoader, builder.getDefaultLoader());
        verify(mockClassLoader);
    }
}
