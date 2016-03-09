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
import bi.meteorite.core.api.objects.MeteoriteUser;
import bi.meteorite.core.api.persistence.CompanyService;
import bi.meteorite.objects.CompanyImpl;
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
@OsgiServiceProvider(classes = { CompanyService.class })
@Properties({
    @Property(name = "service.exported.interfaces", value = "*")
})
@Singleton
@Transactional
public class CompanyServiceImpl implements CompanyService {
  @PersistenceContext(unitName = "systemdbunit")
  EntityManager em;

  @Override
  public MeteoriteCompany mergeCompany(MeteoriteCompany company) {
    return em.merge(company);
  }

  @Override
  public MeteoriteCompany getCompany(long id) {
    return em.find(CompanyImpl.class, id);
  }

  @Override
  public MeteoriteCompany addCompany(MeteoriteCompany company) {
    CompanyImpl c = (CompanyImpl) company;
    em.persist(c);
    em.flush();
    return c;
  }

  @Transactional(Transactional.TxType.SUPPORTS)
  @Override
  public Iterable<MeteoriteCompany> getCompanies() {
    CriteriaQuery<CompanyImpl> query = em.getCriteriaBuilder().createQuery(CompanyImpl.class);
    List collection = em.createQuery(query.select(query.from(CompanyImpl.class))).getResultList();
    return JavaConversions.asScalaBuffer(collection);
  }

  @Override
  public void updateCompany(MeteoriteCompany company) {
    em.merge(company);
  }

  @Override
  public void deleteCompany(MeteoriteCompany company) {
    em.remove(getCompany(company.getId()));
  }

  @Override
  public void deleteCompany(long id) {
    em.remove(getCompany(id));
  }

  @Override
  public MeteoriteUser addUser(MeteoriteUser user) {
    UserImpl u = (UserImpl) user;
    em.persist(u);
    em.flush();
    return u;
  }

  @Override
  public void deleteUser(String username) {
    em.remove(getUser(username));
  }

  @Transactional(Transactional.TxType.SUPPORTS)
  @Override
  public MeteoriteUser getUser(String username) {
    return null;
  }

  public void setEntityManager(EntityManager em) {
    this.em = em;
  }
}
