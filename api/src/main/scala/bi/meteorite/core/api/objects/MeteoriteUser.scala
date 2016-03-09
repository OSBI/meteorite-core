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

import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
  * A user object for Meteorite core.
  */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
trait MeteoriteUser {
  /**
    * Get the user company.
    *
    * @return the company.
    */
  def getCompany: MeteoriteCompany

  /**
    * Set the user company.
    *
    * @param company the company.
    */
  def setCompany(company: MeteoriteCompany)

  /**
    * Get the username.
    *
    * @return the username
    */
  def getUsername: String

  /**
    * Set the username.
    *
    * @param username the username.
    */
  def setUsername(username: String)

  /**
    * Get the user password.
    *
    * @return the password.
    */
  def getPassword: String

  /**
    * Set the user password.
    *
    * @param password the password.
    */
  def setPassword(password: String)

  /**
    * Get an array of roles.
    *
    * @return an array of roles.
    */
  def getRoles: java.util.List[MeteoriteRole]

  /**
    * Set the roles.
    *
    * @param roles an array of roles.
    */
  def setRoles(roles: java.util.List[MeteoriteRole])

  /**
    * Get the user email address.
    *
    * @return the email address.
    */
  def getEmail: String

  /**
    * Set the email address.
    *
    * @param email the email address.
    */
  def setEmail(email: String)

  /**
    * Get the users unique id
    *
    * @return the id
    */
  def getId: Long

  /**
    * Set the users id.
    *
    * @param id the id.
    */
  def setId(id: Long)
}
