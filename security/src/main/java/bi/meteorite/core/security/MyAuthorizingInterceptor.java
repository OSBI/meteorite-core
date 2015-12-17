package bi.meteorite.core.security;

import org.apache.cxf.common.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.SecurityContext;

/**
 * Created by bugg on 17/12/15.
 */
public class MyAuthorizingInterceptor extends MyAbstractAutorizingInInterceptor {

  protected Map<String, List<String>> methodRolesMap = new HashMap<String, List<String>>();
  protected Map<String, List<String>> userRolesMap = Collections.emptyMap();
  protected List<String> globalRoles = Collections.emptyList();
  private boolean checkConfiguredRolesOnly;

  public MyAuthorizingInterceptor() {
    this(true);
  }
  public MyAuthorizingInterceptor(boolean uniqueId) {
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

  protected String createMethodSig(Method method) {
    StringBuilder b = new StringBuilder(method.getReturnType().getName());
    b.append(' ').append(method.getName()).append('(');
    boolean first = true;
    for (Class<?> cls : method.getParameterTypes()) {
      if (!first) {
        b.append(", ");
        first = false;
      }
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
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    for (Map.Entry<String, String> entry : rolesMap.entrySet()) {
      map.put(entry.getKey(), Arrays.asList(StringUtils.split(entry.getValue(), " ")));
    }
    return map;
  }
}