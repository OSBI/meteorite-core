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

import bi.meteorite.core.api.objects.MeteoriteUser
import bi.meteorite.core.api.objects.MeteoriteRole
import javax.persistence._

import com.fasterxml.jackson.annotation.JsonBackReference

/**
  * Created by bugg on 29/12/15.
  */
@Entity(name = "ROLES")
@Table(name = "ROLES") class RoleImpl extends MeteoriteRole {
  @Id
  @Column
  @TableGenerator(name = "EVENT_GEN2", table = "SEQUENCES2", pkColumnName = "SEQ_NAME2", valueColumnName = "SEQ_NUMBER2", pkColumnValue = "SEQUENCE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "EVENT_GEN2")
  private var id: Int = 0
  @ManyToOne(fetch = FetchType.LAZY, cascade = Array(CascadeType.ALL))
  @JoinColumn(name = "USER_ID", nullable = false)
  @JsonBackReference
  private var userid: UserImpl = null
  @Column
  private var rolename: String = null

  def getId: Int = {
    id
  }

  def setId(id: Int) {
    this.id = id
  }

  def getUserId: MeteoriteUser = {
    userid
  }

  def setUserId(id: MeteoriteUser) {
    this.userid = id.asInstanceOf[UserImpl]
  }

  def getRole: String = {
    rolename
  }

  def setRole(role: String) {
    this.rolename = role
  }

  def this(rolename: String, userid: UserImpl) {
    this()
    this.rolename = rolename
    this.userid = userid
  }
}
