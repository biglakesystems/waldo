package waldo.impl.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import waldo.facade.acquisition.DataAcquisitionFacade;

import java.util.Date;

/**
 * {@link Home} ...
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
@Controller
@RequestMapping("/")
class Home
{
    private final DataAcquisitionFacade m_acquisitionFacade;

    /**
     * Construct a {@link Home} instance.
     *
     * @param acquisitionFacade the {@link DataAcquisitionFacade} service.
     */
    @Autowired
    Home(final DataAcquisitionFacade acquisitionFacade)
    {
        super();
        m_acquisitionFacade = acquisitionFacade;
    }

    /**
     * Get the application home page.
     *
     * @return {@link ModelAndView} home page model and view.
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView get()
    {
        final Date date = m_acquisitionFacade.doSomething();
        return new ModelAndView("full/Home", "model", date);
    }
}
