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


package bi.meteorite.core.security.hibernate;

import bi.meteorite.core.api.objects.Event;
import bi.meteorite.core.api.objects.MeteoriteUser;
import bi.meteorite.core.api.persistence.EventService;
import bi.meteorite.core.api.persistence.UserService;
import bi.meteorite.core.security.objects.EventImpl;
import bi.meteorite.core.security.objects.User;

import java.util.Date;
import java.util.UUID;

/**
 * Created by bugg on 21/12/15.
 */
public class DefaultUsers {

  private UserService userService;
  private EventService eventService;

  public DefaultUsers() {
  //  insertUsers();
  }

  public void insertUsers() {

    if (eventService.getEventByEventName("Start Adding Users") == null) {
      String uuid = UUID.randomUUID().toString();
      Event e = eventService.addEvent(new EventImpl(uuid, this.getClass().getName(), "Start Adding users",
          "Adding users to user list", new Date()));
      MeteoriteUser u = new User();
      u.setUsername("admin");
      String[] s = { "ROLE_ADMIN", "ROLE_USER" };
      u.setRoles(s);
      u.setPassword("admin");
      userService.addUser(u);

      u = new User();
      u.setUsername("smith");
      u.setPassword("smith");
      String[] s2 = { "ROLE_USER" };
      u.setRoles(s2);
      userService.addUser(u);

      e.setEndDate(new Date());

      e.setDuration(e.getEndDate().getTime() - e.getStartDate().getTime());

      eventService.updateEvent(e);
    }

  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public void setEventService(EventService eventService) {
    this.eventService = eventService;
  }
}
