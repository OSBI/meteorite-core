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
package bi.meteorite.core.security.rest.objects

import bi.meteorite.core.api.objects.{MeteoriteRole, MeteoriteUser}
import bi.meteorite.objects.UserImpl

/**
  * A User Object for Jackson
  */
class UserObj extends MeteoriteUser {
  private var id: Long = 0
  private var username: String = null
  private var password: String = null
  //private val roles: java.util.List[RoleImpl]  = new java.util.ArrayList[RoleImpl]()
  private var orgId: Int = 0
  private var email: String = null

  def getUsername: String = {
    username
  }

  def setUsername(username: String) {
    this.username = username
  }

  def getPassword: String = {
    password
  }

  def setPassword(password: String) {
    this.password = password
  }

  def getRoles: java.util.List[MeteoriteRole] = {
    null
  }

  def setRoles(roles: java.util.List[MeteoriteRole]) {
  }

  def getEmail: String = {
    email
  }

  def setEmail(email: String) {
    this.email = email
  }

  def getId: Long = {
    id
  }

  def setId(id: Long) {
    this.id = id
  }

  def getOrgId: Int = {
    orgId
  }

  def setOrgId(orgId: Int) {
    this.orgId = orgId
  }


  def toUserImpl(): UserImpl ={
    var u = new UserImpl
    u.setId(this.getId)
    u.setEmail(this.getEmail)
    u.setOrgId(this.getOrgId)
    u.setPassword(this.getPassword)
    u.setRoles(this.getRoles)
    u.setUsername(this.getUsername)

    u
  }
}
