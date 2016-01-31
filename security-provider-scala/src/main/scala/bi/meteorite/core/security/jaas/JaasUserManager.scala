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

import java.security.PrivilegedAction
import java.util.Collections
import javax.inject.{Inject, Named, Singleton}
import javax.security.auth.login.{Configuration, AppConfigurationEntry}

import bi.meteorite.core.api.objects.{UserList, MeteoriteUser}
import bi.meteorite.core.api.persistence.UserService
import bi.meteorite.core.api.security.IUserManagementProvider
import bi.meteorite.core.api.security.exceptions.MeteoriteSecurityException
import org.apache.karaf.jaas.boot.ProxyLoginModule
import org.apache.karaf.jaas.config.JaasRealm
import org.apache.karaf.jaas.modules.{BackingEngine, BackingEngineFactory, BackingEngineService}
import org.ops4j.pax.cdi.api.{OsgiService, OsgiServiceProvider}
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

/**
  * Default JaaS User Manager to control users in Karaf.
  */
@Singleton
@OsgiServiceProvider(classes = Array(classOf[IUserManagementProvider]))
class JaasUserManager extends IUserManagementProvider {
  @Inject
  @Named("engineService") private var backingEngineService: BackingEngineService = null
  @Inject
  @OsgiService private var realm: JaasRealm = null
  @Inject
  @OsgiService private var userService: UserService = null

  private def getEngine: BackingEngine = {
    var config : Configuration = null
    if (config == null) {
      config = java.security.AccessController.doPrivileged(new PrivilegedAction[Configuration]() {
        def run: Configuration = {
          Configuration.getConfiguration
        }
      })
    }
    var entries2: Array[AppConfigurationEntry] = config.getAppConfigurationEntry("meteorite-realm")
    for (entry <- entries2) {
      val moduleClass: String = entry.getOptions.get(ProxyLoginModule.PROPERTY_MODULE).asInstanceOf[String]
      if (moduleClass != null) {
        val factories: BackingEngineFactory = null
        val options = entry.getOptions
        return backingEngineService.get(entry)
      }
    }
    null
  }

  @throws(classOf[MeteoriteSecurityException])
  def addUser(u: MeteoriteUser) = getUsersId.contains(u.getId) match {
      case true => userService.mergeUser(u)
      case false => userService.addUser(u)
  }

  @throws(classOf[MeteoriteSecurityException])
  def deleteUser(u: MeteoriteUser) = getUsersId.contains(u.getId) match {
    case true => userService.deleteUser(u)
    case false => throw new MeteoriteSecurityException("User Doesn't Exist")
  }

  @throws(classOf[MeteoriteSecurityException])
  def deleteUser(u: Long) = getUsersId.contains(u) match {
    case true => userService.deleteUser(u)
    case false => throw new MeteoriteSecurityException("User Doesn't Exist")
  }


  @throws(classOf[MeteoriteSecurityException])
  def getUsers: List[UserList] = (for (user <- userService.getUsers) yield
    UserList(user.getId, user.getUsername)).toList

  @throws(classOf[MeteoriteSecurityException])
  def getUsersId: List[Long] = (for(user <- userService.getUsers) yield user.getId).toList


  @throws(classOf[MeteoriteSecurityException])
  def getRoles(u: String): List[String] = {
    val s = new ListBuffer[String]
    val u2 = getUsers
    if (u2.contains(u)) {
      for (p <- getEngine.listUsers) {
        if (p.getName == u) {
          for (r <- getEngine.listRoles(p)) {
            s.add(r.getName)
          }
        }
      }
      s.toList
    }
    else {
      throw new MeteoriteSecurityException("User does not exist")
    }
  }

  @throws(classOf[MeteoriteSecurityException])
  def addRole(u: String, r: String) {
    for (p <- getEngine.listUsers) {
      if (p.getName == u) {
        val roles = getEngine.listRoles(p)
        if (roles.size == 0) {
          getEngine.addRole(u, r)
        }
        else {
          for (ro <- roles) {
            if (!Collections.singletonList(ro).contains(r)) {
              getEngine.addRole(u, r)
            }
          }
        }
      }
    }
  }

  @throws(classOf[MeteoriteSecurityException])
  def removeRole(u: String, r: String) = getRoles(u).contains(r) match {
    case true => getEngine.deleteRole(u, r)
    case false => throw new MeteoriteSecurityException("Role does not exist for user")
  }

  @throws(classOf[MeteoriteSecurityException])
  def updateUser(u: MeteoriteUser): MeteoriteUser = userService.mergeUser(u)

  @throws(classOf[MeteoriteSecurityException])
  def isAdmin(u: String): Boolean = {
    for (p <- getEngine.listUsers) {
      if (p.getName == u) {
        val roles = getEngine.listRoles(p)
        for (r <- roles) {
          if (getAdminRoles.contains(r.getName)) {
            return true
          }
        }
      }
    }
    false
  }

  @throws(classOf[MeteoriteSecurityException])
  def getAdminRoles: List[String] = {
    null
  }

  @throws(classOf[MeteoriteSecurityException])
  def getUser(id: Long): MeteoriteUser = userService.getUser(id)

  def setBackingEngineService(jassservice: BackingEngineService)= this.backingEngineService = jassservice

  def setRealm(realm: JaasRealm) = this.realm = realm

  def setUserService(userService: UserService) = this.userService = userService
}

