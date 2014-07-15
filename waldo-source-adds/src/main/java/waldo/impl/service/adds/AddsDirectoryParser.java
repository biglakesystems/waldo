package waldo.impl.service.adds;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

/**
 * {@link AddsDirectoryParser} defines the public interface to an object which parses the raw content of the ADDS data
 * directory into a list of {@link AddsContent} instances describing the content files which are contained in the
 * directory.
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
interface AddsDirectoryParser
{
    /**
     * Parse an HTTP entity containing the ADDS content directory into a list of {@link AddsContent} instances, one per
     * content file.
     *
     * @param content the content of the directory.
     * @param contentType the content type of the directory.
     * @param baseUri the base URI against which to resolve relative URIs in the directory.
     * @return {@link List} of {@link AddsContent} instances.
     * @throws IllegalStateException if the directory is not in the supported format.
     * @throws IOException if an error occurs while reading the directory.
     */
    List<AddsContent> parse(InputStream content, String contentType, URI baseUri)
            throws IllegalStateException, IOException;
}
