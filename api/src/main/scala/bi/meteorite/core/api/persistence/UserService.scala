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

package bi.meteorite.core.api.persistence

import bi.meteorite.core.api.objects.MeteoriteUser
import bi.meteorite.core.api.objects.MeteoriteRole

/**
  * Interface for persisting users.
  */
trait UserService {
  /**
    * Update an existing user.
    * @param u the updated user object
    * @return The merged user.
    */
  def mergeUser(u: MeteoriteUser) : MeteoriteUser

  /**
    * Get a user by ID.
    * @param id The ID of the user object.
    * @return The MeteoriteUser.
    */
  def getUser(id: String): MeteoriteUser

  /**
    * Get user by ID.
    * @param id The Id of the user object.
    * @return The MeteoriteUser.
    */
  def getUser(id: Long): MeteoriteUser

  /**
    * Add a new MeteoriteUser.
    * @param user The MeteoriteUser object.
    * @return The persisted MeteoriteUser.
    */
  def addUser(user: MeteoriteUser): MeteoriteUser

  /**
    * Get all users.
    * @return A list of MeteoriteUsers
    */
  def getUsers: Iterable[MeteoriteUser]

  /**
    * Update a user.
    * @param user The MeteoriteUser to update.
    */
  def updateUser(user: MeteoriteUser)

  /**
    * Delete a user.
    * @param id The MeteoriteUser to delete.
    */
  def deleteUser(id: MeteoriteUser)

  /**
    * Delete User by ID.
    * @param id The ID of the remove you wish to remove.
    */
  def deleteUser(id: Long)

  /**
    * Add a role to an existing user.
    * @param r The role object
    * @return The persisted role.
    */
  def addRole(r: MeteoriteRole): MeteoriteRole

  /**
    * Delete a role assigned to a user.
    * @param id The role ID.
    */
  def deleteRole(id: String)

  /**
    * Get a role assigned to a user.
    * @param id The role name.
    * @return The MeteoriteRole.
    */
  def getRole(id: String): MeteoriteRole
}
