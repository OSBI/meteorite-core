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

package bi.meteorite.core.api.persistence;

import bi.meteorite.core.api.objects.Event;

import java.util.Collection;

/**
 * Created by bugg on 21/12/15.
 */
public interface EventService {

  Event getEventById(String id);

  Event getEventByUUID(String uuid);

  Event getEventByEventName(String name);

  Event addEvent(Event user);

  Collection<Event> getEvents();

  void updateEvent(Event event);

  void deleteEventById(String id);

  void deleteEventByUUID(String uuid);
}
