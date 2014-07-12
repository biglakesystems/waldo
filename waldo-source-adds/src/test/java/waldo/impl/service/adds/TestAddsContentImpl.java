package waldo.impl.service.adds;

import org.joda.time.DateTime;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.URI;

import static org.junit.Assert.*;

/**
 * {@link TestAddsContentImpl} provides unit test coverage for {@link AddsContentImpl}.
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
public class TestAddsContentImpl
{
    /**
     * Construct a {@link TestAddsContentImpl} instance.
     */
    public TestAddsContentImpl()
    {
        super();
    }

    /**
     * Test the implementation of all simple getters.
     */
    @Test
    public void testGetters()
    {
        final DateTime now = DateTime.now();
        final AddsContent instance = new AddsContentImpl("testFileName", URI.create("http://www.test.com"),
                new BigDecimal("1.234"), now);
        assertEquals("testFileName", instance.getFileName());
        assertEquals(URI.create("http://www.test.com"), instance.getLocation());
        assertEquals(new BigDecimal("1.234"), instance.getSize());
        assertEquals(now, instance.getModified());
    }

    /**
     * Test the implementation of {@link #equals(Object)}.
     */
    @Test
    public void testEquals()
    {
        final DateTime now = DateTime.now();
        final AddsContent instance = new AddsContentImpl("testFileName", URI.create("http://www.test.com"),
                new BigDecimal("1.234"), now);
        final AddsContent check = new AddsContentImpl("testFileName", URI.create("http://www.test.com"),
                new BigDecimal("1.2340"), now);
        assertEquals(instance, check);
        assertEquals(instance.hashCode(), check.hashCode());
    }
}
