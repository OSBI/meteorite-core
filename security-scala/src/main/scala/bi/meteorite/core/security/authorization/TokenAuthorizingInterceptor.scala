package bi.meteorite.core.security.authorization

import java.lang.reflect.Method
import org.apache.cxf.security.SecurityContext

import java.util

import scala.collection.immutable.HashMap
import scala.collection.mutable.ListBuffer

//remove if not needed
import scala.collection.JavaConversions._
import TokenAuthorizingInterceptor._

object TokenAuthorizingInterceptor {

  private def parseRolesMap(rolesMap: Map[String, String]): scala.collection.mutable.HashMap[String, List[String]] = {
    val map = new scala.collection.mutable.HashMap[String, List[String]]()
    for ((key, value) <- rolesMap) {
      map.put(key, value.split(" ").toList)
    }
    map
  }
}

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
  }

  protected override def getExpectedRoles(method: Method): util.List[String] = {
    var roles = methodRolesMap.get(createMethodSig(method))
    if (roles == null) {
      roles = methodRolesMap.get(method.getName)
    }
    if (roles != null) {
      return roles.get
    }
    globalRoles.toList
  }

  def setMethodRolesMap(rolesMap: Map[String, String]) {
    methodRolesMap.putAll(parseRolesMap(rolesMap))
  }

  def setUserRolesMap(rolesMap: Map[String, String]) {
    userRolesMap = parseRolesMap(rolesMap)
  }

  def setGlobalRoles(roles: String) {
    globalRoles = roles.split(" ").to[ListBuffer]
  }

  def setCheckConfiguredRolesOnly(checkConfiguredRolesOnly: Boolean) {
    this.checkConfiguredRolesOnly = checkConfiguredRolesOnly
  }
}
