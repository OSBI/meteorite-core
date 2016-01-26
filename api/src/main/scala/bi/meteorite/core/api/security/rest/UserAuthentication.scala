package bi.meteorite.core.api.security.rest

import bi.meteorite.core.api.security.exceptions.TokenProviderException
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.Response

import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing

/**
  * RESTful interface for authentication.
  */
@CrossOriginResourceSharing(allowAllOrigins = true)
@Path("/auth/login") trait UserAuthentication {
  /**
    * Logout from the Meteorite core.
    *
    * @param username logout username.
    * @return a HTTP response indicating the logout success.
    * @throws TokenProviderException
    */
  @POST
  @throws(classOf[TokenProviderException])
  def logout(username: String): Response
}