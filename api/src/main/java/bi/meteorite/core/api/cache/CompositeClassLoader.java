/*
 * Copyright 2015 OSBI Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package bi.meteorite.core.api.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CompositeClassLoader For Fixing Bundle Stuff
 */
public class CompositeClassLoader extends ClassLoader {

  private final List classLoaders = Collections.synchronizedList(new ArrayList());

  public CompositeClassLoader() {
    add(Object.class.getClassLoader()); // bootstrap loader.
    add(getClass().getClassLoader()); // whichever classloader loaded this jar.
  }

  /**
   * Add a loader to the n
   *
   * @param classLoader
   */
  public void add(ClassLoader classLoader) {
    if (classLoader != null) {
      classLoaders.add(0, classLoader);
    }
  }

  public Class loadClass(String name) throws ClassNotFoundException {
    for (Object classLoader1 : classLoaders) {
      ClassLoader classLoader = (ClassLoader) classLoader1;
      try {
        return classLoader.loadClass(name);
      } catch (ClassNotFoundException notFound) {
        // ok.. try another one
      }
    }
    // One last try - the context class loader associated with the current thread. Often used in j2ee servers.
    // Note: The contextClassLoader cannot be added to the classLoaders list up front as the thread that constructs
    // XStream is potentially different to thread that uses it.
    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    if (contextClassLoader != null) {
      return contextClassLoader.loadClass(name);
    } else {
      throw new ClassNotFoundException(name);
    }
  }

}
