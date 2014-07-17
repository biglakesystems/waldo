package waldo.impl.service.adds;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URI;

/**
 * {@link AddsContent} ...
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
interface AddsContent extends Serializable
{
    /**
     * Get the name of the content file.
     *
     * @return {@link String} file name.
     */
    String getFileName();

    /**
     * Get the location of the content.
     *
     * @return {@link URI} location.
     */
    URI getLocation();

    /**
     * Get the last-modified date/time of the content.
     *
     * @return {@link DateTime} value.
     */
    DateTime getModified();

    /**
     * Get the size of the content file.
     *
     * @return {@link BigDecimal} size.
     */
    BigDecimal getSize();
}
