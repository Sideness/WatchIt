package com.ktung.watchit.api.oauth.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

/**
 * https://tools.ietf.org/html/rfc6749#section-5.1
 */
public class OAuth2AccessToken {
    @SerializedName("access_token")
    @Expose
    public String accessToken;

    @SerializedName("token_type")
    @Expose
    public String tokenType;

    @SerializedName("expires_in")
    @Expose
    public Integer expiresIn;

    @SerializedName("refresh_token")
    @Expose
    public String refreshToken;

    @SerializedName("scope")
    @Expose
    public String Scope;

    @SerializedName("wi_expiration_date")
    @Expose
    public Calendar expirationDate;
}

