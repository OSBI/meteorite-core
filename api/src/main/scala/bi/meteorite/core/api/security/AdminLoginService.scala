package bi.meteorite.core.api.security

import javax.security.auth.login.LoginContext

/**
  * Admin Api Interface for user management.
  */
trait AdminLoginService {
  def login(username: String, password: String): Boolean

  def logout(username: String): Boolean

  def getUsername: String

  def getRoles: List[String]

  /**
    * Set the JaaS realm.
    *
    * @param realm The JAAS Realm
    */
  def setRealm(realm: String)

  def setLoginContext(loginContext: LoginContext)
}

