package bi.meteorite.core.security.authentication

import java.security.{AccessController, Principal}
import javax.annotation.Priority
import javax.security.auth.Subject
import javax.security.auth.callback.{CallbackHandler, NameCallback}
import javax.ws.rs.Priorities
import javax.ws.rs.container.{ContainerRequestContext, PreMatching}
import javax.ws.rs.core.SecurityContext

import bi.meteorite.core.api.security.exceptions.TokenProviderException
import bi.meteorite.core.api.security.tokenprovider.TokenProvider
import org.apache.cxf.interceptor.security.JAASLoginInterceptor
import org.apache.cxf.interceptor.security.callback.{CallbackHandlerProvider, CallbackHandlerProviderAuthPol, CallbackHandlerProviderUsernameToken}
import org.apache.cxf.jaxrs.security.JAASAuthenticationFilter
import org.apache.cxf.jaxrs.utils.JAXRSUtils
import org.apache.cxf.message.Message
import org.apache.karaf.jaas.boot.principal.RolePrincipal

import scala.collection.JavaConversions._
import scala.collection.immutable.TreeMap

@PreMatching
@Priority(Priorities.AUTHENTICATION)
class TokenJAASAuthenticationFilter extends JAASAuthenticationFilter {


  private val interceptor = new JAASLoginInterceptor() {

    override protected def getCallbackHandler(name: String, password: String): CallbackHandler = {
      TokenJAASAuthenticationFilter.this.getCallbackHandler(name, password)
    }
  }

  private var tokenProvider: TokenProvider = _

  private var oldcontext: SecurityContext = _

  private val callbackHandlerProviders = List[CallbackHandlerProvider](new CallbackHandlerProviderAuthPol(), new CallbackHandlerProviderUsernameToken())

  interceptor.setUseDoAs(false)

  interceptor.setContextName("karaf")

  override def filter(context: ContainerRequestContext) {
    val m = JAXRSUtils.getCurrentMessage
    try {
      var valid: collection.SortedMap[String, String] = null
      val cookies = context.getCookies
      if (cookies.containsKey(TokenProvider.TOKEN_COOKIE_NAME)) {
        try {
          val cookie = cookies.get(TokenProvider.TOKEN_COOKIE_NAME)
          valid = tokenProvider.verifyToken(cookie.getValue)
          val finalValid = valid
          val c = new SecurityContext() {

            val p = new UserPrincipal(finalValid.get(TokenProvider.USERNAME).get)

            override def getUserPrincipal: Principal = p

            override def isUserInRole(role: String): Boolean = {
              val rolearray = finalValid.get(TokenProvider.ROLES).get.split(",")
              rolearray contains role
            }

            override def isSecure: Boolean = false

            override def getAuthenticationScheme: String = null
          }
          oldcontext = context.getSecurityContext
          context.setSecurityContext(c)
        } catch {
          case e: TokenProviderException => context.setSecurityContext(oldcontext)
        }
      }
      if (valid == null || valid.isEmpty) {
        val handler = getFirstCallbackHandler(m)
        interceptor.handleMessage(m)
        val acc = AccessController.getContext
        val subject = Subject.getSubject(acc)
        val principals = subject.getPrincipals
        var s = ""
        for (role <- principals if role.isInstanceOf[RolePrincipal]) {
          s += role.getName + ","
        }
        s = s.substring(0, s.length - 1)
        val userMap = new TreeMap[String, String]()
        userMap.put(TokenProvider.USERNAME, getUsername(handler))
        userMap.put(TokenProvider.ROLES, s)
        try {
          val token = tokenProvider.generateToken(userMap)
          context.setProperty("token", token)
        } catch {
          case e: TokenProviderException => e.printStackTrace()
        }
      }
    } catch {
      case ex: SecurityException => context.abortWith(handleAuthenticationException(ex, m))
    }
  }

  private def getUsername(handler: CallbackHandler): String = {
    if (handler == null) {
      return null
    }
    try {
      val usernameCallBack = new NameCallback("user")
      handler.handle(Array(usernameCallBack))
      usernameCallBack.getName
    } catch {
      case e: Exception => null
    }
  }

  private def getFirstCallbackHandler(message: Message): CallbackHandler = {
    for (cbp <- callbackHandlerProviders) {
      val cbh = cbp.create(message)
      if (cbh != null) {
        return cbh
      }
    }
    null
  }

  def setTokenProvider(tokenProvider: TokenProvider) {
    this.tokenProvider = tokenProvider
  }

  class UserPrincipal(val name: String) extends Principal{

    override def getName: String = name

  }
}
