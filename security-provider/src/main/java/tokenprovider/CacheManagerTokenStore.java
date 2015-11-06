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

package tokenprovider;

import com.hazelcast.core.HazelcastInstance;

import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bugg on 20/07/15.
 */
public class CacheManagerTokenStore implements TokenStorageProvider {

  private HazelcastInstance cacheManager;
  private BundleContext bcontext;
  private static Logger logger = LoggerFactory.getLogger(CacheManagerTokenStore.class);

  public void init() {

    logger.debug("*** Activating CacheManager");


    CompositeClassLoader c = new CompositeClassLoader();

    ClassLoader tccl = Thread.currentThread().getContextClassLoader();
    try {
//      Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
      cacheManager.getConfig().setClassLoader(c);

      /**
       * Start threads, or establish connections, here, now
       */
    } finally {
      //Thread.currentThread().setContextClassLoader(tccl);
      cacheManager.getConfig().setClassLoader(c);

    }
    //addInvokerClassLoader(getClass().getClassLoader());


  }

  @Override
  public void addToken(Token token) {
    addInvokerClassLoader(this.getClass().getClassLoader());
    cacheManager.getMap("tokens").put(token.getToken(), token);
  }

  @Override
  public void updateToken(Token token) {

  }

  @Override
  public Token getToken(String token) {
    addInvokerClassLoader(getInvokerClassLoader());

    return (Token) cacheManager.getMap("tokens").get(token);
  }

  @Override
  public boolean hasToken(String token) {
    addInvokerClassLoader(getInvokerClassLoader());

    return cacheManager.getMap("tokens").get(token) != null;
  }

  @Override
  public void removeToken(Token token) {

  }

  public void setCacheManagerService(HazelcastInstance hazel) {
    this.cacheManager = hazel;
  }

  protected void addInvokerClassLoader(ClassLoader cl) {
    ((CompositeClassLoader) getInstance().getConfig().getClassLoader()).add(cl);
  }

  protected ClassLoader getInvokerClassLoader() {
    return bcontext.getBundle().adapt(BundleWiring.class).getClassLoader();
  }

  public void setBcontext(BundleContext bcontext) {
    this.bcontext = bcontext;
  }

  public HazelcastInstance getInstance() {
    return cacheManager;
  }

}
