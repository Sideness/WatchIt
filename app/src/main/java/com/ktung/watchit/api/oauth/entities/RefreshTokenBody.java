package com.ktung.watchit.api.oauth.entities;

public class RefreshTokenBody extends TokenBody {
    private String refresh_token;

    public RefreshTokenBody(String refresh_token, String client_id, String client_secret, String redirect_uri, String grant_type) {
        this.refresh_token = refresh_token;
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.redirect_uri = redirect_uri;
        this.grant_type = grant_type;
    }
}
