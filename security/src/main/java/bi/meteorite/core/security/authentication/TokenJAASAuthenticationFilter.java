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

package bi.meteorite.core.security.authentication;


import bi.meteorite.core.api.security.exceptions.TokenProviderException;
import bi.meteorite.core.api.security.tokenprovider.TokenProvider;

import org.apache.cxf.interceptor.security.JAASLoginInterceptor;
import org.apache.cxf.interceptor.security.callback.CallbackHandlerProvider;
import org.apache.cxf.interceptor.security.callback.CallbackHandlerProviderAuthPol;
import org.apache.cxf.interceptor.security.callback.CallbackHandlerProviderUsernameToken;
import org.apache.cxf.jaxrs.security.JAASAuthenticationFilter;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;
import org.apache.karaf.jaas.boot.principal.RolePrincipal;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Priority;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.SecurityContext;

/**
 * Extend the JAASAuthenticationFilter to support token discovery, creation and validation.
 */
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class TokenJAASAuthenticationFilter extends JAASAuthenticationFilter {

  private final ArrayList<CallbackHandlerProvider> callbackHandlerProviders;

  private JAASLoginInterceptor interceptor;
  private TokenProvider tokenProvider;
  private SecurityContext oldcontext;

  public TokenJAASAuthenticationFilter() {
    this.callbackHandlerProviders = new ArrayList<>();
    this.callbackHandlerProviders.add(new CallbackHandlerProviderAuthPol());
    this.callbackHandlerProviders.add(new CallbackHandlerProviderUsernameToken());

    interceptor = new JAASLoginInterceptor() {
      protected CallbackHandler getCallbackHandler(String name, String password) {
        return TokenJAASAuthenticationFilter.this.getCallbackHandler(name, password);
      }
    };
    interceptor.setUseDoAs(false);
    interceptor.setContextName("karaf");
  }

  @Override
  public void filter(ContainerRequestContext context) {
    Message m = JAXRSUtils.getCurrentMessage();
    try {
      SortedMap<String, String> valid = null;
      Map<String, Cookie> cookies = context.getCookies();
      if (cookies.containsKey(TokenProvider.TOKEN_COOKIE_NAME)) {
        try {
          Cookie cookie = cookies.get(TokenProvider.TOKEN_COOKIE_NAME);
          valid = tokenProvider.verifyToken(cookie.getValue());
          final SortedMap<String, String> finalValid = valid;
          SecurityContext c = new SecurityContext() {
            java.security.Principal p = new UserPrincipal(finalValid.get(TokenProvider.USERNAME));

            @Override
            public Principal getUserPrincipal() {
              return p;
            }

            @Override
            public boolean isUserInRole(String role) {
              String roles = finalValid.get(TokenProvider.ROLES);
              String[] rolearray = roles.split(",");
              return Arrays.asList(rolearray).contains(role);
            }

            @Override
            public boolean isSecure() {
              return false;
            }

            @Override
            public String getAuthenticationScheme() {
              return null;
            }
          };

          oldcontext = context.getSecurityContext();
          context.setSecurityContext(c);
        } catch (TokenProviderException e) {
          //e.printStackTrace();
          //Token auth failed replace with old context for PW login.
          context.setSecurityContext(oldcontext);

        }
      }
      if (valid == null || valid.size() == 0) {

        CallbackHandler handler = getFirstCallbackHandler(m);


        interceptor.handleMessage(m);


        AccessControlContext acc = AccessController.getContext();
        Subject subject = Subject.getSubject(acc);
        Set<Principal> principals = subject.getPrincipals();

        String s = "";
        for (Principal role : principals) {
          if (role instanceof RolePrincipal) {
            s += role.getName() + ",";
          }

        }
        s = s.substring(0, s.length() - 1);

        SortedMap<String, String> userMap = new TreeMap<>();
        userMap.put(TokenProvider.USERNAME, getUsername(handler));
        userMap.put(TokenProvider.ROLES, s);
        try {
          String token = tokenProvider.generateToken(userMap);
          context.setProperty("token", token);
        } catch (TokenProviderException e) {
          e.printStackTrace();
        }
      }

    } catch (SecurityException ex) {
      context.abortWith(handleAuthenticationException(ex, m));
    }


  }

  private String getUsername(CallbackHandler handler) {
    if (handler == null) {
      return null;
    }
    try {
      NameCallback usernameCallBack = new NameCallback("user");
      handler.handle(new Callback[] { usernameCallBack });
      return usernameCallBack.getName();
    } catch (Exception e) {
      return null;
    }
  }

  private CallbackHandler getFirstCallbackHandler(Message message) {
    for (CallbackHandlerProvider cbp : callbackHandlerProviders) {
      CallbackHandler cbh = cbp.create(message);
      if (cbh != null) {
        return cbh;
      }
    }
    return null;
  }

  public void setTokenProvider(TokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  /**
   * A Core User Principal.
   */
  public class UserPrincipal implements Principal {

    private String name;

    @Override
    public String getName() {
      return name;
    }

    public UserPrincipal(String name) {
      this.name = name;
    }

  }


}
