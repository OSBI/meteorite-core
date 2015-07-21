package bi.meteorite.core.security.tokenprovider;

import bi.meteorite.core.api.cache.CacheManagerService;

/**
 * Created by bugg on 20/07/15.
 */
public class HazelcastTokenStore implements TokenStorageProvider{

  private CacheManagerService hazel;

  @Override
  public void addToken(Token token) {
    hazel.getCache("tokens", this.getClass().getClassLoader()).put(token.getToken(), token);
  }

  @Override
  public void updateToken(Token token) {

  }

  @Override
  public Token getToken(String token) {
    return (Token) hazel.getCache("tokens").get(token);
  }

  @Override
  public boolean hasToken(String token) {
    return hazel.getCache("tokens").get(token) != null;
  }

  @Override
  public void removeToken(Token token) {

  }

  public void setHazel(CacheManagerService hazel) {
    this.hazel = hazel;
  }
}
