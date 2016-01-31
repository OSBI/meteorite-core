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

import java.io.IOException
import javax.servlet.http.HttpServletResponse

import org.apache.cxf.interceptor.security.AuthenticationException
import org.apache.cxf.transport.http.{AbstractHTTPDestination, HttpAuthenticationFaultHandler}
import org.apache.cxf.message.Message

/**
  * HTTP Fault Handler
  */
class CustomHttpAuthenticationFaultHandler extends HttpAuthenticationFaultHandler{

  val authenticationType1 = "Basic"
  val realm1 = "CXF service"

  override def handleFault(message: Message) {
    val ex: Exception = message.getContent(classOf[Exception])
    if (ex.isInstanceOf[AuthenticationException]) {
      val resp: HttpServletResponse = message.getExchange.getInMessage.get(AbstractHTTPDestination.HTTP_RESPONSE).asInstanceOf[HttpServletResponse]
      resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED)
      resp.setHeader("WWW-Authenticate", authenticationType1 + " realm=\"" + realm1 + "\"")
      resp.setHeader("Access-Control-Allow-Origin", "*")
      resp.setContentType("text/plain")

      try {
        resp.getWriter.write(ex.getMessage)
        resp.getWriter.flush
        message.getInterceptorChain.abort
      }
      catch {
        case e: IOException => {
        }
      }
    }
  }

}
