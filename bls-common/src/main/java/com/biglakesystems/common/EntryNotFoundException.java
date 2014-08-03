package com.biglakesystems.common;

/**
 * {@link EntryNotFoundException} is thrown when an attempt to locate an entry in a map or other dictionary-like source
 * fails because no entry is found which matches the given key.
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
public class EntryNotFoundException extends RuntimeException
{
    private final String m_key;

    /**
     * Construct a {@link EntryNotFoundException} instance.
     *
     * @param message the exception message.
     */
    public EntryNotFoundException(final String key, final String message)
    {
        super(message);
        Assert.argumentNotNull("key", m_key = key);
    }

    /**
     * Construct a {@link EntryNotFoundException} instance.
     */
    public EntryNotFoundException(final String key, final String message, final Throwable cause)
    {
        super(message, cause);
        Assert.argumentNotNull("key", m_key = key);
    }

    /**
     * Get the entry key which was not found.
     *
     * @return {@link String} key.
     */
    public String getKey()
    {
        return m_key;
    }
}
