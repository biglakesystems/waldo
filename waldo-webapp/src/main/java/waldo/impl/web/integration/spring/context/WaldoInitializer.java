package waldo.impl.web.integration.spring.context;

import com.biglakesystems.common.Assert;
import com.biglakesystems.common.spring.ResourceUtils;
import com.biglakesystems.common.web.ServletInitializationUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.ServletContextResourceLoader;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;
import org.springframework.web.servlet.DispatcherServlet;
import waldo.Constants;
import waldo.config.app.AppConfig;
import waldo.config.web.WebConfig;

import javax.servlet.*;
import java.util.*;

/**
 * {@link WaldoInitializer} ...
 * <p>
 * <strong>Thread Safety:</strong> instances of this class contain no mutable state and are therefore safe for
 * multithreaded access, provided the same is true of all dependencies provided via constructor.
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
public class WaldoInitializer implements WebApplicationInitializer
{
    private static final Logger LOG = LoggerFactory.getLogger(WaldoInitializer.class);

    private final ServletInitializationUtils m_initializationUtils;
    private final ResourceUtils m_resourceUtils;

    /**
     * Construct a {@link WaldoInitializer} instance.
     */
    public WaldoInitializer()
    {
        this(ServletInitializationUtils.INSTANCE, ResourceUtils.INSTANCE);
    }

    /**
     * Construct a {@link WaldoInitializer} instance.
     *
     * @param initializationUtils the {@link ServletInitializationUtils} component.
     * @param resourceUtils the {@link ResourceUtils} component.
     */
    WaldoInitializer(final ServletInitializationUtils initializationUtils, final ResourceUtils resourceUtils)
    {
        super();
        Assert.argumentNotNull("initializationUtils", m_initializationUtils = initializationUtils);
        Assert.argumentNotNull("resourceUtils", m_resourceUtils = resourceUtils);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStartup(final ServletContext context) throws ServletException
    {
        /* Load the bootstrap configuration. This will include items from the system parameters and servlet context,
        but will not be the full, final configuration. This allows us to pull in additional configuration from sources
        which require configuration themselves, such as an Amazon S3 bucket. Once those sources are configured, we can
        load the final configuration. */
        final PropertyResolver resolver = m_initializationUtils.loadBootstrapConfig(context);

        /* Build a resource loader which is capable of loading from Amazon S3, for which Spring has no built-in
        support. This will allow portions of the application to be pulled from S3 in a private manner, using credentials
        from the bootstrap configuration. The loader will fall back to a standard ServletContextResourceLoader, so
        configuration can also be loaded through any of the mechanisms which Spring supports internally. */
        final ClassLoader classLoader = context.getClassLoader();
        final ResourceLoader defaultLoader = new ServletContextResourceLoader(context);
        final ResourceLoader s3Loader = m_resourceUtils.createAmazonS3ResourceLoader(classLoader,
                new ConfiguredCredentialsSource(resolver));
        final ResourceLoader configurationLoader =
                m_resourceUtils.buildDispatchingResourceLoader(classLoader, defaultLoader)
                        .addPrefixedLoader("s3", s3Loader)
                        .toResourceLoader();
        LOG.debug("Will load application configuration via resource loader {}.", configurationLoader);

        /* Load the final application configuration. */
        final String locations =
                StringUtils.trimToNull(context.getInitParameter(Constants.Context.CONFIGURATION_LOCATIONS));
        if (null == locations)
        {
            throw new IllegalStateException(
                    String.format("Configuration location(s) not found under context-param [%s].",
                            Constants.Context.CONFIGURATION_LOCATIONS));
        }
        final ResourcePatternResolver configurationResolver =
                new ServletContextResourcePatternResolver(configurationLoader);
        final Map<String, Object> configuration =
                m_initializationUtils.loadMergedConfiguration(context, configurationResolver, locations);
        LOG.info("Loaded {} configuration item(s) from location(s) [{}].", configuration.size(), locations);
        if (LOG.isDebugEnabled())
        {
            final StringBuilder builder = new StringBuilder();
            for (final String nextKey : new TreeSet<>(configuration.keySet()))
            {
                builder.append("\n  [").append(nextKey).append("] => [").append(configuration.get(nextKey)).append("]");
            }
            LOG.debug("Merged configuration is:{}", builder.toString());
        }

        /* Initialize the root application context. */
        final ConfigurableWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.setConfigLocation(ClassUtils.getPackageName(AppConfig.class));
        initApplicationContext(appContext, configuration);

        /* Initialize the web application context. */
        final ConfigurableWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
        webContext.setConfigLocation(ClassUtils.getPackageName(WebConfig.class));
        initApplicationContext(webContext, configuration);

        /* Add servlet context listeners. */
        context.addListener(new ContextLoaderListener(appContext));
        context.addListener(new RequestContextListener());

        /* Add the dispatcher servlet. */
        final ServletRegistration.Dynamic servlet = context.addServlet("waldo", new DispatcherServlet(webContext));
        servlet.addMapping("/");
        servlet.setLoadOnStartup(1);
        servlet.setAsyncSupported(true);

        /* Apply security filter to the dispatcher servlet. */
        //        final FilterRegistration.Dynamic security = context.addFilter(BeanIds.SPRING_SECURITY_FILTER_CHAIN,
        //                DelegatingFilterProxy.class);
        //        security.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
    }

    /**
     * Initialize an application context by applying a merged configuration map to its environment properties, and
     * adding an appropriate {@link PropertySourcesPlaceholderConfigurer} to it.
     *
     * @param context the application context.
     * @param configuration the merged configuration.
     */
    private void initApplicationContext(final ConfigurableWebApplicationContext context,
                                        final Map<String, Object> configuration)
    {
        /* Add a property source backed by the configuration map. */
        final ConfigurableEnvironment environment = context.getEnvironment();
        final MutablePropertySources sources = environment.getPropertySources();
        sources.addFirst(new MapPropertySource(Constants.Context.CONFIGURATION, configuration));
        final PropertySourcesPlaceholderConfigurer placeholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        placeholderConfigurer.setPropertySources(sources);
        context.addBeanFactoryPostProcessor(placeholderConfigurer);

        /* Scan for configuration properties prefixed with "waldo.profile."; for each found, activate the Spring
        profile named in the associated value. */
        for (final Map.Entry<String, Object> nextConfiguration : configuration.entrySet())
        {
            final String key = nextConfiguration.getKey();
            if (key.startsWith(Constants.Profiles.PROFILE_CONFIGURATION_PREFIX))
            {
                final String value = ObjectUtils.toString(nextConfiguration.getValue());
                environment.addActiveProfile(value);
                LOG.info("Added active profile [{}] for configuration [{}].", value, key);
            }
        }
    }
}
