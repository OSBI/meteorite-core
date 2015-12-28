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

package bi.meteorite.core.security.hibernate.objects;

import bi.meteorite.core.api.objects.Event;

import java.util.Date;

/**
 * Created by bugg on 21/12/15.
 */
public class EventImpl implements Event {

  String uuid;
  String className;
  String eventName;
  String comment;
  Date startDate;
  Date endDate;
  long duration;
  private int id;

  public EventImpl(String uuid, String className, String eventName, String comment, Date startDate) {
    this.uuid = uuid;
    this.className = className;
    this.eventName = eventName;
    this.comment = comment;
    this.startDate = startDate;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getUuid() {
    return uuid;
  }

  @Override
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  @Override
  public String getClassName() {
    return className;
  }

  @Override
  public void setClassName(String className) {
    this.className = className;
  }

  @Override
  public String getEventName() {
    return eventName;
  }

  @Override
  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  @Override
  public String getComment() {
    return comment;
  }

  @Override
  public void setComment(String comment) {
    this.comment = comment;
  }

  @Override
  public Date getStartDate() {
    return startDate;
  }

  @Override
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  @Override
  public Date getEndDate() {
    return endDate;
  }

  @Override
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  @Override
  public long getDuration() {
    return duration;
  }

  @Override
  public void setDuration(long duration) {
    this.duration = duration;
  }
}
