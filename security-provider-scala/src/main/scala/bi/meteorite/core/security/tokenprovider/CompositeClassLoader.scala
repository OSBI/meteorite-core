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
package bi.meteorite.core.security.tokenprovider

import java.util
import java.util.Collections

/**
  * CompositeClassLoader For Fixing Bundle Stuff
  */
class CompositeClassLoader extends ClassLoader {
  private final val classLoaders = Collections.synchronizedList(new util.ArrayList[ClassLoader])

    add(classOf[AnyRef].getClassLoader)
    add(getClass.getClassLoader)


  /**
    * Add a loader to the n
    *
    * @param classLoader The classloader to add.
    */
  def add(classLoader: ClassLoader) {
    if (classLoader != null) {
      classLoaders.add(0, classLoader)
    }
  }

  @throws(classOf[ClassNotFoundException])
  override def loadClass(name: String): Class[_] = {
    import scala.collection.JavaConversions._
    for (classLoader1 <- classLoaders) {
      val classLoader: ClassLoader = classLoader1.asInstanceOf[ClassLoader]
      try {
        return classLoader.loadClass(name)
      }
      catch {
        case notFound: ClassNotFoundException =>
      }
    }
    val contextClassLoader: ClassLoader = Thread.currentThread.getContextClassLoader
    if (contextClassLoader != null) {
      contextClassLoader.loadClass(name)
    }
    else {
      throw new ClassNotFoundException(name)
    }
  }
}
