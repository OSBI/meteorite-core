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

import bi.meteorite.core.api.objects.{UserList, MeteoriteUser}
import bi.meteorite.core.api.security.exceptions.MeteoriteSecurityException
import org.apache.karaf.jaas.config.JaasRealm
import org.apache.karaf.jaas.modules.BackingEngineService

/**
  * User Management Provider.
  * API for the main user managaement interface for Meteorite Core.
  */
trait IUserManagementProvider {
  /**
    * Add a new user to the platform.
    *
    * @param u MeteoriteUser
    * @throws MeteoriteSecurityException Throws Meteorite Security Exception on error
    */
  @throws(classOf[MeteoriteSecurityException])
  def addUser(u: MeteoriteUser)

  /**
    * Delete a user from the platform.
    *
    * @param u MeteoriteUser
    * @throws MeteoriteSecurityException Throws Meteorite Security Exception on error
    */
  @throws(classOf[MeteoriteSecurityException])
  def deleteUser(u: MeteoriteUser)

  /**
    * Delete a user from the platform.
    *
    * @param u Id
    * @throws MeteoriteSecurityException Throws Meteorite Security Exception on error
    */
  @throws(classOf[MeteoriteSecurityException])
  def deleteUser(u: Long)

  /**
    * Get a list of users on the platform.
    *
    * @return a list of usernames.
    * @throws MeteoriteSecurityException Throws Meteorite Security Exception on error
    */
  @throws(classOf[MeteoriteSecurityException])
  def getUsers: List[UserList]

  /**
    * Get a list of user ids
    * @throws MeteoriteSecurityException Throws Meteorite Security Exception on error
    * @return a list of user ids
    */
  @throws(classOf[MeteoriteSecurityException])
  def getUsersId: List[Long]

  /**
    * Get a list of roles applied to a user.
    *
    * @param u username
    * @return a list of roles.
    * @throws MeteoriteSecurityException Throws Meteorite Security Exception on error
    */
  @throws(classOf[MeteoriteSecurityException])
  def getRoles(u: String): List[String]

  /**
    * Add a role to a user.
    *
    * @param u username
    * @param r role name
    * @throws MeteoriteSecurityException Throws Meteorite Security Exception on error
    */
  @throws(classOf[MeteoriteSecurityException])
  def addRole(u: String, r: String)

  /**
    * Remove role from a user
    *
    * @param u username
    * @param r role
    * @throws MeteoriteSecurityException Throws Meteorite Security Exception on error
    */
  @throws(classOf[MeteoriteSecurityException])
  def removeRole(u: String, r: String)

  /**
    * Update a user on the platform.
    *
    * @param u user object
    * @return a updated user object
    * @throws MeteoriteSecurityException Throws Meteorite Security Exception on error
    */
  @throws(classOf[MeteoriteSecurityException])
  def updateUser(u: MeteoriteUser): MeteoriteUser

  /**
    * Discover is the user is an administrator or not.
    *
    * @param u username
    * @return true if the user is admin.
    * @throws MeteoriteSecurityException Throws Meteorite Security Exception on error
    */
  @throws(classOf[MeteoriteSecurityException])
  def isAdmin(u: String): Boolean

  /**
    * Get a list of roles that define an administrator.
    *
    * @return a list of administrative roles
    * @throws MeteoriteSecurityException Throws Meteorite Security Exception on error
    */
  @throws(classOf[MeteoriteSecurityException])
  def getAdminRoles: List[String]

  /**
    * Get a single user.
    *
    * @return a Meteorite User.
    * @throws MeteoriteSecurityException Throws Meteorite Security Exception on error
    */
  @throws(classOf[MeteoriteSecurityException])
  def getUser(id: Long): MeteoriteUser

  /**
    * Set the backing service engine that drives the security.
    *
    * @param backingEngineService the backing engine service.
    */
  def setBackingEngineService(backingEngineService: BackingEngineService)

  /**
    * Set the JaaS realm.
    *
    * @param realm the JAAS Realm
    */
  def setRealm(realm: JaasRealm)
}

