package com.biglakesystems.common.impl.web;

import com.biglakesystems.common.spring.PropertySourceUtils;
import com.biglakesystems.common.web.ServletInitializationUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.context.support.StandardServletEnvironment;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import java.util.*;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * {@link TestServletInitializationUtilsImpl} provides unit test coverage for {@link ServletInitializationUtilsImpl}.
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
public class TestServletInitializationUtilsImpl
{
    private final PropertiesLoader m_mockPropertiesLoader;
    private final PropertySourceUtils m_mockSourceUtils;
    private final StaticHelper m_mockStaticHelper;

    /**
     * Construct a {@link TestServletInitializationUtilsImpl} instance.
     */
    public TestServletInitializationUtilsImpl()
    {
        super();
        m_mockPropertiesLoader = createMock(PropertiesLoader.class);
        m_mockSourceUtils = createMock(PropertySourceUtils.class);
        m_mockStaticHelper = createMock(StaticHelper.class);
    }

    @Before
    public void resetCommonMocks()
    {
        reset(m_mockPropertiesLoader, m_mockSourceUtils, m_mockStaticHelper);
    }

    /**
     * Test the implementation of {@link ServletInitializationUtils#loadBootstrapConfig(ServletContext)}.
     */
    @Test
    public void testLoadBootstrapConfig()
    {
        final ServletContext mockContext = createMock(ServletContext.class);
        final StandardServletEnvironment mockEnvironment = createMock(StandardServletEnvironment.class);
        final MutablePropertySources mockSources = createMock(MutablePropertySources.class);

        /* Should create and initialize a StandardServletEnvironment. */
        expect(m_mockStaticHelper.StandardServletEnvironment_new()).andReturn(mockEnvironment).once();
        mockEnvironment.initPropertySources(same(mockContext), isNull(ServletConfig.class));
        expectLastCall().once();

        /* Run the test and verify expectations. */
        replayCommonMocks();
        replay(mockContext, mockEnvironment, mockSources);
        final ServletInitializationUtils instance =
                new ServletInitializationUtilsImpl(m_mockSourceUtils, m_mockPropertiesLoader, m_mockStaticHelper);
        assertSame(mockEnvironment, instance.loadBootstrapConfig(mockContext));
        verifyCommonMocks();
        verify(mockContext, mockEnvironment, mockSources);
    }

    /**
     * Test the implementation of
     * {@link ServletInitializationUtils#loadMergedConfiguration(ServletContext, ResourcePatternResolver, String)}.
     *
     * @throws Throwable on unexpected error.
     */
    @Test
    public void testLoadConfigLocations() throws Throwable
    {
        final ServletContext mockContext = createMock(ServletContext.class);
        final ResourcePatternResolver mockResolver = createMock(ResourcePatternResolver.class);
        final Resource mockResource1_1 = createMock(Resource.class);
        final Resource mockResource2_1 = createMock(Resource.class);
        final Resource mockResource2_2 = createMock(Resource.class);
        final Resource mockResource3_1 = createMock(Resource.class);
        final Properties properties = new Properties();
        properties.setProperty("property1", "value1");

        /* Should resolve placeholders in each location. */
        expect(m_mockStaticHelper.ServletContextPropertyUtils_resolvePlaceholders(eq("location1"), same(mockContext),
                eq(false))).andReturn("resource1").atLeastOnce();
        expect(m_mockStaticHelper.ServletContextPropertyUtils_resolvePlaceholders(eq("location2"), same(mockContext),
                eq(false))).andReturn("resource2").atLeastOnce();
        expect(m_mockStaticHelper.ServletContextPropertyUtils_resolvePlaceholders(eq("location3"), same(mockContext),
                eq(false))).andReturn("resource3").atLeastOnce();

        /* Should resolve each resource. */
        expect(mockResolver.getResources(eq("resource1")))
                .andReturn(new Resource[] { mockResource1_1 }).atLeastOnce();
        expect(mockResolver.getResources(eq("resource2")))
                .andReturn(new Resource[]{ mockResource2_1, mockResource2_2 }).atLeastOnce();
        expect(mockResolver.getResources(eq("resource3")))
                .andReturn(new Resource[]{ mockResource3_1 }).atLeastOnce();

        /* Should load properties from the resources. */
        expect(m_mockPropertiesLoader.load(eq(
                Arrays.asList(mockResource1_1, mockResource2_1, mockResource2_2, mockResource3_1))))
                .andReturn(properties)
                .atLeastOnce();

        /* Run the test and verify expectations. */
        replayCommonMocks();
        replay(mockContext, mockResolver, mockResource1_1, mockResource2_1, mockResource2_2, mockResource3_1);
        final ServletInitializationUtils instance = new ServletInitializationUtilsImpl(m_mockSourceUtils,
                m_mockPropertiesLoader, m_mockStaticHelper);
        final Map<String, Object> configuration = instance.loadMergedConfiguration(mockContext, mockResolver,
                "location1, location2 , \n location3");
        assertEquals(1, configuration.size());
        assertEquals("value1", configuration.get("property1"));
        verifyCommonMocks();
        verify(mockContext, mockResolver, mockResource1_1, mockResource2_1, mockResource2_2, mockResource3_1);
    }

    private void replayCommonMocks()
    {
        replay(m_mockPropertiesLoader, m_mockSourceUtils, m_mockStaticHelper);
    }

    private void verifyCommonMocks()
    {
        verify(m_mockPropertiesLoader, m_mockSourceUtils, m_mockStaticHelper);
    }
}
