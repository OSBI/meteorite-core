package bi.meteorite.core.security;

import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.security.AccessDeniedException;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.invoker.MethodDispatcher;
import org.apache.cxf.service.model.BindingOperationInfo;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.SecurityContext;

/**
 * Created by bugg on 17/12/15.
 */
public abstract class MyAbstractAutorizingInInterceptor extends AbstractPhaseInterceptor<Message> {

  private static final Logger LOG = LogUtils.getL7dLogger(MyAbstractAutorizingInInterceptor.class);
  private static final String ALL_ROLES = "*";
  private boolean allowAnonymousUsers = true;

  public MyAbstractAutorizingInInterceptor() {
    this(true);
  }
  public MyAbstractAutorizingInInterceptor(boolean uniqueId) {
    super(null, Phase.PRE_INVOKE, uniqueId);
  }
  public void handleMessage(Message message) throws Fault {
    Method method = getTargetMethod(message);
    SecurityContext sc = message.get(SecurityContext.class);
    if (sc != null && sc.getUserPrincipal() != null) {
      if (authorize(sc, method)) {
        return;
      }
    } else if (!isMethodProtected(method) && isAllowAnonymousUsers()) {
      return;
    }


    throw new AccessDeniedException("Unauthorized");
  }

  protected Method getTargetMethod(Message m) {
    BindingOperationInfo bop = m.getExchange().getBindingOperationInfo();
    if (bop != null) {
      MethodDispatcher md = (MethodDispatcher)
          m.getExchange().getService().get(MethodDispatcher.class.getName());
      return md.getMethod(bop);
    }
    Method method = (Method)m.get("org.apache.cxf.resource.method");
    if (method != null) {
      return method;
    }
    throw new AccessDeniedException("Method is not available : Unauthorized");
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
  protected boolean isMethodProtected(Method method) {
    return !getExpectedRoles(method).isEmpty() || !getDenyRoles(method).isEmpty();
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

  /**
   * Returns a list of expected roles for a given method.
   * @param method Method
   * @return list, empty if no roles are available
   */
  protected abstract List<String> getExpectedRoles(Method method);


  /**
   * Returns a list of roles to be denied for a given method.
   * @param method Method
   * @return list, empty if no roles are available
   */
  protected List<String> getDenyRoles(Method method) {
    return Collections.emptyList();
  }

  public boolean isAllowAnonymousUsers() {
    return allowAnonymousUsers;
  }

  public void setAllowAnonymousUsers(boolean allowAnonymousUsers) {
    this.allowAnonymousUsers = allowAnonymousUsers;
  }

}
