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
package bi.meteorite.core.security.hibernate

import bi.meteorite.core.api.objects.Event
import bi.meteorite.core.api.objects.MeteoriteUser
import bi.meteorite.core.api.objects.MeteoriteCompany
import bi.meteorite.core.api.objects.MeteoriteRole
import bi.meteorite.core.api.persistence.EventService
import bi.meteorite.core.api.persistence.UserService
import bi.meteorite.core.api.persistence.CompanyService
import bi.meteorite.objects.EventImpl
import bi.meteorite.objects.RoleImpl
import bi.meteorite.objects.UserImpl
import bi.meteorite.objects.CompanyImpl
import java.util.Date
import java.util.UUID
import javax.annotation.PostConstruct
import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

/**
  * Default Users for demo and initial install purposes.
  */
class DefaultUsers {
  private var userService: UserService = null
  private var companyService: CompanyService = null
  private var eventService: EventService = null

  @PostConstruct def insertUsers() {
    if (eventService.getEventByEventName("Start Adding Users") == null) {
      val uuid: String = UUID.randomUUID.toString
      val e: Event = eventService.addEvent(new EventImpl(uuid, this.getClass.getName, "Start Adding users", "Adding users to user list", new Date))

      var c: MeteoriteCompany = new CompanyImpl
      c.setName("company")
      c = companyService.addCompany(c)

      var u: MeteoriteUser = new UserImpl
      u.setCompany(c)
      u.setUsername("admin")
      u.setPassword("admin")

      val r: MeteoriteRole = new RoleImpl
      r.setUserId(u)
      r.setRole("ROLE_ADMIN")

      val r2: MeteoriteRole = new RoleImpl
      r2.setUserId(u)
      r2.setRole("ROLE_USER")
      val l = ListBuffer[MeteoriteRole](r, r2)
      u.setRoles(l.asJava)
      u = userService.addUser(u)

      u = new UserImpl
      u.setCompany(c)
      u.setUsername("smith")
      u.setPassword("smith")
      val s2 = List[MeteoriteRole](new RoleImpl("ROLE_USER", u.asInstanceOf[UserImpl]))
      u.setRoles(s2.asJava)
      userService.addUser(u)
      e.setEndDate(new Date)
      e.setDuration(e.getEndDate.getTime - e.getStartDate.getTime)
      eventService.updateEvent(e)
    }
  }

  def setUserService(userService: UserService) {
    this.userService = userService
  }

  def setEventService(eventService: EventService) {
    this.eventService = eventService
  }
}
