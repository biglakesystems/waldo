package com.biglakesystems.common.impl.web;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.Resource;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * {@link TestPropertiesLoaderImpl} provides unit test coverage for {@link PropertiesLoaderImpl}.
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
public class TestPropertiesLoaderImpl
{
    private final StaticHelper m_mockStaticHelper;

    /**
     * Construct a {@link TestPropertiesLoaderImpl} instance.
     */
    public TestPropertiesLoaderImpl()
    {
        super();
        m_mockStaticHelper = createMock(StaticHelper.class);
    }

    @Before
    public void resetCommonMocks()
    {
        reset(m_mockStaticHelper);
    }

    /**
     * Test the implementation of {@link PropertiesLoader#load(List)}.
     *
     * @throws Throwable on unexpected error.
     */
    @Test
    public void testLoad() throws Throwable
    {
        final PropertiesFactoryBean mockFactory = createMock(PropertiesFactoryBean.class);
        final Resource mockResource1 = createMock(Resource.class);
        final Resource mockResource2 = createMock(Resource.class);
        final Resource mockResource3 = createMock(Resource.class);
        final List<Resource> resources = Arrays.asList(mockResource1, mockResource2, mockResource3);
        final Properties properties = new Properties();

        /* Should create a properties factory bean and populate it with the properties resources. */
        expect(m_mockStaticHelper.PropertiesFactoryBean_new()).andReturn(mockFactory).once();
        mockFactory.setLocations(aryEq(new Resource[] { mockResource1, mockResource2, mockResource3 }));
        expectLastCall().once();

        /* Should create the properties object. Note that PropertiesFactoryBean.afterPropertiesSet() is final, so we
        can't set an expectation for it. At the time of this writing, the test works anyway. */
        expect(mockFactory.getObject()).andReturn(properties).atLeastOnce();

        /* Run the test and verify expectations. */
        replayCommonMocks();
        replay(mockFactory, mockResource1, mockResource2, mockResource3);
        final PropertiesLoader instance = new PropertiesLoaderImpl(m_mockStaticHelper);
        assertSame(properties, instance.load(resources));
        verifyCommonMocks();
        verify(mockFactory, mockResource1, mockResource2, mockResource3);
    }

    private void replayCommonMocks()
    {
        replay(m_mockStaticHelper);
    }

    private void verifyCommonMocks()
    {
        verify(m_mockStaticHelper);
    }
}
