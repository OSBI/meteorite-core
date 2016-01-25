package bi.meteorite.core.api.security.tokenprovider


/**
  * An authentication token.
  */
trait IToken {
  def getToken: String

  def setToken(token: String)

  def getTokenSecret: String

  def setTokenSecret(tokenSecret: String)

  def getTimestamp: Long

  def setTimestamp(timestamp: Long)

  def getProperties: Map[String, String]

  def getProperty(key: String): String

  def setProperties(properties: Map[String, String])

  def setProperty(key: String, value: String)

  def isExpired(validityDuration: Long): Boolean
}

