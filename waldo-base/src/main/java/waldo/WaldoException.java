package waldo;

/**
 * {@link WaldoException} ...
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
public class WaldoException extends RuntimeException
{
    /**
     * Construct a {@link WaldoException} instance.
     *
     * @param message the exception message.
     */
    public WaldoException(final String message)
    {
        super(message);
    }

    /**
     * Construct a {@link WaldoException} instance.
     *
     * @param message the exception message.
     * @param cause the cause exception.
     */
    public WaldoException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
