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

package bi.meteorite.core.security.authorization

import javax.annotation.Priority
import javax.inject.{Inject, Named, Singleton}
import javax.ws.rs.Priorities
import javax.ws.rs.container.{ContainerRequestContext, ContainerRequestFilter}
import javax.ws.rs.core.Response

import org.apache.cxf.interceptor.security.AccessDeniedException
import org.apache.cxf.jaxrs.utils.JAXRSUtils

/**
  * Authorizing filter for Token Authentication.
  */
@Priority(Priorities.AUTHORIZATION)
@Singleton
@Named("authorizationFilter")
class TokenAuthorizingFilter extends ContainerRequestFilter {

  @Inject
  @Named("authorizationInterceptor")
  private var interceptor: TokenAuthorizingInterceptor = null

  def filter(context: ContainerRequestContext) = {
    try {
      interceptor.handleMessage(JAXRSUtils.getCurrentMessage)
    }
    catch {
      case _: AccessDeniedException => context.abortWith(Response.status(Response.Status.FORBIDDEN).build)
    }

  }

  def setInterceptor(in: TokenAuthorizingInterceptor) = interceptor = in
}
