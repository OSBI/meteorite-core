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

import bi.meteorite.core.api.objects.Event
import java.util.Date
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.Table

/**
  * Event Object annotated for persistence.
  */
@Entity(name = "EVENTS")
@Table(name = "EVENTS")
class EventImpl extends Event {
  @Id
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long = _
  @Column
  var uuid: String = _
  @Column(length = 100)
  var className: String = _
  @Column(length = 100)
  var eventName: String = _
  @Column
  @Lob
  var comment: String = _
  @Column(name = "starttime")
  @org.hibernate.annotations.Type(`type` = "timestamp")
  var startDate: Date = _
  @Column(name = "endtime")
  @org.hibernate.annotations.Type(`type` = "timestamp")
  var endDate: Date = _
  @Column
  var duration: Long = 0L


  def this(uuid: String, className: String, eventName: String, comment: String, startDate: Date) {
    this()
    this.uuid = uuid
    this.className = className
    this.eventName = eventName
    this.comment = comment
    this.startDate = startDate
  }

  def getId: Long = {
    id
  }

  def setId(id: Long) {
    this.id = id
  }

  def getUuid: String = {
    uuid
  }

  def setUuid(uuid: String) {
    this.uuid = uuid
  }

  def getClassName: String = {
    className
  }

  def setClassName(className: String) {
    this.className = className
  }

  def getEventName: String = {
    eventName
  }

  def setEventName(eventName: String) {
    this.eventName = eventName
  }

  def getComment: String = {
    comment
  }

  def setComment(comment: String) {
    this.comment = comment
  }

  def getStartDate: Date = {
    startDate
  }

  def setStartDate(startDate: Date) {
    this.startDate = startDate
  }

  def getEndDate: Date = {
    endDate
  }

  def setEndDate(endDate: Date) {
    this.endDate = endDate
  }

  def getDuration: Long = {
    duration
  }

  def setDuration(duration: Long) {
    this.duration = duration
  }

  override def toString: String = {
    "EventImpl{" + "id=" + id + ", uuid='" + uuid + '\'' + ", className='" + className + '\'' + ", eventName='" + eventName + '\'' + ", comment='" + comment + '\'' + ", startDate=" + startDate + ", endDate=" + endDate + ", duration=" + duration + '}'
  }
}
