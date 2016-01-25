package bi.meteorite.core.security.rest

import javax.inject.{Inject, Singleton}
import javax.ws.rs.core.Response

import bi.meteorite.core.api.security.AdminLoginService
import bi.meteorite.core.api.security.rest.{UserAuthentication, UserService}
import org.ops4j.pax.cdi.api.OsgiServiceProvider

@OsgiServiceProvider(classes = Array(classOf[UserService]))
@Singleton class UserAuthenticationImpl extends UserAuthentication {

  @Inject
  @volatile
  private var adminLoginService: AdminLoginService = null

  override def logout(username: String) : Response = {
    if(adminLoginService.logout(username)){
      Response.ok().build()
    }
    else{
      Response.serverError().build()
    }
  }

  def setAdminLoginService(adminLoginService: AdminLoginService) = this.adminLoginService = adminLoginService
}

