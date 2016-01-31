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

import java.util.Date

/**
  * An Event Object For Audit Purposes.
  */
trait Event {
  /**
    * Get the Event Id (set by the persistence manager)
    * @return the event ID
    */
  def getId: Long

  /**
    * Set the event ID.
    * @param id The ID number
    */
  def setId(id: Long)

  /**
    * Get the Event UUID.
    * @return A UUID
    */
  def getUuid: String

  /**
    * Set the Event UUID.
    * @param uuid The UUID String
    */
  def setUuid(uuid: String)


  /**
    * Get the triggering class.
    * @return The class name
    */
  def getClassName: String

  /**
    * Set the triggering class name.
    * @param className The class name.
    */
  def setClassName(className: String)

  /**
    * Get the event name.
    * @return The name of the event.
    */
  def getEventName: String


  /**
    * set the event name.
    * @param eventName The event name.
    */
  def setEventName(eventName: String)

  /**
    * Get any additional comment that was stored with the event.
    * @return The comment.
    */
  def getComment: String

  /**
    * Set an additional comment to add value to the event.
    * @param comment The comment string.
    */
  def setComment(comment: String)

  /**
    * Get the start date/time.
    * @return The start date & time.
    */
  def getStartDate: Date

  /**
    * Set the start date & time.
    * @param startDate
    */
  def setStartDate(startDate: Date)


  /**
    * Get the time the event finished.
    * @return The end date & time
    */
  def getEndDate: Date

  /**
    * Set the time the event ended.
    * @param endDate The end date & time.
    */
  def setEndDate(endDate: Date)

  /**
    * Get the duration in milliseconds.
    * @return The duration in milliseconds.
    */
  def getDuration: Long

  /**
    * Set the duration in milliseconds.
    * @param duration The duration.
    */
  def setDuration(duration: Long)
}
