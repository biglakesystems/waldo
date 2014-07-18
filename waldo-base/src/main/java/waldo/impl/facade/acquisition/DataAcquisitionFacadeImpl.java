package waldo.impl.facade.acquisition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import waldo.facade.acquisition.DataAcquisitionFacade;

import javax.persistence.EntityManager;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link DataAcquisitionFacadeImpl} is the concrete implementation of the {@link DataAcquisitionFacade} interface.
 * <p/>
 * <strong>Thread Safety:</strong> instances of this class contain no mutable state and are therefore safe for
 * multithreaded access, provided the same is true of all dependencies provided via constructor.
 * <p/>
 * Copyright 2014 Big Lake Systems, LLC.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
@Service
class DataAcquisitionFacadeImpl implements DataAcquisitionFacade, DataAcquisitionTasks
{
    private final EntityManager m_entityManager;
    private final AtomicInteger m_sequence = new AtomicInteger(0);

    /**
     * Construct a {@link DataAcquisitionFacadeImpl} instance.
     *
     * @param entityManager the transaction-aware {@link EntityManager} proxy.
     */
    @Autowired
    DataAcquisitionFacadeImpl(final EntityManager entityManager)
    {
        super();
        m_entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String doSomething()
    {
        return String.format("%d: %s", m_sequence.get(), "HelloBrain");
    }

    @Scheduled(fixedDelay = 60000L)
    public void increment()
    {
        m_sequence.getAndIncrement();
    }
}
