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

package bi.meteorite.core.security.tokenprovider;

import bi.meteorite.core.api.security.tokenprovider.InvalidTokenException;
import bi.meteorite.core.api.security.tokenprovider.TokenProvider;
import bi.meteorite.core.api.security.tokenprovider.TokenProviderException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Dictionary;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

/*import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.log.LogService;*/

/**
 * Created by bugg on 20/07/15.
 */
public class TokenProviderImpl implements TokenProvider {

  public static final String AUTHORIZATION_HEADER_AMDATU = TokenUtil.AUTHORIZATION_HEADER_AMDATU;

  // Service dependencies
  //private volatile LogService m_logService;
  private volatile TokenStorageProvider mtokenStore;

  // Encryption and decryption key and ciphers
  private static final String ENCRYPTION_METHOD = "AES";
  private static final int ENCRYPTION_METHOD_BYTES = 128 / 8; // We use AES-128 (128 bits = 16 bytes)
  private static final String DEFAULT_CHARSET = "UTF-8";
  private static final String SECRET_KEY_PROPERTY = "secretkey";
  private static final String RANDOM_KEY = "[randomkey]";

  // The secret key
  private volatile SecretKey msecretKey;
  private volatile String mprivateKey;
  private volatile Cipher mencryptCipher;
  private volatile Cipher mdecryptCipher;

  @SuppressWarnings("rawtypes")
  public void updated(final Dictionary properties) throws Exception {
    if (properties == null) {

/**
 * AMDATUSEC-7: TokenProviderImpl#updated throws NPE when properties null
 *
 * The Dependency Manager will take care of unregistering the service when the configuration is removed.
 */
      return;
    }

    Object key = properties.get(SECRET_KEY_PROPERTY);
    if (key != null && RANDOM_KEY.equals(key.toString())) {
      // The value [randomkey] is intended for a single server setup. In this case we generate a
      // random key.
      try {
        KeyGenerator keyGen = KeyGenerator.getInstance(ENCRYPTION_METHOD);
        keyGen.init(ENCRYPTION_METHOD_BYTES * 8);
        msecretKey = keyGen.generateKey();
        mprivateKey = new Base64(0).encodeToString(msecretKey.getEncoded());
        startCiphers();
      } catch (NoSuchAlgorithmException e) {
        //throw new ConfigurationException(SECRET_KEY_PROPERTY, "Could not generate random key", e);
      }
    } else if (key != null && !key.toString().trim().isEmpty()
               && key.toString().length() >= ENCRYPTION_METHOD_BYTES) {
      byte[] bytes = new byte[ENCRYPTION_METHOD_BYTES];
      byte[] keyBytes = key.toString().getBytes();
      if (keyBytes.length == ENCRYPTION_METHOD_BYTES) {
        bytes = keyBytes;
      } else {
        // Chop off first ENCRYPTION_METHOD_BYTES bytes
        for (int i = 0; i < ENCRYPTION_METHOD_BYTES; i++) {
          bytes[i] = keyBytes[i];
        }
      }

      msecretKey = new SecretKeySpec(bytes, ENCRYPTION_METHOD);
      mprivateKey = new Base64(0).encodeToString(msecretKey.getEncoded());
      startCiphers();
    } else {
    /*  throw new ConfigurationException(SECRET_KEY_PROPERTY, "An invalid secret key has been entered "
                                                            + "in the configuration of '" + TokenProvider.PID
                                                            + "'. The secret key must either equal '[randomkey]' "
                                                            + "or contain at least 16 characters. The token provider service cannot be started "
                                                            + "without a valid secret key.");*/
    }
  }

  private void startCiphers() {
    try {
      mencryptCipher = Cipher.getInstance(ENCRYPTION_METHOD);
      mencryptCipher.init(Cipher.ENCRYPT_MODE, msecretKey);
      mdecryptCipher = Cipher.getInstance(ENCRYPTION_METHOD);
      mdecryptCipher.init(Cipher.DECRYPT_MODE, msecretKey);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
      throw new RuntimeException(e);
    }
  }

  private String generateSignature(final SortedMap<String, String> attributes) throws TokenProviderException {
    String signvalue = mprivateKey;
    if (attributes != null && attributes.size() > 0) {
      try {
        signvalue += attributesToString(attributes);
      } catch (UnsupportedEncodingException e) {
        throw new TokenProviderException(e);
      }
    }
    String signature = DigestUtils.md5Hex(signvalue);
    return signature;
  }

  private boolean verifySignature(final SortedMap<String, String> attributes, final String signature)
      throws TokenProviderException {
    String verifySignature = generateSignature(attributes);
    return signature.equals(verifySignature);
  }

  // Converts a map of attribute keys and values into a single String representation, using
  // URL encoding
  public String attributesToString(final SortedMap<String, String> attributes) throws UnsupportedEncodingException {
    StringBuffer result = new StringBuffer();
    if (attributes != null && attributes.size() > 0) {
      for (String key : attributes.keySet()) {
        String value = attributes.get(key);
        String encKey = URLEncoder.encode(key, DEFAULT_CHARSET);
        String encValue = URLEncoder.encode(value, DEFAULT_CHARSET);
        if (result.length() > 0) {
          result.append(' ');
        }
        result.append(encKey);
        result.append('=');
        result.append(encValue);
      }
    }
    return result.toString();
  }

  // Converts a single String into a map of attribute keys and values using URL decoding
  public SortedMap<String, String> stringToAttributes(final String string) throws UnsupportedEncodingException {
    SortedMap<String, String> attributes = null;
    if (string != null && !"".equals(string)) {
      attributes = new TreeMap<String, String>();
      String[] keyvalues = string.split(" "); // space is the attribute separator
      for (String keyvalue : keyvalues) {
        String[] entry = keyvalue.split("=");
        String key = entry[0];
        String value = entry[1];
        attributes.put(key, URLDecoder.decode(value, DEFAULT_CHARSET));
      }
    }
    return attributes;
  }

  public String generateToken(final SortedMap<String, String> attributes) throws TokenProviderException {

    Object key = "[randomkey]";
    if (key != null && RANDOM_KEY.equals(key.toString())) {
      // The value [randomkey] is intended for a single server setup. In this case we generate a
      // random key.
      try {
        KeyGenerator keyGen = KeyGenerator.getInstance(ENCRYPTION_METHOD);
        keyGen.init(ENCRYPTION_METHOD_BYTES * 8);
        msecretKey = keyGen.generateKey();
        mprivateKey = new Base64(0).encodeToString(msecretKey.getEncoded());
        startCiphers();
      } catch (NoSuchAlgorithmException e) {
        //throw new ConfigurationException(SECRET_KEY_PROPERTY, "Could not generate random key", e);
      }
    } else if (key != null && !key.toString().trim().isEmpty()
               && key.toString().length() >= ENCRYPTION_METHOD_BYTES) {
      byte[] bytes = new byte[ENCRYPTION_METHOD_BYTES];
      byte[] keyBytes = key.toString().getBytes();
      if (keyBytes.length == ENCRYPTION_METHOD_BYTES) {
        bytes = keyBytes;
      } else {
        // Chop off first ENCRYPTION_METHOD_BYTES bytes
        for (int i = 0; i < ENCRYPTION_METHOD_BYTES; i++) {
          bytes[i] = keyBytes[i];
        }
      }

      msecretKey = new SecretKeySpec(bytes, ENCRYPTION_METHOD);
      mprivateKey = new Base64(0).encodeToString(msecretKey.getEncoded());
      startCiphers();

    }


    try {
      if (attributes.containsKey(NONCE)) {
        throw new TokenProviderException("Invalid token attributes provided. Parameter '" + NONCE
                                         + "' is a preserved name");
      }
      if (attributes.containsKey(TIMESTAMP)) {
        throw new TokenProviderException("Invalid token attributes provided. Parameter '" + TIMESTAMP
                                         + "' is a preserved name");
      }
      if (attributes.containsKey(TENANTID)) {
        throw new TokenProviderException("Invalid token attributes provided. Parameter '" + TENANTID
                                         + "' is a preserved name");
      }

      // Add nonce and timestamp attributes
      String nonce = DigestUtils.md5Hex(new Long(System.nanoTime()).toString());
      attributes.put(NONCE, nonce);
      String timestamp = new Long(System.currentTimeMillis()).toString();
      attributes.put(TIMESTAMP, timestamp);

      // First create the unencrypted token
      String signature = generateSignature(attributes);
      String token = signature + " " + attributesToString(attributes);

      // Encode the token using UTF-8
      byte[] utf8 = token.getBytes(DEFAULT_CHARSET);

      // Encrypt it
      byte[] enc = mencryptCipher.doFinal(utf8);

      // Encode to base64
      String encryptedToken = StringUtils.newStringUtf8(Base64.encodeBase64(enc, false));

      // Store the encrypted token
      // NB: our token secret is null, since there is no party with which we can share the
      // token secret
      mtokenStore.addToken(new Token(encryptedToken, null, System.currentTimeMillis()));

      return encryptedToken;
    } catch (BadPaddingException e) {
      // m_logService.log(LogService.LOG_ERROR, "Could not encrypt string", e);
    } catch (IllegalBlockSizeException e) {
      //m_logService.log(LogService.LOG_ERROR, "Could not encrypt string", e);
    } catch (UnsupportedEncodingException e) {
      //m_logService.log(LogService.LOG_ERROR, "Could not encrypt string", e);
    } catch (Exception ex) {
      //m_logService.log(LogService.LOG_ERROR, "Error generating token", ex);
      throw new RuntimeException("Error generating token", ex);
    }
    return null;
  }

  public SortedMap<String, String> verifyToken(final String encryptedToken) throws TokenProviderException {
    try {
      // First verify that this token was generated by us
      if (!mtokenStore.hasToken(encryptedToken)) {
        //throw new InvalidTokenException("Token is invalid, token unknown");
        throw new TokenProviderException("Token is invalid, token unkown");
      }

      // Decode base64 to get bytes
      byte[] dec = Base64.decodeBase64(encryptedToken);

      // Decrypt
      byte[] utf8 = mdecryptCipher.doFinal(dec);

      // Decode using UTF-8
      String token = new String(utf8, DEFAULT_CHARSET);

      // Now this token consists of signature + token attributes
      String signature;
      SortedMap<String, String> attributes = null;
      if (token.indexOf(" ") != -1) {
        signature = token.substring(0, token.indexOf(" "));
        attributes = stringToAttributes(token.substring(token.indexOf(" ") + 1));
      } else {
        signature = token;
      }

      // Now verify if this signature is valid
      if (!verifySignature(attributes, signature)) {
        //throw new InvalidTokenException("Token is invalid, signature mismatch");
        throw new TokenProviderException("Token is invalid, signature mismatch");
      }
      return attributes;
    } catch (BadPaddingException e) {
      throw new TokenProviderException(e);
    } catch (IllegalBlockSizeException e) {
      throw new TokenProviderException(e);
    } catch (UnsupportedEncodingException e) {
      throw new TokenProviderException(e);
    }
  }

  public String updateToken(final String encryptedToken, final SortedMap<String, String> newAttributes)
      throws TokenProviderException, InvalidTokenException {
    // First validate that the token is valid and retrieve the original token attributes
    SortedMap<String, String> attributes = verifyToken(encryptedToken);

    // Now update the token attributes with the new ones
    if (newAttributes != null && newAttributes.size() > 0) {
      for (String key : newAttributes.keySet()) {
        String value = newAttributes.get(key);
        attributes.put(key, value);
      }
    }

    // Generate a new token
    return generateToken(attributes);
  }

  public void invalidateToken(final String encryptedToken) {
    if (mtokenStore.hasToken(encryptedToken)) {
      Token token = mtokenStore.getToken(encryptedToken);
      mtokenStore.removeToken(token);
    }
  }

  public void setMtokenStore(TokenStorageProvider mtokenStore) {
    this.mtokenStore = mtokenStore;
  }

  public String getTokenFromRequest(final HttpServletRequest request){
    return TokenUtil.getTokenFromRequest(request);
  }
}
