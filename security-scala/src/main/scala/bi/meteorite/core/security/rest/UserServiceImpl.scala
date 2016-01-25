package bi.meteorite.core.security.rest

import javax.inject.{Inject, Named, Singleton}
import javax.ws.rs.core.Response

import bi.meteorite.core.api.objects.MeteoriteUser
import bi.meteorite.core.api.security.IUserManagementProvider
import bi.meteorite.core.api.security.rest.UserService
import org.ops4j.pax.cdi.api.{OsgiService, OsgiServiceProvider}

@OsgiServiceProvider(classes = Array(classOf[UserService]))
@Singleton
@Named("userRestServiceBean")
class UserServiceImpl extends UserService {

  @Inject
  @OsgiService
  private var iUserManagementProvider: IUserManagementProvider = _

  override def addUser(u: MeteoriteUser): Response = {
    iUserManagementProvider.addUser(u.getUsername, u.getPassword)
    Response.ok().build()
  }

  override def modifyUser(u: MeteoriteUser): Response = Response.ok(iUserManagementProvider.updateUser(u)).build()

  override def deleteUser(u: MeteoriteUser): Response = {
    iUserManagementProvider.deleteUser(u.getUsername)
    Response.ok().build()
  }

  override def deleteUser(id: String): Response = {
    iUserManagementProvider.deleteUser(id)
    Response.ok().build()
  }

  override def addRole(id: String, group: Int): Response = Response.serverError().build()

  override def addRole(id: String, group: String): Response = {
    iUserManagementProvider.addRole(id, group)
    Response.ok().build()
  }

  override def getExistingUsers: Response = Response.ok(iUserManagementProvider.getUsers).build()

  override def getUser(id: Int): Response = Response.ok(iUserManagementProvider.getUser(id)).build()

  override def whoami: Response = {
    Response.ok("{\"login\":{\"password\":\"pass\",\"username\":\"test3\"}}")
      .build()
  }

  def setiUserManagementProvider(iUserManagementProvider: IUserManagementProvider) = this.iUserManagementProvider =
    iUserManagementProvider

}