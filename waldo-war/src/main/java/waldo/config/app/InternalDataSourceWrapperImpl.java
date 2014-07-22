package waldo.config.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

/**
 * {@link InternalDataSourceWrapperImpl} ...
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
class InternalDataSourceWrapperImpl implements InternalDataSourceWrapper
{
    private static final Logger LOG = LoggerFactory.getLogger(InternalDataSourceWrapperImpl.class);

    private final DataSource m_dataSource;
    private final Class<?> m_driverClass;
    private final boolean m_unregisterDriver;

    /**
     * Construct a {@link InternalDataSourceWrapperImpl} instance.
     *
     * @param driverClass the driver class.
     * @param dataSource the data source.
     * @param unregisterDriver flag indicating whether the driver should be unregistered at disposal time.
     */
    InternalDataSourceWrapperImpl(final Class<?> driverClass, final DataSource dataSource,
                                  final boolean unregisterDriver)
    {
        super();
        m_dataSource = dataSource;
        m_driverClass = driverClass;
        m_unregisterDriver = unregisterDriver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() throws Exception
    {
        /* Deregister the driver if it was loaded through the application class loader. */
        if (!m_unregisterDriver)
        {
            LOG.debug("Not deregistering JDBC driver of class {}.", m_driverClass.getName());
        }
        else
        {
            final Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements())
            {
                final Driver nextDriver = drivers.nextElement();
                if (m_driverClass.isInstance(nextDriver))
                {
                    DriverManager.deregisterDriver(nextDriver);
                    LOG.info("Successfully deregistered JDBC driver {} of class {}.", nextDriver,
                            m_driverClass.getName());
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataSource getDataSource()
    {
        return m_dataSource;
    }
}
