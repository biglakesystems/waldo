package waldo.config.app;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link NetworkConfig} ...
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
class NetworkConfig
{
    private static final Logger LOG = LoggerFactory.getLogger(NetworkConfig.class);

    /**
     * Construct a {@link NetworkConfig} instance.
     */
    NetworkConfig()
    {
        super();
    }

    /**
     * Create the {@link HttpClient} instance which will be used when loading data and invoking external web services
     * via HTTP.
     *
     * @return {@link HttpClient} instance.
     */
    @Bean(destroyMethod = "close")
    public HttpClient httpClient()
    {
        final HttpClient result = HttpClients.createDefault();
        LOG.debug("Returning HttpClient component {}.", result);
        return result;
    }
}
