package bi.meteorite.core.security.authentication

import java.io.IOException
import javax.servlet.http.HttpServletResponse

import org.apache.cxf.interceptor.security.AuthenticationException
import org.apache.cxf.transport.http.{AbstractHTTPDestination, HttpAuthenticationFaultHandler}
import org.apache.cxf.message.Message

/**
  * Created by bugg on 27/01/16.
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
