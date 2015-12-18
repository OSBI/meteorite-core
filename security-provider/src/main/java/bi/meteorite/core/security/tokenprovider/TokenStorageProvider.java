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

/**
 * Token Storage Provider
 */
public interface TokenStorageProvider {
  /**
   * Adds a new token to the store.
   *
   * @param token The token to add
   */
  void addToken(Token token);

  /**
   * Updates all properties of the token identified by the token key (returned by getToken()). If no token exists with
   * this identifier, nothing happens.
   *
   * @param token the token to update.
   */
  void updateToken(Token token);

  /**
   * Returns a token from the store.
   *
   * @param token the token to return from the store.
   * @return the token object.
   */
  Token getToken(String token);

  /**
   * Returns if this store holds the given token.
   *
   * @param token The token to verify
   * @return true if this store holds this token, false otherwise
   */
  boolean hasToken(String token);

  /**
   * Removes a token from the store.
   *
   * @param token The token to remove.
   */
  void removeToken(Token token);
}
