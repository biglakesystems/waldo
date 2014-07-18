package waldo.config.app;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import waldo.Constants;
import waldo.entity.EntityObject;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * {@link InternalPersistenceUnitJpaConfig} ...
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
@Profile(Constants.Profiles.JPA_INTERNAL_PERSISTENCE_UNIT)
@SuppressWarnings("unused")
class InternalPersistenceUnitJpaConfig
{
    private static final Logger LOG = LoggerFactory.getLogger(InternalPersistenceUnitJpaConfig.class);

    /**
     * Construct a {@link InternalPersistenceUnitJpaConfig} instance.
     */
    InternalPersistenceUnitJpaConfig()
    {
        super();
    }

    /**
     * Create Spring factory bean which will produce the internally managed JPA {@link EntityManagerFactory}.
     *
     * @param dataSource the application data source.
     * @return {@link FactoryBean} instance.
     */
    @Bean
    FactoryBean<EntityManagerFactory> entityManagerFactory(final DataSource dataSource,
                                                           final ConfigurableEnvironment environment)
    {
        /* Get a properties object containing all items from the merged application configuration. This allows
        Hibernate to be configured via the same configuration files. This is kind of hacky because the Environment does
        not give direct access to the property list. */
        final MapPropertySource source =
                (MapPropertySource) environment.getPropertySources().get(Constants.Context.CONFIGURATION);
        final Properties properties = new Properties();
        for (final String nextProperty : source.getPropertyNames())
        {
            properties.setProperty(nextProperty, environment.getProperty(nextProperty));
        }

        /* Create and return the factory bean. */
        final LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
        result.setDataSource(dataSource);
        result.setPackagesToScan(ClassUtils.getPackageName(EntityObject.class));
        result.setPersistenceUnitName("waldo");
        result.setJpaDialect(new HibernateJpaDialect());
        result.setJpaProperties(properties);
        result.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        LOG.info("Returning entity manager factory {}.", result);
        return result;
    }

    /**
     * Create the transaction-aware shared entity manager proxy.
     *
     * @param entityManagerFactory the {@link EntityManagerFactory} component.
     * @return {@link EntityManager} instance.
     */
    @Bean
    EntityManager transactionalEntityManager(final EntityManagerFactory entityManagerFactory)
    {
        final EntityManager result = SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactory);
        LOG.info("Returning transactional entity manager {}.", result);
        return result;
    }
}
