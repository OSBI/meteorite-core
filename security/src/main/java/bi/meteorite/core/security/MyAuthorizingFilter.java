package bi.meteorite.core.security;

import org.apache.cxf.interceptor.security.AccessDeniedException;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

/**
 * Created by bugg on 16/12/15.
 */
@Priority(Priorities.AUTHORIZATION)
public class MyAuthorizingFilter implements ContainerRequestFilter {

  private MyAuthorizingInterceptor interceptor;

  public void filter(ContainerRequestContext context) {
    try {
      interceptor.handleMessage(JAXRSUtils.getCurrentMessage());
    } catch (AccessDeniedException ex) {
      context.abortWith(Response.status(Response.Status.FORBIDDEN).build());
    }
  }

  public void setInterceptor(MyAuthorizingInterceptor in) {
    interceptor = in;
  }
}
