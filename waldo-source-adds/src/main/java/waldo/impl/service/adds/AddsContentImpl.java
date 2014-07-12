package waldo.impl.service.adds;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.net.URI;

/**
 * {@link AddsContentImpl} is the concrete implementation of the {@link AddsContent} interface.
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
class AddsContentImpl implements AddsContent
{
    private static final long serialVersionUID = 1L;

    private final String m_fileName;
    private final int m_hashCodeValue;
    private final URI m_location;
    private final DateTime m_modified;
    private final BigDecimal m_size;

    /**
     * Construct a {@link AddsContentImpl} instance.
     *
     * @param fileName the name of the content file.
     * @param location the location of the file.
     * @param size the size of the file.
     * @param modified the last-modified date of the file.
     */
    AddsContentImpl(final String fileName, final URI location, final BigDecimal size, final DateTime modified)
    {
        super();
        m_fileName = fileName;
        m_location = location;
        m_modified = modified;
        m_size = size;
        m_hashCodeValue = new HashCodeBuilder(3, 17).append(m_fileName)
                .append(m_location)
                .append(m_modified)
                .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object baseOther)
    {
        final boolean result;
        if (this == baseOther)
        {
            result = true;
        }
        else if (null == baseOther || !getClass().equals(baseOther.getClass()))
        {
            result = false;
        }
        else
        {
            final AddsContentImpl other = (AddsContentImpl) baseOther;
            result = m_hashCodeValue == other.m_hashCodeValue &&
                    ObjectUtils.equals(m_fileName, other.m_fileName) &&
                    ObjectUtils.equals(m_location, other.m_location) &&
                    ObjectUtils.equals(m_modified, other.m_modified) &&
                    0 == m_size.compareTo(other.m_size);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return m_hashCodeValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("fileName", m_fileName)
                .append("location", m_location)
                .append("size", m_size)
                .append("modified", m_modified)
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileName()
    {
        return m_fileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URI getLocation()
    {
        return m_location;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DateTime getModified()
    {
        return m_modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getSize()
    {
        return m_size;
    }
}
