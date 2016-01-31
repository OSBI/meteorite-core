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
package bi.meteorite.objects

import javax.persistence._

import bi.meteorite.core.api.objects.{MeteoriteRole, MeteoriteUser}
import com.fasterxml.jackson.annotation.{JsonInclude, JsonManagedReference}

/**
  * A User Object annotated for persistence
  */
@Entity(name = "USERS")
@Table(name = "USERS") class UserImpl extends MeteoriteUser {
  @Id
  @Column
  @TableGenerator(name = "EVENT_GEN", table = "SEQUENCES", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_NUMBER",
    pkColumnValue = "SEQUENCE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "EVENT_GEN")
  private var id: Long = _
  @Column(length = 100)
  private var username: String = _
  @Column(length = 100)
  private var password: String = _
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @OneToMany(mappedBy = "userid", fetch = FetchType.LAZY, cascade = Array(CascadeType.ALL))
  @JsonManagedReference
  private val roles: java.util.List[RoleImpl]  = new java.util.ArrayList[RoleImpl]()
  @Column
  private var orgId: Int = _
  @Column
  private var email: String = _

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

  //@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
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
    if(roles != null) {
      for (r <- roles) {
        this.roles.add(r.asInstanceOf[RoleImpl])
      }
    }
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
}
