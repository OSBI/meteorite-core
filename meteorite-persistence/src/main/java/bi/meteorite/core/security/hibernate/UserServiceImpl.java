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

import bi.meteorite.core.api.objects.MeteoriteCompany;
import bi.meteorite.core.api.objects.MeteoriteRole;
import bi.meteorite.core.api.objects.MeteoriteUser;
import bi.meteorite.core.api.persistence.UserService;
import bi.meteorite.objects.CompanyImpl;
import bi.meteorite.objects.RoleImpl;
import bi.meteorite.objects.UserImpl;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;

import java.util.List;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

import scala.collection.Iterable;
import scala.collection.JavaConversions;

/**
 * Implementation for hibernate persistence of users.
 */
@OsgiServiceProvider(classes = { UserService.class })
@Properties({
    @Property(name = "service.exported.interfaces", value = "*")
})
@Singleton
@Transactional
public class UserServiceImpl implements UserService {

  @PersistenceContext(unitName = "systemdbunit")
  EntityManager em;

  @Override
  public MeteoriteUser mergeUser(MeteoriteUser u) {
    return em.merge(u);
  }

  @Override
  @Transactional(Transactional.TxType.SUPPORTS)
  public MeteoriteUser getUser(String id) {
    return em.find(UserImpl.class, id);
  }

  @Override
  public MeteoriteUser getUser(long id) {
    return em.find(UserImpl.class, id);
  }

  @Override
  public MeteoriteUser addUser(MeteoriteUser user) {
    UserImpl u = (UserImpl) user;

    if (u.getCompany() != null) {
      CompanyImpl c = em.find(CompanyImpl.class, ((MeteoriteCompany) u.getCompany()).getId());
      u.setCompany(c);
    }

    em.persist(u);
    em.flush();

    return u;
  }

  @Transactional(Transactional.TxType.SUPPORTS)
  @Override
  public Iterable<MeteoriteUser> getUsers() {
    CriteriaQuery<UserImpl> query = em.getCriteriaBuilder().createQuery(UserImpl.class);
    List collection = em.createQuery(query.select(query.from(UserImpl.class))).getResultList();
    return JavaConversions.asScalaBuffer(collection);
  }

  @Override
  public void updateUser(MeteoriteUser user) {
    em.merge(user);
  }

  @Override
  public void deleteUser(MeteoriteUser id) {
    em.remove(getUser(id.getId()));
  }

  @Override
  public void deleteUser(long id) {
    em.remove(getUser(id));
  }

  @Override
  public MeteoriteRole addRole(MeteoriteRole r) {
    RoleImpl u = (RoleImpl) r;
    em.persist(u);
    em.flush();
    return u;
  }

  @Override
  public void deleteRole(String id) {
    em.remove(getRole(id));
  }

  @Override
  public MeteoriteRole getRole(String id) {
    MeteoriteRole r = em.find(RoleImpl.class, id);

    return r;
  }


  public void setEntityManager(EntityManager em) {
    this.em = em;
  }
}
