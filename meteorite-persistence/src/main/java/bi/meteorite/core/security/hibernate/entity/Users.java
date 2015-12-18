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

import bi.meteorite.core.api.objects.MeteoriteUser;
import bi.meteorite.core.security.hibernate.objects.User;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

/**
 * Created by bugg on 18/12/15.
 */
@Entity(name = "USER")
@Table(name = "USERS")
public class Users {

  @Id
  @Column(nullable = false)
  private int id;

  @Column(length = 100)
  private String username;

  @Column(length = 100)
  String password;

  @ElementCollection
  @CollectionTable(name = "ROLES", joinColumns = @JoinColumn(name = "user_id"))
  @Column
  List<String> roles;

  @Column
  int orgId;

  @Column
  String email;


  public Users() {
  }

  public Users(String username, String password, List<String> roles, int orgId, String email) {
    this.username = username;
    this.password = password;
    this.roles = roles;
    this.orgId = orgId;
    this.email = email;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

  public int getOrgId() {
    return orgId;
  }

  public void setOrgId(int orgId) {
    this.orgId = orgId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return "Users{"
           + "id=" + id
           + ", username='" + username + '\''
           + ", password='" + password + '\''
           + ", roles=" + roles
           + ", orgId='" + orgId + '\''
           + ", email='" + email + '\''
           + '}';
  }

  public MeteoriteUser convert(Users u) {
    MeteoriteUser u2 = new User();

    u2.setUsername(u.getUsername());
    u2.setId(u.getId());
    u2.setEmail(u.getEmail());
    u2.setOrgId(u.getOrgId());
    u2.setRoles(u.getRoles().toArray(new String[u.getRoles().size()]));
    u2.setPassword(u.getPassword());
    return u2;
  }
}
