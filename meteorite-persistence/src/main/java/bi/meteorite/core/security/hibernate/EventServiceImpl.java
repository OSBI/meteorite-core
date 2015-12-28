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
import bi.meteorite.core.api.persistence.EventService;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

/**
 * Created by bugg on 21/12/15.
 */
public class EventServiceImpl implements EventService {

  @PersistenceContext(unitName = "eventlistunit")
  EntityManager em;


  @Override
  @Transactional(Transactional.TxType.SUPPORTS)
  public Event getEventById(String id) {
    Event event = em.find(Event.class, id);
    return event;
  }

  @Override
  public Event getEventByUUID(String uuid) {
    List<Event> result = (List<Event>) em.createQuery("select e from Events e where uuid = :uuid").setParameter("uuid",
        uuid).getResultList();

    if (result.size() > 0) {
      return result.get(0);
    }
    return null;

  }

  @Override
  public Event getEventByEventName(String name) {
    List<Event> result = (List<Event>) em.createQuery("select e from Events e where eventName = :ename")
                                         .setParameter("ename", name).getResultList();

    if (result.size() > 0) {
      return result.get(0);
    }
    return null;

  }

  @Override
  public Event addEvent(Event user) {
    em.persist(user);
    em.flush();
    return user;
  }

  @Override
  public Collection<Event> getEvents() {
    CriteriaQuery<Event> query = em.getCriteriaBuilder().createQuery(Event.class);
    List<Event> collection = em.createQuery(query.select(query.from(Event.class))).getResultList();
    return collection;
  }

  @Override
  public void updateEvent(Event event) {
    em.merge(event);
  }

  @Override
  public void deleteEventById(String id) {
    em.remove(getEventById(id));
  }

  @Override
  public void deleteEventByUUID(String uuid) {
    em.remove(getEventByUUID(uuid));
  }
}
