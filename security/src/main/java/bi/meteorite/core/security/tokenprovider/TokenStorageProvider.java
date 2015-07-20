package bi.meteorite.core.security.tokenprovider;

public interface TokenStorageProvider {
    /**
     * Adds a new token to the store.
     * 
     * @param token
     *        The token to add
     */
    void addToken(Token token);

    /**
     * Updates all properties of the token identified by the token key (returned by getToken()).
     * If no token exists with this identifier, nothing happens.
     * 
     * @param token
     */
    void updateToken(Token token);

    /**
     * Returns a token from the store.
     * 
     * @param token
     * @return
     */
    Token getToken(String token);

    /**
     * Returns if this store holds the given token.
     * 
     * @param token
     *        The token to verify
     * @return true if this store holds this token, false otherwise
     */
    boolean hasToken(String token);

    /**
     * Removes a token from the store.
     * 
     * @param token
     *        The token to remove.
     */
    void removeToken(Token token);
}