package waldo.impl.web.integration.spring.context;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.ServletContextPropertyUtils;
import waldo.Constants;
import waldo.config.app.AppConfig;
import waldo.config.web.WebConfig;

import javax.servlet.*;
import java.io.IOException;
import java.util.*;

/**
 * {@link WaldoInitializer} ...
 * <p/>
 * <strong>Thread Safety:</strong> instances of this class contain no mutable state and are therefore safe for
 * multithreaded access, provided the same is true of all dependencies provided via constructor.
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
public class WaldoInitializer implements WebApplicationInitializer
{
    private static final Logger LOG = LoggerFactory.getLogger(WaldoInitializer.class);

    /**
     * Construct a {@link WaldoInitializer} instance.
     */
    public WaldoInitializer()
    {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStartup(final ServletContext context) throws ServletException
    {
        /* Load and merge the application configuration. */
        final Map<String, Object> configuration;
        final String locations =
                StringUtils.trimToNull(context.getInitParameter(Constants.Context.CONFIGURATION_LOCATIONS));
        if (null == locations)
        {
            configuration = Collections.emptyMap();
            LOG.warn("Using empty configuration because context-param [configurationPropertiesLocations] was not set.");
        }
        else
        {
            /* Load the configuration. */
            final String[] patterns = locations.split("\\s*,\\s*");
            configuration = loadConfiguration(context, patterns);
            LOG.info("Loaded {} configuration key(s) from locations {}.", configuration.size(), patterns);
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

    /**
     * Load the configuration properties file(s) from an array of locations. Same-named properties in locations listed
     * <em>later</em> in the location array will override properties in locations listed <em>earlier</em> in the
     * location array.
     * <p/>
     * A {@link ServletContextResourcePatternResolver} instance is used to resolve each individual location; locations
     * can be in any format supported by that resolver.
     *
     * @param context the servlet context.
     * @param locations the properties file locations.
     * @return {@link Map} of {@link String} property names to {@link String} property values.
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> loadConfiguration(final ServletContext context, final String[] locations)
    {
        final Map<String, Object> result;
        try
        {
            /* Create a pattern resolver and use it to locate all configuration resources. */
            final ResourcePatternResolver resolver = new ServletContextResourcePatternResolver(context);
            final List<Resource> allResources = new ArrayList<>();
            for (final String nextLocation : locations)
            {
                final String location = ServletContextPropertyUtils.resolvePlaceholders(nextLocation, context, false);
                final Resource[] resources = resolver.getResources(location);
                if (null != resources && 0 != resources.length)
                {
                    allResources.addAll(Arrays.asList(resources));
                }
            }

            /* Load and merge the configuration. */
            final PropertiesFactoryBean factory = new PropertiesFactoryBean();
            factory.setLocations(allResources.toArray(new Resource[allResources.size()]));
            factory.afterPropertiesSet();
            final Properties properties = factory.getObject();
            result = Collections.unmodifiableMap(new HashMap<String, Object>((Map) properties));
            if (LOG.isInfoEnabled())
            {
                /* Note: iterate over a throwaway TreeMap to sort by configuration key. */
                final StringBuilder builder = new StringBuilder();
                builder.append("Loaded the following configuration from resource(s) {}:");
                for (final Map.Entry<String, Object> nextEntry : new TreeMap<>(result).entrySet())
                {
                    builder.append("\n  [").append(nextEntry.getKey()).append("] => [").append(nextEntry.getValue())
                            .append("]");
                }
                LOG.info(builder.toString(), allResources);
            }
        }
        catch (final IOException e)
        {
            throw new RuntimeException(String.format(
                    "An error of type %s occurred while attempting to load the application configuration: %s",
                    e.getClass().getName(), e.getMessage()), e);
        }
        return result;
    }
}
