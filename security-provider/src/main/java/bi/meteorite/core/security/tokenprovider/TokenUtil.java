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
package bi.meteorite.core.security.tokenprovider;

import bi.meteorite.core.api.security.tokenprovider.TokenProvider;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Token Utils
 */
public final class TokenUtil {

  private TokenUtil() {
  }

  /**
   * Name of the authorization header when a token is passed in the "Authorization" header of a HTTP request.
   */
  public static final String AUTHORIZATION_HEADER_AMDATU = "Amdatu";

  /**
   * Returns the request token associated with the specified request or null of none is associated.
   *
   * @param request The request to get the token from
   * @return the request token associated with the specified request
   */
  public static String getTokenFromRequest(final HttpServletRequest request) {
    // Use case 1: The token is sent along in a cookie with the request.
    // The cookie is sent automatically when a request is sent directly from the end user's
    // browser to the Amdatu server.
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if (TokenProvider.TOKEN_COOKIE_NAME.equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    // Use case 2: When requests are not sent from a browser (for example proxied calls)
    // the token can be sent in the Authorization header (like Basic and Digest HTTP authentication).
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith(AUTHORIZATION_HEADER_AMDATU + " ")) {
      return authHeader.substring(AUTHORIZATION_HEADER_AMDATU.length() + 1);
    }
    return null;
  }

}
