package waldo.impl.utility.network;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import waldo.utility.network.ContentCallback;
import waldo.utility.network.HttpUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * {@link HttpUtilsImpl} is the concrete implementation of the {@link HttpUtils} interface.
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
class HttpUtilsImpl implements HttpUtils
{
    private final HttpClient m_httpClient;

    /**
     * Construct an {@link HttpUtilsImpl} instance.
     */
    HttpUtilsImpl()
    {
        this(HttpClients.createDefault());
    }

    /**
     * Construct an {@link HttpUtilsImpl} instance.
     *
     * @param httpClient the {@link HttpClient} component.
     */
    HttpUtilsImpl(final HttpClient httpClient)
    {
        super();
        m_httpClient = httpClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T get(final URI uri, final ContentCallback<T> callback) throws NetworkException
    {
        final T result;
        final HttpGet get = new HttpGet(uri);
        try
        {
            try (final Closeable baseResponse = (Closeable) m_httpClient.execute(get))
            {
                final HttpResponse response = (HttpResponse) baseResponse;
                if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode())
                {
                    throw new NetworkException(
                            String.format("Got unexpected response status [%s] from GET request for content at [%s].",
                                    response.getStatusLine(), uri));
                }
                else
                {
                    final HttpEntity entity = response.getEntity();
                    try (final InputStream stream = entity.getContent())
                    {
                        result = callback.execute(stream, entity.getContentType().getValue());
                    }
                }
            }
        }
        catch (final IOException e)
        {
            throw new NetworkException(String.format(
                    "An error of type %s occurred while attempting to issue GET request to ADDS content at [%s]: %s",
                    e.getClass().getName(), uri, e.getMessage()), e);
        }
        return result;
    }
}
