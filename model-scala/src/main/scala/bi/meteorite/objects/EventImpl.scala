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
  * Created by bugg on 21/12/15.
  */
@Entity(name = "EVENTS")
@Table(name = "EVENTS")
class EventImpl extends Event {
  @Id
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private[objects] var id: Int = 0
  @Column
  private[objects] var uuid: String = null
  @Column(length = 100)
  private[objects] var className: String = null
  @Column(length = 100)
  private[objects] var eventName: String = null
  @Column
  @Lob
  private[objects] var comment: String = null
  @Column(name = "starttime")
  @org.hibernate.annotations.Type(`type` = "timestamp")
  private var startDate: Date = null
  @Column(name = "endtime")
  @org.hibernate.annotations.Type(`type` = "timestamp")
  private var endDate: Date = null
  @Column private[objects]
  var duration: Long = 0L


  def this(uuid: String, className: String, eventName: String, comment: String, startDate: Date) {
    this()
    this.uuid = uuid
    this.className = className
    this.eventName = eventName
    this.comment = comment
    this.startDate = startDate
  }

  def getId: Int = {
    id
  }

  def setId(id: Int) {
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
