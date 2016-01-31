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

package bi.meteorite.core.security.authorization

import java.lang.reflect.Method
import org.apache.cxf.security.SecurityContext

import java.util

import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

import scala.collection.JavaConversions._
import TokenAuthorizingInterceptor._
import scala.collection.JavaConverters._

object TokenAuthorizingInterceptor {

  private def parseRolesMap(rolesMap: Map[String, String]): scala.collection.mutable.HashMap[String, List[String]] = {
    val map = new scala.collection.mutable.HashMap[String, List[String]]()
    for ((key, value) <- rolesMap) {
      map.put(key, value.split(" ").toList)
    }
    map
  }
}

/**
  * Token Interceptor for JAAS authentication.
  * @param uniqueId
  */
class TokenAuthorizingInterceptor(uniqueId: Boolean) extends TokenAbstractAutorizingInInterceptor(uniqueId) {

  private val methodRolesMap = new HashMap[String, List[String]]()

  private var userRolesMap = new scala.collection.mutable.HashMap[String, List[String]]

  private var globalRoles =  new scala.collection.mutable.ListBuffer[String]

  private var checkConfiguredRolesOnly: Boolean = _

  def this() {
    this(true)
  }

  protected override def isUserInRole(sc: SecurityContext, roles: util.List[String], deny: Boolean): Boolean = {
    if (!checkConfiguredRolesOnly && !super.isUserInRole(sc, roles, deny)) {
      return false
    }
    if (userRolesMap.nonEmpty) {
      val userRoles = userRolesMap.get(sc.getUserPrincipal.getName)
      if (userRoles == null) {
        return false
      }
      for (role <- roles if userRoles.get.contains(role)) {
        return true
      }
      false
    } else {
      !checkConfiguredRolesOnly
    }
  }

  private def createMethodSig(method: Method): String = {
    val b = new StringBuilder(method.getReturnType.getName)
    b.append(' ').append(method.getName).append('(')
    for (cls <- method.getParameterTypes) {
      b.append(cls.getName)
    }
    b.append(')')
    b.toString

    method.getName
  }

  protected override def getExpectedRoles(method: Method): util.List[String] = {

    var roles = methodRolesMap.get(createMethodSig(method))

    if(roles.isEmpty) {
      roles = methodRolesMap.get(method.getName)
    }

    if(roles.isEmpty){
      globalRoles.toList
    }
    else{
      roles.get
    }

  }

  def setMethodRolesMap(rolesMap: java.util.Map[String, String]) =
    methodRolesMap.putAll(parseRolesMap(rolesMap.asScala.toMap))

  def setUserRolesMap(rolesMap: java.util.Map[String, String]) = userRolesMap = parseRolesMap(rolesMap.asScala.toMap)

  def setGlobalRoles(roles: String) = globalRoles = roles.split(" ").to[ListBuffer]

  def setCheckConfiguredRolesOnly(checkConfiguredRolesOnly: Boolean) = this.checkConfiguredRolesOnly =
    checkConfiguredRolesOnly

}
