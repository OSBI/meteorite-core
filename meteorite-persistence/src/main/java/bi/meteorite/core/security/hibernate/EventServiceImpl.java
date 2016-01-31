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

package bi.meteorite.core.security.hibernate;

import bi.meteorite.core.api.objects.Event;
import bi.meteorite.core.api.persistence.EventService;
import bi.meteorite.objects.EventImpl;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;

import java.util.List;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

import scala.collection.JavaConversions;

/**
 * Default Event Service Implementation
 */
@OsgiServiceProvider(classes = { EventService.class })
@Properties({
    @Property(name = "service.exported.interfaces", value = "*")
})
@Singleton
@Transactional
public class EventServiceImpl implements EventService {

  @PersistenceContext(unitName = "systemdbunit")
  EntityManager em;


  @Override
  @Transactional(Transactional.TxType.SUPPORTS)
  public Event getEventById(String id) {
    Event event = em.find(Event.class, id);
    return event;
  }

  @Override
  public Event getEventByUUID(String uuid) {
    List<EventImpl> result =
        (List<EventImpl>) em.createQuery("select e from " + EventImpl.class.getName() + " e where uuid = :uuid")
                            .setParameter("uuid", uuid).getResultList();

    if (result.size() > 0) {
      return result.get(0);
    }
    return null;

  }

  @Override
  public Event getEventByEventName(String name) {
    List<EventImpl> result =
        (List<EventImpl>) em.createQuery("select e from " + EventImpl.class.getName() + " e where eventName = :ename")
                            .setParameter("ename", name).getResultList();
    if (result.size() > 0) {
      return result.get(0);
    }
    return null;

  }

  @Override
  public Event addEvent(Event e) {
    em.persist(e);
    em.flush();
    return e;
  }

  @Override
  public scala.collection.immutable.List<Event> getEvents() {
    CriteriaQuery<EventImpl> query = em.getCriteriaBuilder().createQuery(EventImpl.class);
    List<? extends Event> list = em.createQuery(query.select(query.from(EventImpl.class))).getResultList();

    return (scala.collection.immutable.List<Event>) JavaConversions.asScalaBuffer(list).toList();

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
