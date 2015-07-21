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

import bi.meteorite.core.api.cache.CacheManagerService;

/**
 * Created by bugg on 20/07/15.
 */
public class CacheManagerTokenStore implements TokenStorageProvider {

  private CacheManagerService cacheManager;

  @Override
  public void addToken(Token token) {
    cacheManager.getCache("tokens", this.getClass().getClassLoader()).put(token.getToken(), token);
  }

  @Override
  public void updateToken(Token token) {

  }

  @Override
  public Token getToken(String token) {
    return (Token) cacheManager.getCache("tokens").get(token);
  }

  @Override
  public boolean hasToken(String token) {
    return cacheManager.getCache("tokens").get(token) != null;
  }

  @Override
  public void removeToken(Token token) {

  }

  public void setCacheManagerService(CacheManagerService hazel) {
    this.cacheManager = hazel;
  }
}
