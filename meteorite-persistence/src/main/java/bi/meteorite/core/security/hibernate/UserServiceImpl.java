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
import bi.meteorite.core.security.hibernate.entity.Users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

/**
 * Implementation for hibernate persistence of users.
 */
@Singleton
@Transactional
public class UserServiceImpl implements UserService {

  EntityManager em;

  @Override
  @Transactional(Transactional.TxType.SUPPORTS)
  public MeteoriteUser getUser(String id) {
    Users user = em.find(Users.class, id);
    return user.convert(user);
  }

  @Override
  public void addUser(MeteoriteUser user) {
    em.persist(new Users(user.getUsername(), user.getPassword(), Arrays.asList(user.getRoles()),
        user.getOrgId(), user.getEmail()));
    em.flush();
  }

  @Transactional(Transactional.TxType.SUPPORTS)
  @Override
  public Collection<MeteoriteUser> getUsers() {
    CriteriaQuery<Users> query = em.getCriteriaBuilder().createQuery(Users.class);
    List<Users> collection = em.createQuery(query.select(query.from(Users.class))).getResultList();
    List<MeteoriteUser> m = new ArrayList<>();
    for (Users c : collection) {
      m.add(c.convert(c));
    }
    return m;
  }

  @Override
  public void updateUser(MeteoriteUser user) {
    em.merge(user);
  }

  @Override
  public void deleteUser(String id) {
    em.remove(getUser(id));
  }

  public void setEntityManager(EntityManager em) {
    this.em = em;
  }
}
