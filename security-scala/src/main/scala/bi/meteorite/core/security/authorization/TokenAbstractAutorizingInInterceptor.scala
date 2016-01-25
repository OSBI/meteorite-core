package bi.meteorite.core.security.authorization

import java.lang.reflect.Method
import java.util
import java.util.logging.Level
import org.apache.cxf.security.SecurityContext

import org.apache.cxf.common.logging.LogUtils
import org.apache.cxf.interceptor.security.{AbstractAuthorizingInInterceptor, AccessDeniedException}
import org.apache.cxf.message.Message
//remove if not needed
import scala.collection.JavaConversions._

object TokenAbstractAutorizingInInterceptor {

  private val LOG = LogUtils.getL7dLogger(classOf[TokenAbstractAutorizingInInterceptor])

  private val ALL_ROLES = "*"
}

abstract class TokenAbstractAutorizingInInterceptor(uniqueId: Boolean) extends AbstractAuthorizingInInterceptor(uniqueId) {

  def this() {
    this(true)
  }

  override def handleMessage(message: Message) {
    val method = getTargetMethod(message)
    val sc = message.get(classOf[SecurityContext])
    if (sc == null) {
      val sc2 = message.get(classOf[org.apache.cxf.security.SecurityContext])
      if (authorize(sc2, method)) {
        return
      }
    } else if (sc.getUserPrincipal != null) {
      if (authorize(sc, method)) {
        return
      }
    } else if (!isMethodProtected(method) && isAllowAnonymousUsers) {
      return
    }
    throw new AccessDeniedException("Unauthorized")
  }

  protected override def authorize(sc: SecurityContext, method: Method): Boolean = {
    val expectedRoles = getExpectedRoles(method)
    if (expectedRoles.isEmpty) {
      val denyRoles = getDenyRoles(method)
      return denyRoles.isEmpty || isUserInRole(sc, denyRoles, deny = true)
    }
    if (isUserInRole(sc, expectedRoles, deny = false)) {
      return true
    }
    if (TokenAbstractAutorizingInInterceptor.LOG.isLoggable(Level.FINE)) {
      TokenAbstractAutorizingInInterceptor.LOG.fine(sc.getUserPrincipal.getName + " is not authorized")
    }
    false
  }


  protected override def isUserInRole(sc: SecurityContext, roles: util.List[String], deny: Boolean): Boolean = {
    if (roles.size == 1 && TokenAbstractAutorizingInInterceptor.ALL_ROLES == roles.get(0)) {
      return !deny
    }
    for (role <- roles if sc.isUserInRole(role)) {
      return !deny
    }
    deny
  }
}
