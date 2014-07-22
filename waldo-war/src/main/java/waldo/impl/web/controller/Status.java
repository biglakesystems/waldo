package waldo.impl.web.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.Writer;

/**
 * {@link Status} ...
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
@RequestMapping("/_status")
class Status
{
    /**
     * Construct a {@link Status} instance.
     */
    Status()
    {
        super();
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String get()
    {
        return "{\"status\": \"OK\"}";
    }

    @RequestMapping(value = "/logs", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void getLogs(final HttpServletResponse response) throws Throwable
    {
        final File file = new File("/var/log/tomcat7/catalina.out");
        try (final Reader reader = new FileReader(file))
        {
            final Writer writer = response.getWriter();
            IOUtils.copy(reader, writer);
        }
    }
}
