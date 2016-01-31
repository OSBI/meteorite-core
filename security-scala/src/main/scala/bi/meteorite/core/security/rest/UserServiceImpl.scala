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

import javax.inject.{Inject, Named, Singleton}
import javax.ws.rs.core.Response

import bi.meteorite.core.security.rest.objects.UserObj
import bi.meteorite.objects.UserImpl

import scala.collection.JavaConverters._

import bi.meteorite.core.api.objects.MeteoriteUser
import bi.meteorite.core.api.security.IUserManagementProvider
import bi.meteorite.core.api.security.rest.UserService
import org.ops4j.pax.cdi.api.{OsgiService, OsgiServiceProvider}

/**
  * User Service Endpoints.
  */
@OsgiServiceProvider(classes = Array(classOf[UserService]))
@Singleton
@Named("userRestServiceBean")
class UserServiceImpl extends UserService {

  @Inject
  @OsgiService
  private var iUserManagementProvider: IUserManagementProvider = _

  override def addUser(u: MeteoriteUser): Response = {
    iUserManagementProvider.addUser(u.asInstanceOf[UserObj].toUserImpl())
    Response.ok(u).build()
  }

  override def modifyUser(u: MeteoriteUser): Response = Response.ok(iUserManagementProvider.updateUser(u)).build()

  override def deleteUser(u: MeteoriteUser): Response = {
    iUserManagementProvider.deleteUser(u)
    Response.ok().build()
  }

  override def deleteUser(id: Int): Response = {
    iUserManagementProvider.deleteUser(id)
    Response.ok().build()
  }

  override def addRole(id: String, group: Int): Response = Response.serverError().build()

  override def addRole(id: String, group: String): Response = {
    iUserManagementProvider.addRole(id, group)
    Response.ok().build()
  }

  override def getExistingUsers: Response = Response.ok(iUserManagementProvider.getUsers.asJava).build()

  override def getUser(id: Int): Response = Response.ok(iUserManagementProvider.getUser(id)).build()


  override def whoami: Response = Response.ok("{\"login\":{\"password\":\"pass\",\"username\":\"test3\"}}").build()

  def setiUserManagementProvider(iUserManagementProvider: IUserManagementProvider) = this.iUserManagementProvider =
    iUserManagementProvider

}