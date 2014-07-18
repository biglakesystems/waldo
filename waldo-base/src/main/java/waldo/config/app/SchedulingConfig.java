package waldo.config.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import waldo.Constants;

/**
 * {@link SchedulingConfig} ...
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
@EnableScheduling
@SuppressWarnings("unused")
class SchedulingConfig
{
    private static final Logger LOG = LoggerFactory.getLogger(SchedulingConfig.class);

    /**
     * Construct a {@link SchedulingConfig} instance.
     */
    SchedulingConfig()
    {
        super();
    }

    /**
     * Create the worker thread pool, which is shared by Spring scheduling and other third-party libraries which handle
     * tasks on background threads.
     *
     * @return {@link ThreadPoolTaskScheduler} instance.
     */
    @Bean
    ThreadPoolTaskScheduler taskScheduler(final Environment environment)
    {
        /* Get the configured pool size and create the thread pool. */
        final int poolSize = environment.getProperty(Constants.Scheduling.POOL_SIZE, Integer.class);
        final ThreadPoolTaskScheduler result = new ThreadPoolTaskScheduler();
        result.setDaemon(true);
        result.setPoolSize(poolSize);
        LOG.info("Returning scheduled task executor {} with {} thread(s) ({} processor(s) present.)", result,
                poolSize, Runtime.getRuntime().availableProcessors());
        return result;
    }
}
