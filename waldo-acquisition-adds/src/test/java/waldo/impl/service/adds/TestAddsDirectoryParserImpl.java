package waldo.impl.service.adds;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;

/**
 * {@link TestAddsDirectoryParserImpl} provides unit test coverage for {@link AddsDirectoryParserImpl}.
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
public class TestAddsDirectoryParserImpl
{
    /**
     * Construct a {@link TestAddsDirectoryParserImpl} instance.
     */
    public TestAddsDirectoryParserImpl()
    {
        super();
    }

    /**
     * Test the implementation of {@link AddsDirectoryParser#parse(InputStream, String, URI)}.
     *
     * @throws Throwable on unexpected error.
     */
    @Test
    public void testParse() throws Throwable
    {
        /* Request the data directory. */
        final URI baseUri = URI.create("http://www.aviationweather.gov/adds/dataserver_current/current/");
        final URL resource = getClass().getResource(String.format("%s_%s.html", getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName()));
        final InputStream content;
        try (final InputStream stream = resource.openStream())
        {
            content = new ByteArrayInputStream(IOUtils.toByteArray(stream));
        }

        /* Run the test and verify expectations. */
        final AddsDirectoryParser instance = new AddsDirectoryParserImpl();
        final List<AddsContent> files = instance.parse(content, "text/html;charset=UTF-8", baseUri);
        assertEquals(16, files.size());
        assertEquals(new AddsContentImpl("aircraftreports.cache.csv",
                URI.create("http://www.aviationweather.gov/adds/dataserver_current/current/aircraftreports.cache.csv"),
                new BigDecimal("313.7"),
                AddsDirectoryParserImpl.s_modified.parseDateTime("Sat, 12 Jul 2014 03:20:07 GMT")), files.get(0));
        assertEquals(new AddsContentImpl("tafs.cache.xml.gz",
                URI.create("http://www.aviationweather.gov/adds/dataserver_current/current/tafs.cache.xml.gz"),
                new BigDecimal("269.6"),
                AddsDirectoryParserImpl.s_modified.parseDateTime("Sat, 12 Jul 2014 03:20:05 GMT")), files.get(15));
    }
}
