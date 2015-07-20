package bi.meteorite.core.security.rest;

import bi.meteorite.core.api.security.AdminLoginService;
import bi.meteorite.core.api.security.Login;
import bi.meteorite.core.api.security.rest.UserAuthentication;
import bi.meteorite.core.api.security.rest.UserService;
import bi.meteorite.core.api.security.tokenprovider.TokenProvider;
import bi.meteorite.core.api.security.tokenprovider.TokenProviderException;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;

import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Singleton;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

/**
 * Created by bugg on 20/07/15.
 */
@OsgiServiceProvider(classes = { UserAuthentication.class })
@Singleton
public class UserAuthenticationImpl implements UserAuthentication {

  private volatile AdminLoginService adminLoginService;
  private volatile TokenProvider tokenProvider;


  @Override
  public Response login(Login login) throws TokenProviderException {
    if(adminLoginService.login(login.getUsername(), login.getPassword())) {
      SortedMap<String, String> userMap = new TreeMap<>();
      userMap.put(TokenProvider.USERNAME, "admin");

      String token = tokenProvider.generateToken(userMap);

      return Response.ok().cookie(new NewCookie(TokenProvider.TOKEN_COOKIE_NAME, token)).build();
    }

    return Response.status(403).build();
  }

  public void setAdminLoginService(AdminLoginService adminLoginService) {
    this.adminLoginService = adminLoginService;
  }

  public void setTokenProvider(TokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }
}
