package com.biglakesystems.common.impl.spring;

import com.biglakesystems.common.impl.StaticHelper;
import com.biglakesystems.common.spring.PropertySourceUtils;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.io.Resource;

import java.util.*;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.*;

/**
 * {@link TestPropertySourceUtilsImpl} provides unit test coverage for {@link PropertySourceUtilsImpl}.
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
public class TestPropertySourceUtilsImpl
{
    private final StaticHelper m_mockStaticHelper;

    /**
     * Construct a {@link TestPropertySourceUtilsImpl} instance.
     */
    public TestPropertySourceUtilsImpl()
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
     * Test the implementation of {@link PropertySourceUtils#getEnumerablePropertyNames(PropertySources)}.
     */
    @Test
    public void testGetEnumerablePropertyNames()
    {
        final PropertySources mockSources = createMock(PropertySources.class);
        final PropertySource<?> mockSource1 = createMock(PropertySource.class);
        final EnumerablePropertySource<?> mockSource2 = createMock(EnumerablePropertySource.class);
        final EnumerablePropertySource<?> mockSource3 = createMock(EnumerablePropertySource.class);
        final PropertySource<?> mockSource4 = createMock(PropertySource.class);
        final List<PropertySource<?>> sources = Arrays.asList(mockSource1, mockSource2, mockSource3, mockSource4);

        /* Should enumerate property sources. */
        expect(mockSources.iterator()).andAnswer(new IAnswer<Iterator<PropertySource<?>>>()
        {
            /** {@inheritDoc} */
            @Override
            public Iterator<PropertySource<?>> answer() throws Throwable
            {
                return sources.iterator();
            }
        });

        /* Should get property names from each enumerable property source. */
        expect(mockSource2.getPropertyNames()).andReturn(new String[] { "source2property1", "source2property2" })
                .atLeastOnce();
        expect(mockSource3.getPropertyNames()).andReturn(new String[] { "source3property1", "source3property2" })
                .atLeastOnce();

        /* Run the test and verify expectations. */
        replay(mockSources, mockSource1, mockSource2, mockSource3, mockSource4);
        final PropertySourceUtils instance = new PropertySourceUtilsImpl(m_mockStaticHelper);
        final Set<String> names = instance.getEnumerablePropertyNames(mockSources);
        assertEquals(4, names.size());
        assertTrue(names.containsAll(Arrays.asList("source2property1", "source2property2", "source3property1",
                "source3property2")));
        verify(mockSources, mockSource1, mockSource2, mockSource3, mockSource4);
    }


    /**
     * Test the implementation of {@link PropertySourceUtils#loadProperties(List)}.
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
        final PropertySourceUtils instance = new PropertySourceUtilsImpl(m_mockStaticHelper);
        assertSame(properties, instance.loadProperties(resources));
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
