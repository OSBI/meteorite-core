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

import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.interceptor.security.AbstractAuthorizingInInterceptor;
import org.apache.cxf.interceptor.security.AccessDeniedException;
import org.apache.cxf.message.Message;

import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.SecurityContext;

/**
 * Token Authorizing Interceptor. Altered to accept a JAX RS security context.
 */
public abstract class TokenAbstractAutorizingInInterceptor extends AbstractAuthorizingInInterceptor {

  private static final Logger LOG = LogUtils.getL7dLogger(TokenAbstractAutorizingInInterceptor.class);
  private static final String ALL_ROLES = "*";
  private boolean allowAnonymousUsers = true;

  public TokenAbstractAutorizingInInterceptor() {
    this(true);
  }

  public TokenAbstractAutorizingInInterceptor(boolean uniqueId) {
    super(uniqueId);
  }

  @Override
  public void handleMessage(Message message) {
    Method method = getTargetMethod(message);
    SecurityContext sc = message.get(SecurityContext.class);
    if (sc == null) {
      org.apache.cxf.security.SecurityContext sc2 = message.get(org.apache.cxf.security.SecurityContext.class);
      if (authorize(sc2, method)) {
        return;
      }
    } else if (sc.getUserPrincipal() != null) {
      if (authorize(sc, method)) {
        return;
      }
    } else if (!isMethodProtected(method) && isAllowAnonymousUsers()) {
      return;
    }


    throw new AccessDeniedException("Unauthorized");
  }

  private boolean authorize(SecurityContext sc, Method method) {
    List<String> expectedRoles = getExpectedRoles(method);
    if (expectedRoles.isEmpty()) {

      List<String> denyRoles = getDenyRoles(method);

      return denyRoles.isEmpty() || isUserInRole(sc, denyRoles, true);
    }

    if (isUserInRole(sc, expectedRoles, false)) {
      return true;
    }
    if (LOG.isLoggable(Level.FINE)) {
      LOG.fine(sc.getUserPrincipal().getName() + " is not authorized");
    }
    return false;
  }

  protected boolean isUserInRole(SecurityContext sc, List<String> roles, boolean deny) {

    if (roles.size() == 1 && ALL_ROLES.equals(roles.get(0))) {
      return !deny;
    }

    for (String role : roles) {
      if (sc.isUserInRole(role)) {
        return !deny;
      }
    }
    return deny;
  }


}
