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

import com.hazelcast.core.HazelcastInstance
import org.ops4j.pax.cdi.api.OsgiService
import org.osgi.framework.BundleContext
import org.osgi.framework.wiring.BundleWiring
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.annotation.PostConstruct
import javax.inject.{Named, Inject, Singleton}

/**
  * Token Storage.
  */
@Singleton
@Named("CManager")
object CacheManagerTokenStore {
  private var logger: Logger = LoggerFactory.getLogger(classOf[CacheManagerTokenStore])
}

@Singleton class CacheManagerTokenStore extends TokenStorageProvider {
  @OsgiService
  @Inject private var cacheManager: HazelcastInstance = null
  @Inject private var bcontext: BundleContext = null

  @PostConstruct def init {
    CacheManagerTokenStore.logger.debug("*** Activating CacheManager")
    val c: CompositeClassLoader = new CompositeClassLoader
    val tccl: ClassLoader = Thread.currentThread.getContextClassLoader
    try {
      cacheManager.getConfig.setClassLoader(c)
    } finally {
      cacheManager.getConfig.setClassLoader(c)
    }
  }

  def addToken(token: Token) {
    addInvokerClassLoader(this.getClass.getClassLoader)
    cacheManager.getMap("tokens").put(token.getToken, token)
  }

  def updateToken(token: Token) {
  }

  def getToken(token: String): Token = {
    addInvokerClassLoader(getInvokerClassLoader)
    cacheManager.getMap("tokens").get(token).asInstanceOf[Token]
  }

  def hasToken(token: String): Boolean = {
    addInvokerClassLoader(getInvokerClassLoader)
    cacheManager.getMap("tokens").get(token) != null
  }

  def removeToken(token: Token) {
  }

  def setCacheManagerService(hazel: HazelcastInstance) {
    this.cacheManager = hazel
  }

  protected def addInvokerClassLoader(cl: ClassLoader) {
    getInstance.getConfig.getClassLoader.asInstanceOf[CompositeClassLoader].add(cl)
  }

  protected def getInvokerClassLoader: ClassLoader = {
    bcontext.getBundle.adapt(classOf[BundleWiring]).getClassLoader
  }

  def setBcontext(bcontext: BundleContext) {
    this.bcontext = bcontext
  }

  def getInstance: HazelcastInstance = {
    cacheManager
  }
}
