package waldo.config.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

import java.util.List;

/**
 * {@link MvcConfig} handles portions of web application configuration which are specific to Spring MVC.
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
@Configuration
@ComponentScan(basePackages = "waldo.impl.web.controller")
@EnableWebMvc
@SuppressWarnings("unused")
class MvcConfig extends WebMvcConfigurationSupport
{
    private static final Logger LOG = LoggerFactory.getLogger(MvcConfig.class);

    /**
     * Construct an {@link MvcConfig} instance.
     */
    MvcConfig()
    {
        super();
    }

    /**
     * Get the application view resolver.
     *
     * @return {@link ViewResolver} instance.
     */
    @Bean
    ViewResolver viewResolver()
    {
        final ViewResolver result = new TilesViewResolver();
        LOG.info("Returning Tiles view resolver {}.", result);
        return result;
    }

    /**
     * Get the Tiles configurer.
     *
     * @return {@link TilesConfigurer} instance.
     */
    @Bean
    TilesConfigurer tilesConfigurer()
    {
        final TilesConfigurer result = new TilesConfigurer();
        LOG.info("Returning Tiles configurer {}.", result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer)
    {
        configurer.enable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configureMessageConverters(final List<HttpMessageConverter<?>> converters)
    {
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}
