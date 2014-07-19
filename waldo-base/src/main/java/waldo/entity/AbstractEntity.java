package waldo.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * {@link AbstractEntity} ...
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
@MappedSuperclass
public abstract class AbstractEntity implements EntityObject
{
    @Id
    @Access(AccessType.PROPERTY)
    private Long id;

    @Version
    @Column(name = "VER")
    private int version;

    /**
     * Construct a {@link AbstractEntity} instance.
     */
    public AbstractEntity()
    {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("id", id).toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Long getId()
    {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getVersion()
    {
        return version;
    }

    private void setId(final Long id)
    {
        this.id = id;
    }
}
