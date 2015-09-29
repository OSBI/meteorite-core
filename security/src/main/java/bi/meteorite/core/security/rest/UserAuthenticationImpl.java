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
import bi.meteorite.core.api.security.rest.objects.Login;
import bi.meteorite.core.api.security.rest.UserAuthentication;
import bi.meteorite.core.api.security.tokenprovider.TokenProvider;
import bi.meteorite.core.api.security.exceptions.TokenProviderException;

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
    if (adminLoginService.login(login.getUsername(), login.getPassword())) {
      SortedMap<String, String> userMap = new TreeMap<>();
      userMap.put(TokenProvider.USERNAME, "admin");

      String token = tokenProvider.generateToken(userMap);

      return Response.ok().cookie(new NewCookie(TokenProvider.TOKEN_COOKIE_NAME, token)).build();
    }

    return Response.status(403).build();
  }

  @Override
  public Response login() {
    Login l = new Login();
    l.setUsername("test");
    l.setPassword("pass");
    return Response.ok().entity(l).build();

  }

  public void setAdminLoginService(AdminLoginService adminLoginService) {
    this.adminLoginService = adminLoginService;
  }

  public void setTokenProvider(TokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }
}
