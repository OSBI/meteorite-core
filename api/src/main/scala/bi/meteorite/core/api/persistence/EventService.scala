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
package bi.meteorite.core.api.persistence

import bi.meteorite.core.api.objects.Event

/**
  * Interface for event persistence.
  */
trait EventService {
  /**
    * Get the event by persistence managed ID
    * @param id
    * @return the event
    */
  def getEventById(id: String): Event

  /**
    * Get the event by the UUID set when the event was created.
    * @param uuid
    * @return The event
    */
  def getEventByUUID(uuid: String): Event

  /**
    * Get the event by the event name.
    * @param name
    * @return A List of events.
    */
  def getEventByEventName(name: String): List[Event]

  /**
    * Add an event.
    * @param event The event object.
    * @return The persisted event.
    */
  def addEvent(event: Event): Event

  /**
    * Get all events.
    * @return A list of events.
    */
  def getEvents: List[Event]

  /**
    * Update event.
    * @param event Update an already existing event.
    */
  def updateEvent(event: Event)

  /**
    * Delete event by persistence ID.
    * @param id the id of the event.
    */
  def deleteEventById(id: String)

  /**
    * Delete event by event UUID.
    * @param uuid the UUID of the event.
    */
  def deleteEventByUUID(uuid: String)
}

