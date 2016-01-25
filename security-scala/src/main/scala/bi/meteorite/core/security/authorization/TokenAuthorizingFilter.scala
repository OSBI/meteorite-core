package bi.meteorite.core.security.authorization

import javax.annotation.Priority
import javax.inject.{Inject, Named, Singleton}
import javax.ws.rs.Priorities
import javax.ws.rs.container.{ContainerRequestContext, ContainerRequestFilter}
import javax.ws.rs.core.Response

import org.apache.cxf.interceptor.security.AccessDeniedException
import org.apache.cxf.jaxrs.utils.JAXRSUtils

@Priority(Priorities.AUTHORIZATION)
@Singleton
@Named("authorizationFilter") class TokenAuthorizingFilter extends ContainerRequestFilter {

  @Inject
  @Named("authorizationInterceptor")
  private var interceptor: TokenAuthorizingInterceptor = null

  def filter(context: ContainerRequestContext) = {
    try {
      interceptor.handleMessage(JAXRSUtils.getCurrentMessage)
    }
    catch {
      case _: AccessDeniedException => context.abortWith(Response.status(Response.Status.FORBIDDEN).build)
    }

  }

  def setInterceptor(in: TokenAuthorizingInterceptor) = interceptor = in
}
