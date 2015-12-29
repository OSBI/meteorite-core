package bi.meteorite.objects;

import bi.meteorite.core.api.objects.MeteoriteUser;
import bi.meteorite.core.api.objects.Role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by bugg on 29/12/15.
 */
@Entity(name = "ROLES")
@Table(name = "ROLES")
public class RoleImpl implements Role {

  @Id
  @Column
  @GeneratedValue(strategy= GenerationType.AUTO)
  private int id;

  @ManyToOne
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
    this.userid = userid;
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
