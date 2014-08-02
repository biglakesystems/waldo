package com.biglakesystems.common;

/**
 * {@link Assert} ...
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
public class Assert
{
    /**
     * Construct an {@link Assert} instance.
     */
    private Assert()
    {
        super();
    }

    /**
     * Assert that a method or constructor argument is not null.
     *
     * @param name the argument name to include if an exception is thrown.
     * @param value the argument value.
     * @throws IllegalArgumentException if the argument value was {@code null}.
     */
    public static void argumentNotNull(String name, Object value) throws IllegalArgumentException
    {
        if (null == value)
        {
            throw new IllegalArgumentException(String.format("Argument [%s] cannot be null.", name));
        }
    }
}
