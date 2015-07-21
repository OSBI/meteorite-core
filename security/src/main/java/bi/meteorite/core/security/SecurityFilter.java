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

package bi.meteorite.core.security;


import bi.meteorite.core.api.security.AdminLoginService;
import bi.meteorite.core.api.security.tokenprovider.TokenProvider;
import bi.meteorite.core.api.security.tokenprovider.TokenProviderException;

import java.io.IOException;
import java.util.SortedMap;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Security Filter
 */
public class SecurityFilter implements Filter {
  private TokenProvider tokenProvider;
  private AdminLoginService adminLoginService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
      ServletException {
    HttpServletRequest servletRequest = (HttpServletRequest) request;
    HttpServletResponse servletResponse = (HttpServletResponse) response;

    if (servletRequest.getPathInfo().startsWith("/admin/ui/") || servletRequest.getPathInfo().equals
      ("/rest/core/auth/login")) {
      chain.doFilter(request, response);
      return;
    } else if (servletRequest.getPathInfo().startsWith("/rest/core/admin")) {
      String token = tokenProvider.getTokenFromRequest(servletRequest);
      boolean isAdmin = false;
      try {
        SortedMap<String, String> userDetails = tokenProvider.verifyToken(token);
        if (adminLoginService.getUsername().equals(userDetails.get(TokenProvider.USERNAME))) {
          isAdmin = true;
        }

      } catch (TokenProviderException e) {
        //The user will be redirect to login below
      }

      if (isAdmin) {
        chain.doFilter(request, response);
      } else {
        servletResponse.sendRedirect("/admin/ui/index.html");
      }
    } else {
      chain.doFilter(request, response);
    }


  }

  @Override
  public void init(FilterConfig arg0) throws ServletException {

  }

  @Override
  public void destroy() {

  }

  public void setTokenProvider(TokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  public void setAdminLoginService(AdminLoginService adminLoginService) {
    this.adminLoginService = adminLoginService;
  }
}
