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

package bi.meteorite.core.security.authentication

import javax.inject.{Named, Singleton}
import javax.ws.rs.container.{ContainerRequestContext, ContainerResponseContext, ContainerResponseFilter}
import javax.ws.rs.core.{HttpHeaders, NewCookie}

import bi.meteorite.core.api.security.tokenprovider.TokenProvider

/**
  * Token Response Filter for JAAS.
  */
@Singleton
@Named("tokenResponseFilter")
class TokenResponseFilter extends ContainerResponseFilter {

  override def filter(requestContext: ContainerRequestContext, responseContext: ContainerResponseContext) {
    val value = requestContext.getProperty("token").asInstanceOf[String]
    if (value != null) {
      val newcookie = new NewCookie(TokenProvider.TOKEN_COOKIE_NAME, value)
      responseContext.getHeaders.putSingle(HttpHeaders.SET_COOKIE, newcookie)
    }
  }
}
