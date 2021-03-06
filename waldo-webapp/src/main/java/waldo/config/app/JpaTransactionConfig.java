package waldo.config.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import waldo.Constants;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * {@link JpaTransactionConfig} handles portions of the transaction management configuration which apply only to
 * environments where simple JPA {@link EntityManager}-based transactions are applicable. This configuration is enabled
 * when the {@link Constants.Profiles#TRANSACTION_JPA} profile is active.
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
@Profile(Constants.Profiles.TRANSACTION_JPA)
@SuppressWarnings("unused")
class JpaTransactionConfig
{
    private static final Logger LOG = LoggerFactory.getLogger(JpaTransactionConfig.class);

    /**
     * Construct a {@link JpaTransactionConfig} instance.
     */
    JpaTransactionConfig()
    {
        super();
    }

    /**
     * Create the simple JPA transaction manager.
     *
     * @param entityManagerFactory the {@link EntityManagerFactory} component.
     * @return {@link PlatformTransactionManager} instance.
     */
    @Bean
    PlatformTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory)
    {
        final PlatformTransactionManager result = new JpaTransactionManager(entityManagerFactory);
        LOG.info("Returning JPA transaction manager {}.", result);
        return result;
    }
}
