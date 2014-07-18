package waldo;

import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

import javax.persistence.EntityManager;
import java.sql.Connection;

/**
 * {@link Constants} ...
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
public interface Constants
{
    /**
     * {@link Context} defines constants which correspond to various configuration items which can be applied to the
     * servlet context in {@code web.xml}.
     */
    interface Context
    {
        /**
         * The name which is applied to the property source in which the merged application configuration is added to
         * the Spring {@link Environment}.
         */
        String CONFIGURATION = "configuration";

        /**
         * Context parameter in which a comma-delimited list of application configuration {@code .properties} files is
         * supplied. The merged content of these properties are added to the Spring application context as a
         * {@link PropertySource} for access either through the {@link Environment} or through property placeholders in
         * Spring-managed objects.
         */
        String CONFIGURATION_LOCATIONS = "configurationLocations";
    }

    /**
     * {@link Profiles} defines constants which correspond to the various Spring profiles which may be active during the
     * execution of the application. These profiles generally allow certain subsystems, such as data access and
     * transaction management, to be configured differently based upon the type of container within which the
     * application is running.
     */
    interface Profiles
    {
        /**
         * Profile which is active when the application should create its own internal database connection pool, rather
         * than having one supplied by the container via JNDI.
         */
        String DATABASE_INTERNAL_POOL = "waldo.profile.database.internal_pool";

        /**
         * Profile which is active when the application should create its own internal JPA persistence unit, rather
         * than having one supplied by the container via JNDI.
         */
        String JPA_INTERNAL_PERSISTENCE_UNIT = "waldo.profile.jpa.internal_persistence_unit";

        /**
         * Prefix which indicates that a key/value in the merged application configuration identifies a profile which
         * should be activated in the Spring application context. For each configuration item whose key <em>starts
         * with</em> this prefix, the corresponding value is added to the list of active profiles for the application
         * context. Only this prefix is significant; the remainder of the key is ignored.
         */
        String PROFILE_CONFIGURATION_PREFIX = "waldo.profile.";

        /**
         * Profile which is active when JPA {@link EntityManager}-based transaction management is applicable.
         */
        String TRANSACTION_JPA = "waldo.profile.transaction.jpa";
    }

    /**
     * {@link Profiles} defines constants which correspond to various configuration items which control scheduled tasks,
     * and the thread pool in which they are executed.
     */
    interface Scheduling
    {
        /**
         * Size of the internally managed thread pool in which scheduled tasks are executed.
         */
        String POOL_SIZE = "waldo.scheduling.thread_pool_size";
    }
}
