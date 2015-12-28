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

package bi.meteorite.core.security.hibernate.entity;

import org.hibernate.annotations.Type;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by bugg on 18/12/15.
 */
@Entity(name = "EVENT")
@Table(name = "EVENTS")
public class Events {

  @Id
  @Column(nullable = false)
  private int id;

  @Column(length = 100)
  private String className;

  @Column(length = 100)
  String eventName;

  @Column
  @Lob
  private String comment;

  @Column
  @Type(type = "timestamp")
  Date start;

  @Column
  @Type(type = "timestamp")
  Date end;

  @Column
  long duration;

  @Column
  private String uuid;


  public Events() {
  }

  public Events(int id, String uuid, String className, String eventName, String comment, Date start) {
    this.id = id;
    this.uuid = uuid;
    this.className = className;
    this.eventName = eventName;
    this.comment = comment;
    this.start = start;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  public long getDuration() {
    return duration;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  @Override
  public String toString() {
    return "Events{"
           + "id=" + id
           + ", className='" + className
           + '\''
           + ", eventName='" + eventName + '\''
           + ", comment='" + comment + '\''
           + ", start=" + start
           + ", end=" + end
           + ", duration=" + duration
           + ", uuid='" + uuid + '\''
           + '}';
  }
}
