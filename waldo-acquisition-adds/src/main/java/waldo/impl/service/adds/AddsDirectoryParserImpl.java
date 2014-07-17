package waldo.impl.service.adds;

import org.apache.commons.lang3.ObjectUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link AddsDirectoryParserImpl} is the concrete implementation of the {@link AddsDirectoryParser} interface.
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
class AddsDirectoryParserImpl implements AddsDirectoryParser
{
    private static final Logger LOG = LoggerFactory.getLogger(AddsDirectoryParserImpl.class);

    /**
     * Construct a {@link AddsDirectoryParserImpl} instance.
     */
    AddsDirectoryParserImpl()
    {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AddsContent> parse(final InputStream content, final String contentType, final URI baseUri)
            throws IllegalStateException, IOException
    {
        /* Expect an HTML directory. */
        final List<AddsContent> result;
        final MediaType type = MediaType.parseMediaType(contentType);
        if (!type.isCompatibleWith(MediaType.TEXT_HTML))
        {
            throw new IllegalStateException(String.format("Unsupported directory media type [%s].", type));
        }
        else
        {
            /* Parse the HTML directory. */
            final Document document;
            final String charset = ObjectUtils.defaultIfNull(type.getParameter("charset"), "UTF-8");
            try
            {
                document = Jsoup.parse(content, charset, baseUri.toString());
            }
            catch (final IOException e)
            {
                throw new IOException(String.format(
                        "An error of type %s occurred while attempting to read ADDS directory: %s",
                        e.getClass().getName(), e.getMessage()), e);
            }

            /* Directory should contain a header row (which we ignore) then one row per data file. */
            final Elements rows = document.select("tr:not(:first-child)");
            result = new ArrayList<>(rows.size());
            for (final Element nextRow : rows)
            {
                /* Read the next directory entry. */
                final Elements columns = nextRow.select("tt");
                final BigDecimal size = parseSize(columns.get(1).text());
                if (null != size)
                {
                    /* This is a file (not a directory); parse and add it to the result list. */
                    final String name = columns.get(0).text();
                    final String href = nextRow.getElementsByTag("a").get(0).attr("href");
                    final URI location = baseUri.resolve(href);
                    final DateTime modified = s_modified.parseDateTime(columns.get(2).text());
                    result.add(new AddsContentImpl(name, location, size, modified));
                }
            }
        }
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Returning {} entries read from ADDS directory with base URI [{}].", result.size(), baseUri);
        }
        return result;
    }

    /**
     * Parse a file size included in the HTML directory. Note that the magnitude suffix (should be "k") is ignored.
     *
     * @param size the file size column text.
     * @return {@link BigDecimal} file size.
     */
    private BigDecimal parseSize(final String size)
    {
        final BigDecimal result;
        final int space = size.indexOf(" ");
        if (-1 == space)
        {
            result = null;
        }
        else
        {
            result = new BigDecimal(size.substring(0, space));
        }
        return result;
    }

    /**
     * Format in which the last-modified date/time is included in the HTML directory.
     */
    static final DateTimeFormatter s_modified = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss z");
}
