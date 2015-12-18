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

package bi.meteorite.core.security.authorization;

import org.apache.cxf.common.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.SecurityContext;

/**
 * Token Based Authorizing Interceptor.
 */
public class TokenAuthorizingInterceptor extends TokenAbstractAutorizingInInterceptor {

  private final Map<String, List<String>> methodRolesMap = new HashMap<>();
  private Map<String, List<String>> userRolesMap = Collections.emptyMap();
  private List<String> globalRoles = Collections.emptyList();
  private boolean checkConfiguredRolesOnly;

  public TokenAuthorizingInterceptor() {
    this(true);
  }

  public TokenAuthorizingInterceptor(boolean uniqueId) {
    super(uniqueId);
  }

  @Override
  protected boolean isUserInRole(SecurityContext sc, List<String> roles, boolean deny) {
    if (!checkConfiguredRolesOnly && !super.isUserInRole(sc, roles, deny)) {
      return false;
    }
    // Additional check.
    if (!userRolesMap.isEmpty()) {
      List<String> userRoles = userRolesMap.get(sc.getUserPrincipal().getName());
      if (userRoles == null) {
        return false;
      }
      for (String role : roles) {
        if (userRoles.contains(role)) {
          return true;
        }
      }
      return false;
    } else {
      return !checkConfiguredRolesOnly;
    }
  }

  private String createMethodSig(Method method) {
    StringBuilder b = new StringBuilder(method.getReturnType().getName());
    b.append(' ').append(method.getName()).append('(');
    for (Class<?> cls : method.getParameterTypes()) {
      b.append(cls.getName());
    }
    b.append(')');
    return b.toString();
  }

  @Override
  protected List<String> getExpectedRoles(Method method) {
    List<String> roles = methodRolesMap.get(createMethodSig(method));
    if (roles == null) {
      roles = methodRolesMap.get(method.getName());
    }
    if (roles != null) {
      return roles;
    }
    return globalRoles;
  }


  public void setMethodRolesMap(Map<String, String> rolesMap) {
    methodRolesMap.putAll(parseRolesMap(rolesMap));
  }

  public void setUserRolesMap(Map<String, String> rolesMap) {
    userRolesMap = parseRolesMap(rolesMap);
  }

  public void setGlobalRoles(String roles) {
    globalRoles = Arrays.asList(StringUtils.split(roles, " "));
  }

  public void setCheckConfiguredRolesOnly(boolean checkConfiguredRolesOnly) {
    this.checkConfiguredRolesOnly = checkConfiguredRolesOnly;
  }

  private static Map<String, List<String>> parseRolesMap(Map<String, String> rolesMap) {
    Map<String, List<String>> map = new HashMap<>();
    for (Map.Entry<String, String> entry : rolesMap.entrySet()) {
      map.put(entry.getKey(), Arrays.asList(StringUtils.split(entry.getValue(), " ")));
    }
    return map;
  }
}
