/*
 * Copyright 2016 OSBI Ltd
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