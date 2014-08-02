package waldo.impl.web.integration.spring.context;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Properties;

/**
 * {@link PropertiesLoader} defines the public interface to an object which builds a merged {@link Properties} instance
 * from the contents of zero or more {@link Resource} instances representing Java {@code .properties} files. This
 * interface and its implementor exist primarily to improve the unit testability of downstream code; the implementation
 * will simply delegate to a {@link PropertiesFactoryBean} to handle the real work of loading and merging properties.
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
interface PropertiesLoader
{
    /**
     * Runtime implementation.
     */
    PropertiesLoader INSTANCE = PropertiesLoaderImpl.INSTANCE;

    /**
     * Load and merge the contents of zero or more {@code .properties} resources. Files are loaded in order; same-named
     * properties in a resource at a <em>higher</em> index in the input array will override properties at a
     * <em>lower</em> index in the array.
     *
     * @param resources the resources to merge.
     * @return {@link Properties} instance; never {@code null}.
     */
    Properties load(List<Resource> resources);
}
