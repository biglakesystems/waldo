package com.biglakesystems.common;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * {@link TestAssert} provides unit test coverage for {@link Assert}.
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
public class TestAssert
{
    /**
     * Construct a {@link TestAssert} instance.
     */
    public TestAssert()
    {
        super();
    }

    /**
     * Test the implementation of {@link Assert#argumentNotNull(String, Object)}.
     */
    @Test
    public void testArgumentNotNull()
    {
        Assert.argumentNotNull("testing", "nonNullValue");
        try
        {
            Assert.argumentNotNull("testing", null);
            fail("Invocation with null argument did not throw.");
        }
        catch (final IllegalArgumentException e)
        {
            /* Good. */
            assertTrue(e.getMessage().contains("testing"));
        }
    }
}
