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
package bi.meteorite.objects

import javax.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, OneToMany, Table, TableGenerator}

import bi.meteorite.core.api.objects.{MeteoriteRole, MeteoriteUser}

/**
  * A User Object
  */
@Entity(name = "USERS")
@Table(name = "USERS") class UserImpl extends MeteoriteUser {
  @Id
  @Column
  @TableGenerator(name = "EVENT_GEN", table = "SEQUENCES", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_NUMBER",
    pkColumnValue = "SEQUENCE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "EVENT_GEN")
  private var id: Int = 0
  @Column(length = 100)
  private var username: String = null
  @Column(length = 100)
  private var password: String = null
  @OneToMany(mappedBy = "userid")
  private val roles: java.util.List[RoleImpl]  = new java.util.ArrayList[RoleImpl]()
  @Column
  private var orgId: Int = 0
  @Column
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

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
  def getRoles: java.util.List[MeteoriteRole] = {
    val l = new java.util.ArrayList[MeteoriteRole]()
    import scala.collection.JavaConversions._
    for (role <- roles) {
      l.add(role)
    }
    l
  }

  def setRoles(roles: java.util.List[MeteoriteRole]) {
    import scala.collection.JavaConversions._
    for (r <- roles) {
      this.roles.add(r.asInstanceOf[RoleImpl])
    }
  }

  def getEmail: String = {
    email
  }

  def setEmail(email: String) {
    this.email = email
  }

  def getId: Int = {
    id
  }

  def setId(id: Int) {
    this.id = id
  }

  def getOrgId: Int = {
    orgId
  }

  def setOrgId(orgId: Int) {
    this.orgId = orgId
  }
}
