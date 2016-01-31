/*
 * Copyright 2016 OSBI Ltd
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
package bi.meteorite.core.security.jaas

import java.io.IOException
import javax.security.auth.Subject
import javax.security.auth.callback.{Callback, CallbackHandler, NameCallback, PasswordCallback, UnsupportedCallbackException}
import javax.security.auth.login.{LoginContext, LoginException}

import bi.meteorite.core.api.security.AdminLoginService
import org.apache.karaf.jaas.boot.principal.{RolePrincipal, UserPrincipal}
import org.ops4j.pax.cdi.api.OsgiServiceProvider
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable.ListBuffer

/**
  * Jaas Login Manager
  */
@OsgiServiceProvider(classes = Array(classOf[AdminLoginService]))
object JaasLoginManager {
  private val LOGGER: Logger = LoggerFactory.getLogger(classOf[JaasLoginManager])
  val ROLES_GROUP_NAME: String = "ROLES"
  private val ROLES_PREFIX: String = "ROLE_"
}

@OsgiServiceProvider(classes = Array(classOf[AdminLoginService]))
class JaasLoginManager extends AdminLoginService {
  private var realm: String = null
  private var subject: Subject = null
  private final val roles = new ListBuffer[String]
  private var ctx: LoginContext = null

  /**
    * Login Callback Handler
    */
  private class LoginCallbackHandler extends CallbackHandler {
    private final var username: String = null
    private final var password: String = null

    def this(username: String, password: String) {
      this()
      this.username = username
      this.password = password
    }

    @throws(classOf[IOException])
    @throws(classOf[UnsupportedCallbackException])
    def handle(callbacks: Array[Callback]) {
      for (callback <- callbacks) {
        callback match {
          case callback1: NameCallback =>
            callback1.setName(username)
          case pwCallback: PasswordCallback =>
            pwCallback.setPassword(password.toCharArray)
          case _ =>
            throw new UnsupportedCallbackException(callback, "Callback type not supported")
        }
      }
    }
  }

  def login(username: String, password: String): Boolean = {
    var authenticated: Boolean = false
    val handler: LoginCallbackHandler = new LoginCallbackHandler(username, password)
    try {
      if (ctx == null) {
        ctx = new LoginContext(realm, handler)
      }
      ctx.login()
      authenticated = true
      subject = ctx.getSubject
      import scala.collection.JavaConversions._
      for (p <- subject.getPrincipals) {
        if (p.getName.startsWith(JaasLoginManager.ROLES_PREFIX)) {
          roles.add(p.getName.substring(JaasLoginManager.ROLES_PREFIX.length))
        }
      }
    }
    catch {
      case e: LoginException =>
        authenticated = false
    }
    authenticated
  }

  def logout(username: String): Boolean = {
    val handler: LoginCallbackHandler = new LoginCallbackHandler(username, null)
    try {
      if (ctx == null) {
        ctx = new LoginContext(realm, handler)
      }
      ctx.logout()
      return true
    }
    catch {
      case e: LoginException =>
        e.printStackTrace()
    }
    false
  }

  def getUsername: String = {
    val principals = subject.getPrincipals
    import scala.collection.JavaConversions._
    for (p <- principals) {
      if (p.isInstanceOf[UserPrincipal]) {
        return p.getName
      }
    }
    null
  }

  def getRoles: List[String] = {
    val s = new ListBuffer[String]
    val principals = subject.getPrincipals
    import scala.collection.JavaConversions._
    for (p <- principals) {
      System.out.println(p.getClass)
      JaasLoginManager.LOGGER.debug("Principal type:" + p.getClass)
      if (p.isInstanceOf[RolePrincipal]) {
        s.add(p.getName)
      }
    }
    s.toList
  }

  def setLoginContext(ctx: LoginContext) {
    this.ctx = ctx
  }

  def setRealm(realm: String) {
    this.realm = realm
  }
}
