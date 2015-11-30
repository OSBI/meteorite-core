package bi.meteorite.core.security.jaas;

import bi.meteorite.core.api.security.IUserManagementProvider;
import bi.meteorite.core.api.security.exceptions.MeteoriteSecurityException;

import org.apache.felix.utils.properties.Properties;
import org.apache.karaf.jaas.boot.ProxyLoginModule;
import org.apache.karaf.jaas.config.JaasRealm;
import org.apache.karaf.jaas.modules.BackingEngine;
import org.apache.karaf.jaas.modules.BackingEngineFactory;
import org.apache.karaf.jaas.modules.BackingEngineService;
import org.apache.karaf.jaas.modules.properties.PropertiesBackingEngine;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.AppConfigurationEntry;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.when;

/**
 * Created by bugg on 30/11/15.
 */
public class TestJaasUserManager {

  private static BackingEngineService backingengine;
  private static JaasRealm realm;

  @BeforeClass
  public static void setupMockedBackend(){
    backingengine = Mockito.mock(BackingEngineService.class);
    realm = Mockito.mock(JaasRealm.class);
    AppConfigurationEntry a = Mockito.mock(AppConfigurationEntry.class);

    Map<String, Object> map = new HashMap<>();
    map.put("org.apache.karaf.jaas.module", "mock");
    when(a.getOptions().get(ProxyLoginModule.PROPERTY_MODULE)).thenReturn(map);
    AppConfigurationEntry[] array = {a};

    when(realm.getEntries()).thenReturn(array);
    BackingEngineFactory backingEngineFactory = Mockito.mock(BackingEngineFactory.class);
    BackingEngine be = Mockito.spy(new PropertiesBackingEngine(new Properties()));
    when(backingEngineFactory.build(anyMap())).thenReturn(be);
    List<BackingEngineFactory> l = new ArrayList<>();
    l.add(backingEngineFactory);
    l.add(backingEngineFactory);
    when(backingengine.getEngineFactories()).thenReturn(l);
  }

  @Test
  public void testAddUser() throws MeteoriteSecurityException {
    IUserManagementProvider jaasUserManager = new JaasUserManager();



    jaasUserManager.setRealm(realm);
    jaasUserManager.setBackingEngineService(backingengine);

    jaasUserManager.addUser("test", "password");
    assertThat(jaasUserManager.getUsers(), hasItem("test"));

  }


}
