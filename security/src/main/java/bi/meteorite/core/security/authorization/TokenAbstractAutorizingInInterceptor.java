package bi.meteorite.core.security.authorization;

import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.interceptor.Fault;
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
public abstract class TokenAbstractAutorizingInInterceptor extends AbstractAuthorizingInInterceptor  {

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
  public void handleMessage(Message message) throws Fault {
    Method method = getTargetMethod(message);
    SecurityContext sc = message.get(SecurityContext.class);
    if(sc == null){
      org.apache.cxf.security.SecurityContext sc2 = message.get(org.apache.cxf.security.SecurityContext.class);
      if (authorize(sc2, method)) {
        return;
      }
    }
    else if (sc != null && sc.getUserPrincipal() != null) {
      if (authorize(sc, method)) {
        return;
      }
    } else if (!isMethodProtected(method) && isAllowAnonymousUsers()) {
      return;
    }


    throw new AccessDeniedException("Unauthorized");
  }

  protected boolean authorize(SecurityContext sc, Method method) {
    List<String> expectedRoles = getExpectedRoles(method);
    if (expectedRoles.isEmpty()) {

      List<String> denyRoles = getDenyRoles(method);

      return denyRoles.isEmpty() ? true : isUserInRole(sc, denyRoles, true);
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
