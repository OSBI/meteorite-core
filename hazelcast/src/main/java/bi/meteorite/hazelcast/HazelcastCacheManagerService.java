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


package bi.meteorite.hazelcast;

import bi.meteorite.core.api.cache.CacheManagerService;
import bi.meteorite.core.api.cache.CompositeClassLoader;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;

import java.util.Map;

/**
 * Hazelcast stuff.
 */
@OsgiServiceProvider(classes = { CacheManagerService.class })
public class HazelcastCacheManagerService implements CacheManagerService {

  private Config cfg;
  private HazelcastInstance instance;
  private BundleContext bcontext;

  public void setInstance(HazelcastInstance instance) {
    this.instance = instance;
  }

  public HazelcastInstance getInstance() {
    return instance;
  }

  public void init() {
    cfg = new Config();
    NetworkConfig network = cfg.getNetworkConfig();
    JoinConfig join = network.getJoin();
    join.getMulticastConfig().setEnabled(false);
    join.getTcpIpConfig().setEnabled(false);
    CompositeClassLoader c = new CompositeClassLoader();

    ClassLoader tccl = Thread.currentThread().getContextClassLoader();
    try {
//      Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
      cfg = cfg.setClassLoader(c);

    /**
     * Start threads, or establish connections, here, now
     */
    } finally {
      //Thread.currentThread().setContextClassLoader(tccl);
      cfg = cfg.setClassLoader(c);

    }
    instance = Hazelcast.newHazelcastInstance(cfg);
    //addInvokerClassLoader(getClass().getClassLoader());
  }

  @Override
  public Map getCache(String name) {
    addInvokerClassLoader(getInvokerClassLoader());
    return getInstance().getMap(name);
  }

  public Map getCache(String name, ClassLoader c) {
    addInvokerClassLoader(c);
    return getInstance().getMap(name);
  }

  protected ClassLoader getInvokerClassLoader() {
    return bcontext.getBundle().adapt(BundleWiring.class).getClassLoader();
  }

  protected void addInvokerClassLoader(ClassLoader cl) {
    ((CompositeClassLoader) getInstance().getConfig().getClassLoader()).add(cl);
  }

  public void setBcontext(BundleContext bcontext) {
    this.bcontext = bcontext;
  }
}
