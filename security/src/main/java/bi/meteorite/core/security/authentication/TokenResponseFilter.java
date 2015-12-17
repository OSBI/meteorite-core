package bi.meteorite.core.security.authentication;

import bi.meteorite.core.api.security.tokenprovider.TokenProvider;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.NewCookie;

/**
 * Created by bugg on 16/12/15.
 */
public class TokenResponseFilter implements ContainerResponseFilter {


  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
      throws IOException {
    String value = (String)requestContext.getProperty("token");

    if(value!=null) {
      NewCookie newcookie = new NewCookie(TokenProvider.TOKEN_COOKIE_NAME, value);

      responseContext.getHeaders().putSingle(HttpHeaders.SET_COOKIE, newcookie);
    }
  }
}
