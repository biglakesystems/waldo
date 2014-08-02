package com.biglakesystems.common.spring;

import org.springframework.core.io.ResourceLoader;

/**
 * {@link DispatchingResourceLoaderBuilder} ...
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
public interface DispatchingResourceLoaderBuilder
{
    /**
     * Add a resource loader which will handle loading of resources with a given location prefix. The prefix will be
     * followed by a colon in resource location strings, but the colon should <em>not</em> be included in the string
     * passed as the {@code prefix} argument.
     *
     * @param prefix the resource location prefix to be handled by this loader, not including the trailing colon.
     * @param loader the resource loader.
     * @return {@link DispatchingResourceLoaderBuilder} this builder instance.
     */
    DispatchingResourceLoaderBuilder addPrefixedLoader(String prefix, ResourceLoader loader);

    /**
     * Create a {@link ResourceLoader} from the accumulated state in this builder instance.
     *
     * @return {@link ResourceLoader} instance.
     */
    ResourceLoader toResourceLoader();
}
