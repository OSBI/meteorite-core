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
package bi.meteorite.core.api.objects

/**
  * An internal role for authentication.
  */
trait MeteoriteRole {
  /**
    * Get the ID of the role (usually set by the persistence manager)
    * @return the ID.
    */
  def getId: Int

  /**
    * Set the ID
    * @param id the id.
    */
  def setId(id: Int)

  /**
    * Get the user object that is associated with the role.
    * @return A meteorite user object.
    */
  def getUserId: MeteoriteUser

  /**
    * Set the user associated with the role.
    * @param id the user.
    */
  def setUserId(id: MeteoriteUser)

  /**
    * Get the role name.
    * @return The role.
    */
  def getRole: String

  /**
    * Set the role name.
    * @param role
    */
  def setRole(role: String)
}

