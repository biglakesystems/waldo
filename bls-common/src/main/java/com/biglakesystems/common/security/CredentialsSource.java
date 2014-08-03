package com.biglakesystems.common.security;

import com.biglakesystems.common.EntryNotFoundException;

/**
 * {@link CredentialsSource} defines the public interface to an object which provides named sets of credentials,
 * typically user name and password pairs, for some other piece of code. This separates the mechanism for loading these
 * credentials from the code that must make use of them.
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
public interface CredentialsSource
{
    /**
     * Get the credentials which are associated with a given name.
     *
     * @param name the name of the credentials.
     * @return {@link Credentials} instance, never {@code null}.
     * @throws EntryNotFoundException if no entry was associated with the given name.
     */
    Credentials getCredentials(String name) throws EntryNotFoundException;
}
