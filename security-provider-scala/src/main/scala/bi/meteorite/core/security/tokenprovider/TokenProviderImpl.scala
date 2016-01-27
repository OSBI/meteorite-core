/*
 * Copyright 2015 OSBI Ltd
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
package bi.meteorite.core.security.tokenprovider

import java.io.UnsupportedEncodingException
import java.net.{URLDecoder, URLEncoder}
import java.security.NoSuchAlgorithmException
import javax.crypto.spec.SecretKeySpec
import javax.crypto.{Cipher, KeyGenerator, SecretKey}
import javax.inject.{Inject, Singleton}
import javax.servlet.http.HttpServletRequest

import bi.meteorite.core.api.security.exceptions.TokenProviderException
import bi.meteorite.core.api.security.tokenprovider.{InvalidTokenException, TokenProvider}
import org.apache.commons.codec.binary.{Base64, StringUtils}
import org.apache.commons.codec.digest.DigestUtils
import org.ops4j.pax.cdi.api.OsgiServiceProvider

import scala.collection.immutable.TreeMap

/**
  * Token Provider.
  */
object TokenProviderImpl {
  val AUTHORIZATION_HEADER_AMDATU: String = TokenUtil.AUTHORIZATION_HEADER_AMDATU
  private val ENCRYPTION_METHOD: String = "AES"
  private val ENCRYPTION_METHOD_BYTES: Int = 128 / 8
  private val DEFAULT_CHARSET: String = "UTF-8"
  private val SECRET_KEY_PROPERTY: String = "secretkey"
  private val RANDOM_KEY: String = "[randomkey]"
}

@Singleton
@OsgiServiceProvider(classes = Array(classOf[TokenProvider]))
class TokenProviderImpl extends TokenProvider {
  @Inject
  @volatile
  private var mtokenStore: TokenStorageProvider = null
  @volatile
  private var msecretKey: SecretKey = null
  @volatile
  private var mprivateKey: String = null
  @volatile
  private var mencryptCipher: Cipher = null
  @volatile
  private var mdecryptCipher: Cipher = null

  initKey()

  @SuppressWarnings(Array("rawtypes"))
  @throws(classOf[Exception])
  def updated(properties: Map[String,String]) {
    if (properties == null) {
      return
    }
    val key: String = properties.get(TokenProviderImpl.SECRET_KEY_PROPERTY).get
    if (key != null && (TokenProviderImpl.RANDOM_KEY == key.toString)) {
      try {
        val keyGen: KeyGenerator = KeyGenerator.getInstance(TokenProviderImpl.ENCRYPTION_METHOD)
        keyGen.init(TokenProviderImpl.ENCRYPTION_METHOD_BYTES * 8)
        msecretKey = keyGen.generateKey
        mprivateKey = new Base64(0).encodeToString(msecretKey.getEncoded)
        startCiphers()
      }
      catch {
        case e: NoSuchAlgorithmException =>
      }
    }
    else if (key != null && !key.toString.trim.isEmpty && key.toString.length >= TokenProviderImpl.ENCRYPTION_METHOD_BYTES) {
      var bytes: Array[Byte] = new Array[Byte](TokenProviderImpl.ENCRYPTION_METHOD_BYTES)
      val keyBytes: Array[Byte] = key.toString.getBytes
      if (keyBytes.length == TokenProviderImpl.ENCRYPTION_METHOD_BYTES) {
        bytes = keyBytes
      }
      else {
        System.arraycopy(keyBytes, 0, bytes, 0, TokenProviderImpl.ENCRYPTION_METHOD_BYTES)
      }
      msecretKey = new SecretKeySpec(bytes, TokenProviderImpl.ENCRYPTION_METHOD)
      mprivateKey = new Base64(0).encodeToString(msecretKey.getEncoded)
      startCiphers()
    }
    else {
    }
  }

  private def startCiphers() {
    if (mencryptCipher == null || mdecryptCipher == null) {
      try {
        mencryptCipher = Cipher.getInstance(TokenProviderImpl.ENCRYPTION_METHOD)
        mencryptCipher.init(Cipher.ENCRYPT_MODE, msecretKey)
        mdecryptCipher = Cipher.getInstance(TokenProviderImpl.ENCRYPTION_METHOD)
        mdecryptCipher.init(Cipher.DECRYPT_MODE, msecretKey)
      }
      catch {
        case e: Any =>
          throw new RuntimeException(e)
      }
    }
  }

  @throws(classOf[TokenProviderException])
  private def generateSignature(attributes: TreeMap[String, String]): String = {
    var signvalue: String = mprivateKey
    if (attributes != null && attributes.nonEmpty) {
      try {
        signvalue += attributesToString(attributes)
      }
      catch {
        case e : UnsupportedEncodingException =>
          throw new TokenProviderException(e.getMessage)
      }
    }
    DigestUtils.md5Hex(signvalue)
  }

  @throws(classOf[TokenProviderException])
  private def verifySignature(attributes: TreeMap[String, String], signature: String): Boolean = {
    val verifySignature: String = generateSignature(attributes)
    signature == verifySignature
  }

  @throws(classOf[UnsupportedEncodingException])
  private def attributesToString(attributes: TreeMap[String, String]): String = {
    val result: StringBuilder = new StringBuilder
    if (attributes != null && attributes.nonEmpty) {
      for (key <- attributes.keySet) {
        val value: String = attributes.get(key).get
        val encKey: String = URLEncoder.encode(key, TokenProviderImpl.DEFAULT_CHARSET)
        val encValue: String = URLEncoder.encode(value, TokenProviderImpl.DEFAULT_CHARSET)
        if (result.nonEmpty) {
          result.append(' ')
        }
        result.append(encKey)
        result.append('=')
        result.append(encValue)
      }
    }
    result.toString
  }

  @throws(classOf[UnsupportedEncodingException])
  private def stringToAttributes(string: String): TreeMap[String, String] = {
    var attributes: TreeMap[String, String] = null
    if (string != null && !("" == string)) {
      attributes = new TreeMap[String, String]
      val keyvalues: Array[String] = string.split(" ")
      for (keyvalue <- keyvalues) {
        val entry: Array[String] = keyvalue.split("=")
        val key: String = entry(0)
        val value: String = entry(1)
        attributes = attributes + (key -> URLDecoder.decode(value, TokenProviderImpl.DEFAULT_CHARSET))
      }
    }
    attributes
  }

  private def initKey() {
    val key: AnyRef = "[randomkey]"
    if (key != null && (TokenProviderImpl.RANDOM_KEY == key.toString)) {
      try {
        val keyGen: KeyGenerator = KeyGenerator.getInstance(TokenProviderImpl.ENCRYPTION_METHOD)
        keyGen.init(TokenProviderImpl.ENCRYPTION_METHOD_BYTES * 8)
        msecretKey = keyGen.generateKey
        mprivateKey = new Base64(0).encodeToString(msecretKey.getEncoded)
        startCiphers()
      }
      catch {
        case e: NoSuchAlgorithmException =>
      }
    }
    else if (key != null && !key.toString.trim.isEmpty && key.toString.length >= TokenProviderImpl.ENCRYPTION_METHOD_BYTES) {
      var bytes: Array[Byte] = new Array[Byte](TokenProviderImpl.ENCRYPTION_METHOD_BYTES)
      val keyBytes: Array[Byte] = key.toString.getBytes
      if (keyBytes.length == TokenProviderImpl.ENCRYPTION_METHOD_BYTES) {
        bytes = keyBytes
      }
      else {
        System.arraycopy(keyBytes, 0, bytes, 0, TokenProviderImpl.ENCRYPTION_METHOD_BYTES)
      }
      msecretKey = new SecretKeySpec(bytes, TokenProviderImpl.ENCRYPTION_METHOD)
      mprivateKey = new Base64(0).encodeToString(msecretKey.getEncoded)
      startCiphers()
    }
  }

  @throws(classOf[TokenProviderException])
  def generateToken(attributes: TreeMap[String, String]): String = {
    var a = attributes
    //initKey()
    try {
      if (attributes.contains(TokenProvider.NONCE)) {
        throw new TokenProviderException("Invalid token attributes provided. Parameter '" + TokenProvider.NONCE + "' is a preserved name")
      }
      if (attributes.contains(TokenProvider.TIMESTAMP)) {
        throw new TokenProviderException("Invalid token attributes provided. Parameter '" + TokenProvider.TIMESTAMP + "' is a preserved name")
      }
      if (attributes.contains(TokenProvider.TENANTID)) {
        throw new TokenProviderException("Invalid token attributes provided. Parameter '" + TokenProvider.TENANTID + "' is a preserved name")
      }
      val nonce: String = DigestUtils.md5Hex(System.nanoTime.toString)
      a = attributes + (TokenProvider.NONCE -> nonce)
      val timestamp: String = System.currentTimeMillis.toString
      a = attributes + (TokenProvider.TIMESTAMP -> timestamp)
      val signature: String = generateSignature(a)
      val token: String = signature + " " + attributesToString(a)
      val utf8: Array[Byte] = token.getBytes(TokenProviderImpl.DEFAULT_CHARSET)
      val enc: Array[Byte] = mencryptCipher.doFinal(utf8)
      val encryptedToken: String = StringUtils.newStringUtf8(Base64.encodeBase64(enc, false))
      mtokenStore.addToken(new Token(encryptedToken, null, System.currentTimeMillis))
      return encryptedToken
    }
    catch {
      case ex: Exception =>
        throw new RuntimeException("Error generating token", ex)
    }
    null
  }

  @throws(classOf[TokenProviderException])
  def verifyToken(encryptedToken: String): TreeMap[String, String] = {
    //initKey()
    try {
      if (!mtokenStore.hasToken(encryptedToken)) {
        throw new TokenProviderException("Token is invalid, token unkown")
      }
      val dec: Array[Byte] = Base64.decodeBase64(encryptedToken)
      val utf8: Array[Byte] = mdecryptCipher.doFinal(dec)
      val token: String = new String(utf8, TokenProviderImpl.DEFAULT_CHARSET)
      var signature: String = null
      var attributes: TreeMap[String, String] = null
      if (token.contains(" ")) {
        signature = token.substring(0, token.indexOf(" "))
        attributes = stringToAttributes(token.substring(token.indexOf(" ") + 1))
      }
      else {
        signature = token
      }
      if (!verifySignature(attributes, signature)) {
        throw new TokenProviderException("Token is invalid, signature mismatch")
      }
      attributes
    }
    catch {
      case e: Any =>
        throw new TokenProviderException(e.getMessage)
    }
  }

  @throws(classOf[TokenProviderException])
  @throws(classOf[InvalidTokenException])
  def updateToken(encryptedToken: String, newAttributes: TreeMap[String, String]): String = {
    var attributes: TreeMap[String, String] = verifyToken(encryptedToken)
    if (newAttributes != null && newAttributes.nonEmpty) {
      for (key <- newAttributes.keySet) {
        val value: String = newAttributes.get(key).get
        attributes = attributes + (key -> value)
      }
    }
    generateToken(attributes)
  }

  def invalidateToken(encryptedToken: String) {
    if (mtokenStore.hasToken(encryptedToken)) {
      val token: Token = mtokenStore.getToken(encryptedToken)
      mtokenStore.removeToken(token)
    }
  }

  def setMtokenStore(mtokenStore: TokenStorageProvider) {
    this.mtokenStore = mtokenStore
  }

  def getTokenFromRequest(request: HttpServletRequest): String = {
    TokenUtil.getTokenFromRequest(request)
  }
}
