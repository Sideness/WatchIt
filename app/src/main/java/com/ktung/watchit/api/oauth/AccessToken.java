package com.ktung.watchit.api.oauth;

import com.ktung.watchit.api.oauth.entities.OAuth2AccessToken;

public class AccessToken {
    private String accessToken;
    private String tokenType;

    public AccessToken(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        // OAuthService requires uppercase Authorization HTTP header value for token type
        if (!Character.isUpperCase(tokenType.charAt(0))) {
            tokenType =
                Character
                    .toString(tokenType.charAt(0))
                    .toUpperCase() + tokenType.substring(1);
        }

        return tokenType;
    }

    public String getAuthorization() {
        return getTokenType() +" "+ getAccessToken();
    }

    public static AccessToken from(OAuth2AccessToken oauth2AccessToken) {
        if (oauth2AccessToken != null && oauth2AccessToken.accessToken != null && oauth2AccessToken.tokenType != null) {
            return new AccessToken(oauth2AccessToken.accessToken, oauth2AccessToken.tokenType);
        }

        return null;
    }
}
