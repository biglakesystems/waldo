package waldo.impl.service.adds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * {@link TafCacheCsvHandler} ...
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
class TafCacheCsvHandler implements AddsContentParser
{
    /**
     * Construct a {@link TafCacheCsvHandler} instance.
     */
    TafCacheCsvHandler()
    {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void parse(final InputStream stream, final String contentType) throws IOException
    {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "ASCII"));
        String line;
        while (null != (line = reader.readLine()))
        {
            System.out.println(line);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(final AddsContent content)
    {
        return "tafs.cache.csv.gz".equals(content.getFileName());
    }
}
