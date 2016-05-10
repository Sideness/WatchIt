package com.ktung.watchit.api.oauth.entities;


public abstract class TokenBody {
    protected String client_id;
    protected String client_secret;
    protected String redirect_uri;
    protected String grant_type;
}
