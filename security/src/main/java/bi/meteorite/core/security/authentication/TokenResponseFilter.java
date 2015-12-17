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
    String value = (String) requestContext.getProperty("token");

    if (value != null) {
      NewCookie newcookie = new NewCookie(TokenProvider.TOKEN_COOKIE_NAME, value);

      responseContext.getHeaders().putSingle(HttpHeaders.SET_COOKIE, newcookie);
    }
  }
}
