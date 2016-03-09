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
package bi.meteorite.core.security.rest

import javax.inject.{Inject, Singleton}
import javax.ws.rs.core.Response

import bi.meteorite.core.api.security.AdminLoginService
import bi.meteorite.core.api.security.rest.{UserAuthentication, UserService}
import org.ops4j.pax.cdi.api.{OsgiService, OsgiServiceProvider}

/**
  * User Authentication endpoints.
  */
@OsgiServiceProvider(classes = Array(classOf[UserAuthentication]))
@Singleton class UserAuthenticationImpl extends UserAuthentication {

  @Inject
  @volatile
  @OsgiService
  private var adminLoginService: AdminLoginService = null

  override def logout(companyId: Long, username: String) : Response = {
    if(adminLoginService.logout(companyId, username)){
      Response.ok().build()
    }
    else{
      Response.serverError().build()
    }
  }

  def setAdminLoginService(adminLoginService: AdminLoginService) = this.adminLoginService = adminLoginService
}

