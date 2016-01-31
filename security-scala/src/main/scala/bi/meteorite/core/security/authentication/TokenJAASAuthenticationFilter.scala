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

/**
  * Token Authentication filter for JAAS
  */
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

  interceptor.setContextName("meteorite-realm")

  interceptor.setRoleClassifierType(JAASLoginInterceptor.ROLE_CLASSIFIER_CLASS_NAME)
  interceptor.setRoleClassifier("org.apache.karaf.jaas.boot.principal.RolePrincipal")
  interceptor.setReportFault(true)

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
          m.put(classOf[SecurityContext], c);
        } catch {
          case e: TokenProviderException => context.setSecurityContext(oldcontext)
        }
      }
      if (valid == null || valid.isEmpty) {
        val handler = getFirstCallbackHandler(m)
        interceptor.handleMessage(m)
        val o = m.get(classOf[org.apache.cxf.security.SecurityContext])
        var obj = o match {
          case o2: org.apache.cxf.interceptor.security.RolePrefixSecurityContextImpl => o2
          case _ => throw new ClassCastException
        }




        val acc = AccessController.getContext
        val subject = obj.getSubject
        val principals = subject.getPrincipals
        var s = ""
        for (role <- principals if role.isInstanceOf[RolePrincipal]) {
          s += role.getName + ","
        }
        if(s.length>0) {
          s = s.substring(0, s.length - 1)
        }
        var userMap = new TreeMap[String, String]()
        userMap += (TokenProvider.USERNAME -> getUsername(handler))
        userMap += (TokenProvider.ROLES -> s)
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
