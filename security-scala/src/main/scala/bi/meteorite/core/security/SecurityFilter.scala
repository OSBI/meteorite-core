package bi.meteorite.core.security

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import javax.servlet.{Filter, FilterChain, FilterConfig, ServletRequest, ServletResponse}

import bi.meteorite.core.api.security.AdminLoginService
import bi.meteorite.core.api.security.exceptions.TokenProviderException
import bi.meteorite.core.api.security.tokenprovider.TokenProvider

class SecurityFilter extends Filter {

  private var tokenProvider: TokenProvider = _

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

  def setTokenProvider(tokenProvider: TokenProvider) {
    this.tokenProvider = tokenProvider
  }

  def setAdminLoginService(adminLoginService: AdminLoginService) {
    this.adminLoginService = adminLoginService
  }
}