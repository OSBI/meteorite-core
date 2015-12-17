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

package bi.meteorite.core.security.jaas;

import bi.meteorite.core.api.security.AdminLoginService;
import bi.meteorite.core.api.security.IUserManagementProvider;
import bi.meteorite.core.api.security.exceptions.MeteoriteSecurityException;

import org.apache.felix.utils.properties.Properties;
import org.apache.karaf.jaas.boot.ProxyLoginModule;
import org.apache.karaf.jaas.config.JaasRealm;
import org.apache.karaf.jaas.modules.BackingEngine;
import org.apache.karaf.jaas.modules.BackingEngineFactory;
import org.apache.karaf.jaas.modules.BackingEngineService;
import org.apache.karaf.jaas.modules.properties.PropertiesBackingEngine;

import com.sun.security.auth.UserPrincipal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.*;

/**
 * Created by bugg on 03/12/15.
 */
public class TestJaasLoginManager {

  private BackingEngineService backingengine;
  private JaasRealm realm;
  private IUserManagementProvider jaasUserManager;
  private AdminLoginService jaasLoginManager;


  @Before
  public void setupMockedBackend() {
    jaasLoginManager = new JaasLoginManager();
    backingengine = mock(BackingEngineService.class);
    realm = mock(JaasRealm.class);
    when(realm.getName()).thenReturn("karaf");
    AppConfigurationEntry a = mock(AppConfigurationEntry.class);

    Map<String, Object> map = new HashMap<>();
    map.put("org.apache.karaf.jaas.module", "mock");
    when(a.getOptions().get(ProxyLoginModule.PROPERTY_MODULE)).thenReturn(map);
    AppConfigurationEntry[] array = { a };

    when(realm.getEntries()).thenReturn(array);
    BackingEngineFactory backingEngineFactory = mock(BackingEngineFactory.class);
    BackingEngine be = spy(new PropertiesBackingEngine(new Properties()));
    when(backingEngineFactory.build(anyMap())).thenReturn(be);
    List<BackingEngineFactory> l = new ArrayList<>();
    l.add(backingEngineFactory);
    l.add(backingEngineFactory);
    when(backingengine.getEngineFactories()).thenReturn(l);
    jaasUserManager = new JaasUserManager();


    jaasUserManager.setRealm(realm);
    jaasUserManager.setBackingEngineService(backingengine);
    jaasLoginManager.setRealm(realm.getName());

    LoginContext lcMock = mock(LoginContext.class);


    Subject s = new Subject();

    s.getPrincipals().add(new UserPrincipal("ROLE_USER"));
    s.getPrincipals().add(new UserPrincipal("ROLE_ADMIN"));


    when(lcMock.getSubject()).thenReturn(s);

    jaasLoginManager.setLoginContext(lcMock);

//    try {
//      jaasUserManager.addUser("test", "password");
//    } catch (MeteoriteSecurityException e) {
//      e.printStackTrace();
//    }

  }

  @Test
  public void testLogin() throws MeteoriteSecurityException {

    boolean ans = jaasLoginManager.login("test", "password");

    assertEquals(ans, true);
  }

  @Test
  public void testLoginWrongUsername() throws LoginException {

    failedLogin();

    boolean ans = jaasLoginManager.login("t1est", "password");

    assertEquals(ans, false);
  }

  @Test
  public void testLoginWrongPassword() throws LoginException {

    failedLogin();

    boolean ans = jaasLoginManager.login("test", "password1");

    assertEquals(ans, false);
  }

  @Test
  public void testLogout() {
    boolean ans = jaasLoginManager.login("test", "password");

    assertEquals(ans, true);

    ans = jaasLoginManager.logout("test");

    assertEquals(ans, true);

  }

  @Test
  public void testLogoutWithoutLogin() {

    boolean ans = jaasLoginManager.logout("test");

    assertEquals(ans, true);

  }


  private void failedLogin() throws LoginException {
    LoginContext lcMock = mock(LoginContext.class);


    Subject s = new Subject();

    s.getPrincipals().add(new UserPrincipal("ROLE_USER"));
    s.getPrincipals().add(new UserPrincipal("ROLE_ADMIN"));


    when(lcMock.getSubject()).thenReturn(s);

    jaasLoginManager.setLoginContext(lcMock);

    Mockito.doThrow(new LoginException("login failed")).when(lcMock).login();

  }

  private void failedLogout() throws LoginException {
    LoginContext lcMock = mock(LoginContext.class);


    Subject s = new Subject();

    s.getPrincipals().add(new UserPrincipal("ROLE_USER"));
    s.getPrincipals().add(new UserPrincipal("ROLE_ADMIN"));


    when(lcMock.getSubject()).thenReturn(s);

    jaasLoginManager.setLoginContext(lcMock);

    Mockito.doThrow(new LoginException("login failed")).when(lcMock).logout();

  }
}
