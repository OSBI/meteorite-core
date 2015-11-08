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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 * Created by bugg on 20/07/15.
 */

/**
 * Jaas Login Manager
 */
//@OsgiServiceProvider(classes = { AdminLoginService.class })
public class JaasLoginManager implements AdminLoginService {

  private static final Logger logger = LoggerFactory.getLogger(JaasLoginManager.class);
  private String realm;
  private Subject subject;
  private ArrayList<String> roles = new ArrayList<>();
  public static final String ROLES_GROUP_NAME = "ROLES";
  public static final String ROLES_PREFIX = "ROLE_";

  /**
   * Login Callback Handler
   */
  private class LoginCallbackHandler implements CallbackHandler {

    private String username;
    private String password;

    public LoginCallbackHandler(String username, String password) {
      this.username = username;
      this.password = password;

    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
      for (Callback callback : callbacks) {
        if (callback instanceof NameCallback) {
          ((NameCallback) callback).setName(username);
        } else if (callback instanceof PasswordCallback) {
          PasswordCallback pwCallback = (PasswordCallback) callback;
          pwCallback.setPassword(password.toCharArray());
        } else {
          throw new UnsupportedCallbackException(callback, "Callback type not supported");
        }
      }
    }
  }

  public boolean login(String username, String password) {
    boolean authenticated;
    LoginCallbackHandler handler = new LoginCallbackHandler(username, password);
    try {
      LoginContext ctx = new LoginContext(realm, handler);
      ctx.login();
      authenticated = true;
      subject = ctx.getSubject();
      for (Principal p : subject.getPrincipals()) {
        if (p.getName().startsWith(ROLES_PREFIX)) {

          roles.add(p.getName().substring(ROLES_PREFIX.length()));
        }
      }
    } catch (LoginException e) {
      // You'll get a LoginException on a failed username/password combo.
      authenticated = false;
    }
    return authenticated;
  }

  public String getUsername() {
    Set<Principal> principals = subject.getPrincipals();
    System.out.println("size:"+principals.size());
    for(Principal p : principals){
      System.out.println(p.getClass());
      logger.debug("Principal type:"+p.getClass());
      if(p instanceof org.apache.karaf.jaas.boot.principal.UserPrincipal){
        return p.getName();
      }
    }
    return null;
  }

  public void setRealm(String realm) {
    this.realm = realm;
  }
}
