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
package bi.meteorite.core.security.tokenprovider

import javax.servlet.http.HttpServletRequest

import bi.meteorite.core.api.security.tokenprovider.TokenProvider

/**
  * Token Utils
  */
object TokenUtil {
  /**
    * Name of the authorization header when a token is passed in the "Authorization" header of a HTTP request.
    */
  val AUTHORIZATION_HEADER_AMDATU: String = "Amdatu"

  /**
    * Returns the request token associated with the specified request or null of none is associated.
    *
    * @param request The request to get the token from
    * @return the request token associated with the specified request
    */
  def getTokenFromRequest(request: HttpServletRequest): String = {
    if (request.getCookies != null) {
      for (cookie <- request.getCookies) {
        if (TokenProvider.TOKEN_COOKIE_NAME == cookie.getName) {
          return cookie.getValue
        }
      }
    }
    val authHeader: String = request.getHeader("Authorization")
    if (authHeader != null && authHeader.startsWith(AUTHORIZATION_HEADER_AMDATU + " ")) {
      return authHeader.substring(AUTHORIZATION_HEADER_AMDATU.length + 1)
    }
    null
  }
}

final class TokenUtil {
}
