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

package bi.meteorite.objects;

import bi.meteorite.core.api.objects.MeteoriteUser;
import bi.meteorite.core.api.objects.Role;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * A User Object
 */
@Entity(name = "USERS")
@Table(name = "USERS")
public class UserImpl implements MeteoriteUser {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(length = 100)
  private String username;

  @Column(length = 100)
  private String password;

  @OneToMany(mappedBy = "userid")
  List<RoleImpl> roles = new ArrayList<>();

  @Column
  int orgId;

  @Column
  String email;


  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public List<String> getRoles() {
    //return roles;
    List l = new ArrayList();
    for (Role role : roles) {
      l.add(role.getRole());
    }
    return l;
  }

  @Override
  public void setRoles(List<String> roles) {
    for (String r : roles) {
      this.roles.add(new RoleImpl(r, this));
    }
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
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
  public int getOrgId() {
    return orgId;
  }

  @Override
  public void setOrgId(int orgId) {
    this.orgId = orgId;
  }
}
