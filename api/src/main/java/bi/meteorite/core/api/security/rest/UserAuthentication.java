/*
 * Copyright 2015 OSBI Ltd
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

package bi.meteorite.core.api.security.rest;

import bi.meteorite.core.api.security.rest.objects.Login;
import bi.meteorite.core.api.security.exceptions.TokenProviderException;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * RESTful interface for authentication.
 */
@Path("/auth/login")
public interface UserAuthentication {

  /**
   * Login to the Meteorite core.
   * @param login Login Object
   * @return an HTTP response.
   * @throws TokenProviderException
   */
  @POST
  @Consumes("application/json")
  Response login(Login login) throws TokenProviderException;

  /**
   * Demo, needs removing in production.
   * @return
   */
  @GET
  @Produces("application/json")
  Response login();
}
