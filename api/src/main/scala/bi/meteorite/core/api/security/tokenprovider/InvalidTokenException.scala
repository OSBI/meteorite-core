package bi.meteorite.core.api.security.tokenprovider

/**
  * Invalid Token Exception.
  */
@SerialVersionUID(783720889456143935L)
class InvalidTokenException(message: String = null) extends Exception(message)

