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

package bi.meteorite.core.api.security

import bi.meteorite.core.api.objects.MeteoriteUser
import bi.meteorite.core.api.security.exceptions.MeteoriteSecurityException

/**
  * User Management Interface.
  */
trait IUserManagement {
  @throws(classOf[MeteoriteSecurityException])
  def addUser(u: MeteoriteUser): MeteoriteUser

  @throws(classOf[MeteoriteSecurityException])
  def deleteUser(u: MeteoriteUser): Boolean

  @throws(classOf[MeteoriteSecurityException])
  def setUser(u: MeteoriteUser): MeteoriteUser

  @throws(classOf[MeteoriteSecurityException])
  def getUser(id: Int): MeteoriteUser

  @throws(classOf[MeteoriteSecurityException])
  def getRoles(u: MeteoriteUser): Array[String]

  @throws(classOf[MeteoriteSecurityException])
  def addRole(u: MeteoriteUser)

  @throws(classOf[MeteoriteSecurityException])
  def removeRole(u: MeteoriteUser)

  @throws(classOf[MeteoriteSecurityException])
  def removeUser(username: String)

  @throws(classOf[MeteoriteSecurityException])
  def updateUser(u: MeteoriteUser): MeteoriteUser

  @throws(classOf[MeteoriteSecurityException])
  def isAdmin: Boolean

  @throws(classOf[MeteoriteSecurityException])
  def getAdminRoles: List[String]
}

