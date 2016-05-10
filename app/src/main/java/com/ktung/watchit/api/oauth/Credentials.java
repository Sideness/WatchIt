package com.ktung.watchit.api.oauth;

import com.ktung.watchit.api.oauth.entities.OAuth2AccessToken;

public interface Credentials {
    void storeAccessToken(OAuth2AccessToken accessToken);

    void removeAccessToken();

    boolean hasAccessToken();

    void getValidAccessToken(String authorizationCode);
}

