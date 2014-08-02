package waldo.config.app;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import waldo.Constants;

import javax.sql.DataSource;
import java.sql.Driver;

/**
 * {@link InternalPoolDatabaseConfig} handles portions of the database configuration which apply only to environments
 * where the database is accessed by an internally managed connection pool. This configuration is enabled when the
 * {@link Constants.Profiles#DATABASE_INTERNAL_POOL} profile is active.
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
@Configuration
@org.springframework.context.annotation.Profile(Constants.Profiles.DATABASE_INTERNAL_POOL)
@SuppressWarnings("unused")
class InternalPoolDatabaseConfig
{
    private static final Logger LOG = LoggerFactory.getLogger(InternalPoolDatabaseConfig.class);

    /**
     * Construct an {@link InternalPoolDatabaseConfig} instance.
     */
    InternalPoolDatabaseConfig()
    {
        super();
    }

    /**
     * Create the internal pooled {@link DataSource}.
     *
     * @return {@link DataSource} instance.
     */
    @Bean(destroyMethod = "close")
    DataSource dataSource(final InternalDataSourceWrapper wrapper) throws Throwable
    {
        /* Create and return the data source. */
        final DataSource result = wrapper.getDataSource();
        LOG.info("Returning internally managed data source {}.", result);
        return result;
    }

    /**
     * Create and return the internally managed application data source, wrapping it in an
     * {@link InternalDataSourceWrapper} so that the driver will be deregistered, if necessary, at shutdown time.
     *
     * @param environment the application environment.
     * @return {@link InternalDataSourceWrapper} instance.
     * @throws Throwable if an error occurs while creating the data source.
     */
    @Bean
    InternalDataSourceWrapper dataSourceWrapper(final Environment environment) throws Throwable
    {
        /* Get the driver class and determine whether it was loaded from the webapp class loader. Note that the "=="
        comparison is intentional: we want to check for the SAME class loader, regardless of equals() implementation. */
        final Class<Driver> driverClass = environment.getPropertyAsClass("waldo.database.internal_pool.driver_class",
                Driver.class);
        final boolean internalDriver = Thread.currentThread().getContextClassLoader() == driverClass.getClassLoader();

        /* Build data source config from the merged application configuration. */
        final HikariConfig config = new HikariConfig();
        config.setAutoCommit(false);
        config.setConnectionTestQuery(environment.getProperty("waldo.database.internal_pool.test_query"));
        config.setDriverClassName(environment.getProperty("waldo.database.internal_pool.driver_class"));
        config.setIdleTimeout(environment.getProperty("waldo.database.internal_pool.idle_timeout", Integer.TYPE));
        config.setJdbcUrl(environment.getProperty("waldo.database.internal_pool.url"));
        config.setMaximumPoolSize(environment.getProperty("waldo.database.internal_pool.max_size", Integer.TYPE));
        config.setMaxLifetime(environment.getProperty("waldo.database.internal_pool.max_lifetime", Integer.TYPE));
        config.setMinimumIdle(environment.getProperty("waldo.database.internal_pool.min_size", Integer.TYPE));
        config.setPassword(environment.getProperty("waldo.database.internal_pool.password"));
        config.setRegisterMbeans(true);
        config.setUsername(environment.getProperty("waldo.database.internal_pool.user_name"));

        /* Create, wrap, and return the data source. */
        final HikariDataSource dataSource = new HikariDataSource(config);
        final InternalDataSourceWrapper result = new InternalDataSourceWrapperImpl(driverClass, dataSource,
                internalDriver);
        LOG.info("Returning wrapper {} for internally managed data source {}.", result, dataSource);
        return result;
    }
}
