package waldo.impl.web.integration.spring.context;

import com.biglakesystems.common.Assert;
import com.biglakesystems.common.security.Credentials;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * {@link ConfiguredCredentials} is an implementation of the {@link Credentials} interface which holds a credential
 * pair which was loaded from the application configuration. Holds both the credentials themselves, and the
 * configuration keys from which they were loaded.
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
class ConfiguredCredentials implements Credentials, Serializable
{
    private static final long serialVersionUID = 1L;

    private final String m_name;
    private final String m_credentials;
    private final String m_credentialsKey;
    private final String m_principal;
    private final String m_principalKey;

    /**
     * Construct a {@link ConfiguredCredentials} instance.
     *
     * @param name the name associated with the credential pair.
     * @param principal the principal.
     * @param credentials the credentials.
     * @param principalKey the configuration key from which the principal was loaded.
     * @param credentialsKey the configuration key from which the credentials were loaded.
     */
    ConfiguredCredentials(final String name, final String principal, final String credentials,
                          final String principalKey, final String credentialsKey)
    {
        super();
        Assert.argumentNotBlank("credentialsKey", m_credentialsKey = credentialsKey);
        Assert.argumentNotBlank("name", m_name = name);
        Assert.argumentNotBlank("principal", m_principal = principal);
        Assert.argumentNotBlank("principalKey", m_principalKey = principalKey);
        m_credentials = credentials;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("name", m_name).append("principal", m_principal)
                .append("credentials", "REMOVED").append("principalKey", m_principalKey)
                .append("credentialsKey", m_credentialsKey).toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return m_name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getPrincipal()
    {
        return m_principal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getCredentials()
    {
        return m_credentials;
    }
}
