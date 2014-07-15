package waldo.impl.service.adds;

import waldo.service.acquisition.DataAcquisitionException;
import waldo.utility.network.ContentCallback;
import waldo.utility.network.HttpUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * {@link AddsDataAcquirer} ...
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
class AddsDataAcquirer
{
    private final List<AddsContentParser> m_contentParsers;
    private final AddsDirectoryParser m_directoryParser;
    private final URI m_directoryUri;
    private final HttpUtils m_httpUtils;

    /**
     * Construct an {@link AddsDataAcquirer} instance.
     *
     * @param directoryParser the {@link AddsDirectoryParser} component.
     * @param contentParsers the {@link AddsContentParser} component(s).
     * @param httpUtils the {@link HttpUtils} component.
     */
    AddsDataAcquirer(final AddsDirectoryParser directoryParser, final List<? extends AddsContentParser> contentParsers,
                     final HttpUtils httpUtils)
    {
        super();
        m_directoryParser = directoryParser;
        m_httpUtils = httpUtils;
        m_directoryUri = URI.create("http://www.aviationweather.gov/adds/dataserver_current/current/");
        m_contentParsers = Collections.unmodifiableList(new ArrayList<>(contentParsers));
    }

    public void acquire()
    {
        /* Loop over all data files available in the ADDS current data directory. */
        for (final AddsContent content : readAvailableContent())
        {
            for (final AddsContentParser parser : m_contentParsers)
            {
                if (parser.supports(content))
                {
                    acquireContent(content, parser);
                }
            }
        }
    }

    private void acquireContent(final AddsContent content, final AddsContentParser parser)
    {
        m_httpUtils.get(content.getLocation(), new ContentCallback<Object>()
        {
            /** {@inheritDoc} */
            @Override
            public Object execute(final InputStream content, final String contentType) throws IOException
            {
                parser.parse(uncompressedStream(content, contentType), contentType);
                return null;
            }
        });
    }

    /**
     * Read available content, as a list of {@link AddsContent} instances, from the ADDS current data directory. This
     * issues a request to the data server and parses its (HTML) response.
     *
     * @return {@link List} of {@link AddsContent} instances describing the available content.
     * @throws DataAcquisitionException if an error occurs while reading the directory.
     */
    private List<AddsContent> readAvailableContent() throws DataAcquisitionException
    {
        return m_httpUtils.get(m_directoryUri, new ContentCallback<List<AddsContent>>()
        {
            /** {@inheritDoc} */
            @Override
            public List<AddsContent> execute(final InputStream content, final String contentType) throws IOException
            {
                return m_directoryParser.parse(content, contentType, m_directoryUri);
            }
        });
    }

    private InputStream uncompressedStream(final InputStream stream, final String contentType) throws IOException
    {
        final InputStream result;
        if (!"application/x-gzip".equals(contentType))
        {
            result = stream;
        }
        else
        {
            result = new GZIPInputStream(stream);
        }
        return result;
    }
}
