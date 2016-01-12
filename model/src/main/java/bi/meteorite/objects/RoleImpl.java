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
import bi.meteorite.core.api.objects.MeteoriteRole;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * Created by bugg on 29/12/15.
 */
@Entity(name = "ROLES")
@Table(name = "ROLES")
public class RoleImpl implements MeteoriteRole {

  @Id
  @Column
  @TableGenerator(name = "EVENT_GEN2",
      table = "SEQUENCES2",
      pkColumnName = "SEQ_NAME2",
      valueColumnName = "SEQ_NUMBER2",
      pkColumnValue = "SEQUENCE",
      allocationSize=1)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "EVENT_GEN2")
  private int id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID", nullable = false)
  private UserImpl userid;

  @Column
  private String rolename;

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public MeteoriteUser getUserId() {
    return userid;
  }

  @Override
  public void setUserId(MeteoriteUser id) {
    this.userid = (UserImpl) id;
  }

  @Override
  public String getRole() {
    return rolename;
  }

  @Override
  public void setRole(String role) {
    this.rolename = role;
  }

  public RoleImpl() {
  }

  public RoleImpl(String rolename, UserImpl userid) {
    this.rolename = rolename;
    this.userid = userid;
  }
}
