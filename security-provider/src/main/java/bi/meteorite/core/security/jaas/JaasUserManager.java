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

import bi.meteorite.core.api.objects.MeteoriteUser;
import bi.meteorite.core.api.persistence.UserService;
import bi.meteorite.core.api.security.IUserManagementProvider;
import bi.meteorite.core.api.security.exceptions.MeteoriteSecurityException;
import bi.meteorite.core.security.objects.User;

import org.apache.karaf.jaas.boot.ProxyLoginModule;
import org.apache.karaf.jaas.boot.principal.RolePrincipal;
import org.apache.karaf.jaas.config.JaasRealm;
import org.apache.karaf.jaas.modules.BackingEngine;
import org.apache.karaf.jaas.modules.BackingEngineFactory;
import org.apache.karaf.jaas.modules.BackingEngineService;

import com.google.common.collect.ImmutableList;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.AppConfigurationEntry;


/**
 * Default JaaS User Manager to control users in Karaf.
 */
public class JaasUserManager implements IUserManagementProvider {

  private BackingEngineService backingEngineService;
  private JaasRealm realm;
  private UserService userService;

  private BackingEngine getEngine() {
    for (AppConfigurationEntry entry : realm.getEntries()) {
      String moduleClass = (String) entry.getOptions().get(ProxyLoginModule.PROPERTY_MODULE);
      if (moduleClass != null) {
        BackingEngineFactory factories =
            backingEngineService.getEngineFactories().get(1);
        Map<String, ?> options = entry.getOptions();
        return factories.build(options);
      }
    }
    return null;
  }

  @Override
  public void addUser(String u, String p) throws MeteoriteSecurityException {
    if (getUsers().contains(u)) {
      throw new MeteoriteSecurityException("User already exists");
    }
    getEngine().addUser(u, p);
  }

  @Override
  public void deleteUser(String u) throws MeteoriteSecurityException {
    if (getUsers().contains(u)) {
      getEngine().deleteUser(u);
    } else {
      throw new MeteoriteSecurityException("User Doesn't Exist");
    }
  }

  @Override
  public List<String> getUsers() throws MeteoriteSecurityException {
    MeteoriteUser u = new User();
    u.setId("1234");
    u.setUsername("test");
    userService.addUser(u);
    List<String> users = new ArrayList<>();
    for (org.apache.karaf.jaas.boot.principal.UserPrincipal user : getEngine().listUsers()) {
      users.add(user.getName());
    }
    return users;
  }

  @Override
  public List<String> getRoles(String u) throws MeteoriteSecurityException {
    List<String> s = new ArrayList<>();
    List u2 = getUsers();

    if (u2.contains(u)) {
      for (Principal p : getEngine().listUsers()) {
        if (p.getName().equals(u)) {
          for (RolePrincipal r : getEngine().listRoles(p)) {
            s.add(r.getName());
          }

        }
      }
      return ImmutableList.copyOf(s);
    } else {
      throw new MeteoriteSecurityException("User does not exist");
    }

  }

  @Override
  public void addRole(String u, String r) throws MeteoriteSecurityException {
    for (Principal p : getEngine().listUsers()) {
      if (p.getName().equals(u)) {
        List<RolePrincipal> roles = getEngine().listRoles(p);
        if (roles.size() == 0) {
          getEngine().addRole(u, r);
        } else {
          for (RolePrincipal ro : roles) {
            if (!Collections.singletonList(ro).contains(r)) {
              getEngine().addRole(u, r);
            }
          }
        }

      }
    }
  }

  @Override
  public void removeRole(String u, String r) throws MeteoriteSecurityException {
    if (getRoles(u).contains(r)) {
      getEngine().deleteRole(u, r);
    } else {
      throw new MeteoriteSecurityException("Role does not exist for user");
    }
  }

  @Override
  public MeteoriteUser updateUser(MeteoriteUser u) throws MeteoriteSecurityException {
    return null;
  }

  @Override
  public boolean isAdmin(String u) throws MeteoriteSecurityException {
    for (Principal p : getEngine().listUsers()) {
      if (p.getName().equals(u)) {
        List<RolePrincipal> roles = getEngine().listRoles(p);
        for (RolePrincipal r : roles) {
          if (getAdminRoles().contains(r.getName())) {
            return true;
          }
        }
      }
    }
    return false;
  }

  @Override
  public List<String> getAdminRoles() throws MeteoriteSecurityException {
    return null;
  }

  @Override
  public MeteoriteUser getUser(String id) throws MeteoriteSecurityException {
    for (org.apache.karaf.jaas.boot.principal.UserPrincipal user : getEngine().listUsers()) {
      if (user.getName().equals(id)) {
        MeteoriteUser u = new User();
        u.setId(id);
        u.setUsername(id);
        return u;
      }
    }
    return null;

  }

  @Override
  public void setBackingEngineService(BackingEngineService jassservice) {
    this.backingEngineService = jassservice;
  }

  @Override
  public void setRealm(JaasRealm realm) {
    this.realm = realm;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }
}

