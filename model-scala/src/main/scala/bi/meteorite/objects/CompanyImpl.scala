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

import bi.meteorite.core.api.objects.{MeteoriteUser, MeteoriteCompany}
import com.fasterxml.jackson.annotation.{JsonInclude, JsonManagedReference}

/**
  * A User Object annotated for persistence
  */
@Entity(name = "COMPANIES")
@Table(name = "COMPANIES") class CompanyImpl extends MeteoriteCompany {
  @Id
  @Column
  @TableGenerator(name = "EVENT_GEN3", table = "SEQUENCES3", pkColumnName = "SEQ_NAME3", valueColumnName = "SEQ_NUMBER3",
    pkColumnValue = "SEQUENCE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "EVENT_GEN3")
  private var id: Long = _
  @Column(length = 100)
  private var name: String = _
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = Array(CascadeType.ALL))
  @JsonManagedReference
  private val users: java.util.List[UserImpl]  = new java.util.ArrayList[UserImpl]()

  def getName: String = {
    name
  }

  def setName(name: String) {
    this.name = name
  }

  def getUsers: java.util.List[MeteoriteUser] = {
    val l = new java.util.ArrayList[MeteoriteUser]()
    import scala.collection.JavaConversions._
    for (user <- users) {
      l.add(user)
    }
    l
  }

  def setUsers(roles: java.util.List[MeteoriteUser]) {
    import scala.collection.JavaConversions._
    if(users != null) {
      for (u <- users) {
        this.users.add(u.asInstanceOf[UserImpl])
      }
    }
  }

  def getId: Long = {
    id
  }

  def setId(id: Long) {
    this.id = id
  }
}
