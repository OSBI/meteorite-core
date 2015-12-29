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

package bi.meteorite.core.security.rest;

import bi.meteorite.core.api.security.AdminLoginService;
import bi.meteorite.core.api.security.exceptions.TokenProviderException;
import bi.meteorite.core.api.security.rest.UserAuthentication;

import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;

/**
 * User Authentication Implementation.
 */
@OsgiServiceProvider(classes = { UserAuthentication.class })
@Singleton
public class UserAuthenticationImpl implements UserAuthentication {

  @Inject
  @OsgiService
  private volatile AdminLoginService adminLoginService;

  @Override
  public Response logout(String username) throws TokenProviderException {
    if (adminLoginService.logout(username)) {
      return Response.ok().build();
    } else {
      return Response.serverError().build();
    }
  }


  public void setAdminLoginService(AdminLoginService adminLoginService) {
    this.adminLoginService = adminLoginService;
  }

}
