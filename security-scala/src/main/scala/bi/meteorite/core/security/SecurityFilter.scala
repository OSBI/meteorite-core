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

package bi.meteorite.core.security

import javax.inject.{Inject, Named, Singleton}
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import javax.servlet.{Filter, FilterChain, FilterConfig, ServletRequest, ServletResponse}

import bi.meteorite.core.api.security.AdminLoginService
import bi.meteorite.core.api.security.exceptions.TokenProviderException
import bi.meteorite.core.api.security.tokenprovider.TokenProvider
import org.ops4j.pax.cdi.api.OsgiService

/**
  * Extended Security Filter for Token Authentication.
  */
@Singleton
@Named("securityFilter")
class SecurityFilter extends Filter {

  @Inject
  @OsgiService
  private var tokenProvider: TokenProvider = _

  @Inject
  @OsgiService
  private var adminLoginService: AdminLoginService = _

  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
    val servletRequest = request.asInstanceOf[HttpServletRequest]
    val servletResponse = response.asInstanceOf[HttpServletResponse]
    var s = servletRequest.getPathInfo
    val s2 = servletRequest.getServletPath
    if (s != null && s2 != null) {
      s = s2 + s
    } else if (s == null) {
      s = s2
    }
    if (s.startsWith("/admin/ui/") || s == "/rest/core/auth/login") {
      chain.doFilter(request, response)
    } else if (servletRequest.getPathInfo.startsWith("/rest/core/admin")) {
      val token = tokenProvider.getTokenFromRequest(servletRequest)
      var isAdmin = false
      try {
        val userDetails = tokenProvider.verifyToken(token)

        if (adminLoginService.getUsername ==
          userDetails.get(TokenProvider.USERNAME).get) {
          isAdmin = true
        }
      } catch {
        case e: TokenProviderException =>
      }
      if (isAdmin) {
        chain.doFilter(request, response)
      } else {
        servletResponse.sendRedirect("/admin/ui/index.html")
      }
    } else {
      chain.doFilter(request, response)
    }
  }

  override def init(arg0: FilterConfig) {
  }

  override def destroy() {
  }

  def setTokenProvider(tokenProvider: TokenProvider) = this.tokenProvider = tokenProvider

  def setAdminLoginService(adminLoginService: AdminLoginService) = this.adminLoginService = adminLoginService
}