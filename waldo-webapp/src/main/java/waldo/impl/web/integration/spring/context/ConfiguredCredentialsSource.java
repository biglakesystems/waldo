package waldo.impl.web.integration.spring.context;

import com.biglakesystems.common.Assert;
import com.biglakesystems.common.EntryNotFoundException;
import com.biglakesystems.common.security.Credentials;
import com.biglakesystems.common.security.CredentialsSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.PropertyResolver;

/**
 * {@link ConfiguredCredentialsSource} is an implementation of the {@link CredentialsSource} interface which loads
 * credentials from the application bootstrap configuration. Credentials are stored under a pair of keys in the
 * configuration, one each for the username and the password:
 * <ul>
 *     <li/><code>waldo.credentials.username.<em>name</em></code> stores the user name.
 *     <li/><code>waldo.credentials.password.<em>name</em></code> stores the password.
 * </ul>
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
class ConfiguredCredentialsSource implements CredentialsSource
{
    private static final Logger LOG = LoggerFactory.getLogger(ConfiguredCredentialsSource.class);

    private final PropertyResolver m_resolver;

    /**
     * Construct a {@link ConfiguredCredentialsSource} instance.
     *
     * @param resolver the {@link PropertyResolver} through which the configuration will be accessed.
     */
    ConfiguredCredentialsSource(final PropertyResolver resolver)
    {
        super();
        Assert.argumentNotNull("resolver", m_resolver = resolver);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Credentials getCredentials(final String name) throws EntryNotFoundException
    {
        Assert.argumentNotBlank("name", name);

        /* Format the principal and credentials key; if the principal is not found in the configuration, throw with a
        message that includes the keys under which it should be configured. */
        final String principalKey = String.format("waldo.credentials.username.%s", name);
        final String credentialsKey = String.format("waldo.credentials.password.%s", name);
        final String principal = m_resolver.getProperty(principalKey);
        if (null == principal)
        {
            throw new EntryNotFoundException(name,
                    String.format("Found no credentials under name [%s]. Username should be configured under key [%s] and password under key [%s].",
                            name, principalKey, credentialsKey));
        }

        /* Wrap and return the credentials. */
        final String credentials = m_resolver.getProperty(credentialsKey);
        final Credentials result =
                new ConfiguredCredentials(name, principal, credentials, principalKey, credentialsKey);
        LOG.debug("Returning credentials {} from configuration {}.", result, m_resolver);
        return result;
    }
}
