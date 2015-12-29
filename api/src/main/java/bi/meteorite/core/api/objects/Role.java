package bi.meteorite.core.api.objects;

/**
 * Created by bugg on 29/12/15.
 */
public interface Role {

  int getId();
  void setId(int id);
  MeteoriteUser getUserId();
  void setUserId(MeteoriteUser id);
  String getRole();
  void setRole(String role);
}
