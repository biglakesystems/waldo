package waldo.config.app;

import org.springframework.beans.factory.DisposableBean;

import javax.sql.DataSource;

/**
 * {@link InternalDataSourceWrapper} defines the public interface to an object which wraps an internally managed
 * {@link DataSource} with information on its underlying JDBC driver in an implementation of the {@link DisposableBean}
 * interface, so that the driver can be deregistered at shutdown time if it was loaded within the web application
 * class loader.
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
interface InternalDataSourceWrapper extends DisposableBean
{
    /**
     * Get the wrapped data source.
     *
     * @return {@link DataSource} instance.
     */
    DataSource getDataSource();
}
