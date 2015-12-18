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

import bi.meteorite.core.api.objects.MeteoriteUser;
import bi.meteorite.core.api.persistence.UserService;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;

import java.util.Collection;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

/**
 * Implementation for hibernate persistence of users.
 */
@OsgiServiceProvider(classes = { UserService.class })
// The properties below allow to transparently export the service as a web service using Distributed OSGi
@Properties({
    @Property(name = "service.exported.interfaces", value = "*")
})
@Singleton
@Transactional
public class UserServiceImpl implements UserService {

  @PersistenceContext(unitName = "userlist")
  EntityManager em;

  @Override
  @Transactional(Transactional.TxType.SUPPORTS)
  public MeteoriteUser getUser(String id) {
    return em.find(MeteoriteUser.class, id);
  }

  @Override
  public void addUser(MeteoriteUser user) {
    em.persist(user);
    em.flush();
  }

  @Transactional(Transactional.TxType.SUPPORTS)
  @Override
  public Collection<MeteoriteUser> getUsers() {
    CriteriaQuery<MeteoriteUser> query = em.getCriteriaBuilder().createQuery(MeteoriteUser.class);
    return em.createQuery(query.select(query.from(MeteoriteUser.class))).getResultList();
  }

  @Override
  public void updateUser(MeteoriteUser user) {
    em.merge(user);
  }

  @Override
  public void deleteUser(String id) {
    em.remove(getUser(id));
  }

  public void setEm(EntityManager em) {
    this.em = em;
  }
}
