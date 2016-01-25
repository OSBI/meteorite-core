package bi.meteorite.core.security.authentication

import javax.inject.{Named, Singleton}
import javax.ws.rs.container.{ContainerRequestContext, ContainerResponseContext, ContainerResponseFilter}
import javax.ws.rs.core.{HttpHeaders, NewCookie}

import bi.meteorite.core.api.security.tokenprovider.TokenProvider

@Singleton
@Named("tokenResponseFilter")
class TokenResponseFilter extends ContainerResponseFilter {

  override def filter(requestContext: ContainerRequestContext, responseContext: ContainerResponseContext) {
    val value = requestContext.getProperty("token").asInstanceOf[String]
    if (value != null) {
      val newcookie = new NewCookie(TokenProvider.TOKEN_COOKIE_NAME, value)
      responseContext.getHeaders.putSingle(HttpHeaders.SET_COOKIE, newcookie)
    }
  }
}
