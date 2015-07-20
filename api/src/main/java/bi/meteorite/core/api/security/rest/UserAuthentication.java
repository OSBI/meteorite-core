package bi.meteorite.core.api.security.rest;

import bi.meteorite.core.api.security.Login;
import bi.meteorite.core.api.security.tokenprovider.TokenProviderException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by bugg on 20/07/15.
 */
@Path("/core/auth/login")
public interface UserAuthentication {

  @POST
  @Consumes("application/json")
  Response login(Login login) throws TokenProviderException;
}
