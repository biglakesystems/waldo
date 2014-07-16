package waldo.config.app;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import waldo.Constants;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * {@link InternalPoolDatabaseConfig} handles portions of the database configuration which apply only to environments
 * where the database is accessed by an internally managed connection pool. This configuration is enabled when the
 * {@link Constants.Profiles#DATABASE_INTERNAL_POOL} profile is active.
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
@Profile(Constants.Profiles.DATABASE_INTERNAL_POOL)
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
    public DataSource dataSource(final Environment environment) throws Throwable
    {
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

        /* Create and return the data source. */
        final HikariDataSource result = new HikariDataSource(config);
        LOG.debug("Returning internally managed data source {}.", result);
        return result;
    }
}
