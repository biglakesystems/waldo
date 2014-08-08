package waldo.impl.daemon.adds;

import org.junit.Test;
import waldo.utility.network.HttpUtils;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.Collections;

/**
 * {@link TestAddsDataAcquirer} ...
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
public class TestAddsDataAcquirer
{
    /**
     * Construct a {@link TestAddsDataAcquirer} instance.
     */
    public TestAddsDataAcquirer()
    {
        super();
    }

    @Test
    public void testAcquire() throws Throwable
    {
        @SuppressWarnings("unchecked")
        final Class<HttpUtils> clazz = (Class<HttpUtils>) Class.forName("waldo.impl.utility.network.HttpUtilsImpl");
        final Constructor<HttpUtils> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        final HttpUtils httpUtils = constructor.newInstance();
        final AddsDirectoryParser parser = new AddsDirectoryParserImpl();
        final AddsDataAcquirer instance = new AddsDataAcquirer(parser,
                Collections.singletonList(new TafCacheCsvHandler()), httpUtils,
                URI.create("http://www.aviationweather.gov/adds/dataserver_current/current/"));
        instance.acquire();
    }
}
