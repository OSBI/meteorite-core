package bi.meteorite.core.security.jaas;

import bi.meteorite.core.api.objects.MeteoriteUser;
import bi.meteorite.core.api.security.IUserManagementProvider;
import bi.meteorite.core.api.security.exceptions.MeteoriteSecurityException;

import org.apache.karaf.jaas.boot.ProxyLoginModule;
import org.apache.karaf.jaas.boot.principal.RolePrincipal;
import org.apache.karaf.jaas.config.JaasRealm;
import org.apache.karaf.jaas.modules.BackingEngine;
import org.apache.karaf.jaas.modules.BackingEngineFactory;
import org.apache.karaf.jaas.modules.BackingEngineService;

import com.google.common.collect.ImmutableList;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.AppConfigurationEntry;


/**
 * Default JaaS User Manager to control users in Karaf.
 */
public class JaasUserManager implements IUserManagementProvider {

  private BackingEngineService backingEngineService;
  private JaasRealm realm;


  private BackingEngine getEngine(){
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
    if(getUsers().contains(u)){
      throw new MeteoriteSecurityException("User already exists");
    }
    getEngine().addUser(u, p);
  }

  @Override
  public void deleteUser(String u) throws MeteoriteSecurityException {
    if(getUsers().contains(u)) {
      getEngine().deleteUser(u);
    }
    else{
      throw new MeteoriteSecurityException("User Doesn't Exist");
    }
  }

  @Override
  public List<String> getUsers() throws MeteoriteSecurityException {
    List<String> users = new ArrayList<>();
    for(org.apache.karaf.jaas.boot.principal.UserPrincipal user: getEngine().listUsers()){
      users.add(user.getName());
    }
    return users;
  }

  @Override
  public List<String> getRoles(String u) throws MeteoriteSecurityException {
    List<String> s = new ArrayList<>();
    List u2 = getUsers();

    if(u2.contains(u)) {
      for (Principal p : getEngine().listUsers()) {
        if (p.getName().equals(u)) {
          for (RolePrincipal r : getEngine().listRoles(p)) {
            s.add(r.getName());
          }

        }
      }
      return ImmutableList.copyOf(s);
    }
    else{
      throw new MeteoriteSecurityException("User does not exist");
    }

  }

  @Override
  public void addRole(String u, String r) throws MeteoriteSecurityException {
    for(Principal p :getEngine().listUsers()){
      if(p.getName().equals(u)){
        List<RolePrincipal> roles = getEngine().listRoles(p);
        if(roles.size()==0){
          getEngine().addRole(u, r);
        }
        else {
          for (RolePrincipal ro : roles) {
            if (!Arrays.asList(ro).contains(r)) {
              getEngine().addRole(u, r);
            }
          }
        }

      }
    }
  }

  @Override
  public void removeRole(String u, String r) throws MeteoriteSecurityException {
    if(getRoles(u).contains(r)) {
      getEngine().deleteRole(u, r);
    }
    else{
      throw new MeteoriteSecurityException("Role does not exist for user");
    }
  }

  @Override
  public MeteoriteUser updateUser(MeteoriteUser u) throws MeteoriteSecurityException {
    return null;
  }

  @Override
  public boolean isAdmin(String u) throws MeteoriteSecurityException {
    for(Principal p :getEngine().listUsers()) {
      if (p.getName().equals(u)) {
        List<RolePrincipal> roles = getEngine().listRoles(p);
        for(RolePrincipal r : roles){
          if(getAdminRoles().contains(r.getName())){
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
  public void setBackingEngineService(BackingEngineService jassservice) {
    this.backingEngineService = jassservice;
  }

  @Override
  public void setRealm(JaasRealm realm){
    this.realm = realm;
  }
}

