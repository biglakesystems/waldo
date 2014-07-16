package waldo.config.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * {@link CommonDatabaseConfig} handles portions of the database configuration which are common to all environments.
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
@SuppressWarnings("unused")
class CommonDatabaseConfig
{
    private static final Logger LOG = LoggerFactory.getLogger(CommonDatabaseConfig.class);

    /**
     * Construct a {@link CommonDatabaseConfig} instance.
     */
    CommonDatabaseConfig()
    {
        super();
    }

    /**
     * Create the JDBC template.
     *
     * @param dataSource the application data source.
     * @return {@link JdbcOperations} instance.
     */
    @Bean
    public JdbcOperations jdbcTemplate(final DataSource dataSource)
    {
        final JdbcOperations result = new JdbcTemplate(dataSource);
        LOG.info("Returning JDBC template {}.", result);
        return result;
    }
}
