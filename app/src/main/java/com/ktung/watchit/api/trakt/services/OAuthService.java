package com.ktung.watchit.api.trakt.services;


import com.ktung.watchit.api.oauth.entities.RefreshTokenBody;
import com.ktung.watchit.api.oauth.entities.RequestTokenBody;
import com.ktung.watchit.api.oauth.entities.OAuth2AccessToken;

import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

public interface OAuthService {
    @POST("/oauth/token")
    Observable<OAuth2AccessToken> getToken(@Body RequestTokenBody requestTokenBody);

    @POST("/oauth/token")
    Observable<OAuth2AccessToken> getToken(@Body RefreshTokenBody refreshTokenBody);
}
