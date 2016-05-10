package com.ktung.watchit.api.trakt.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.ktung.watchit.api.oauth.Credentials;
import com.ktung.watchit.api.oauth.AccessToken;
import com.ktung.watchit.api.oauth.SharedPreferencesAccessTokenStorage;
import com.ktung.watchit.api.oauth.TokenObserver;
import com.ktung.watchit.api.oauth.entities.OAuth2AccessToken;
import com.ktung.watchit.api.oauth.entities.RefreshTokenBody;
import com.ktung.watchit.api.oauth.entities.RequestTokenBody;
import com.ktung.watchit.api.trakt.Trakt;
import com.ktung.watchit.api.trakt.TraktConfig;

import java.util.Calendar;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TraktCredentials implements Credentials {
    private static SharedPreferencesAccessTokenStorage storage;
    private OAuth2AccessToken oauth2AccessToken;
    private TokenObserver tokenObserver;

    public TraktCredentials(Context context) {
        SharedPreferences pref = context.getSharedPreferences(
            SharedPreferencesAccessTokenStorage.PREF_KEY, Context.MODE_PRIVATE);
        storage = new SharedPreferencesAccessTokenStorage(pref);
        getValidAccessToken(null);
    }

    public void storeAccessToken(OAuth2AccessToken accessToken) {
        accessToken.expirationDate = Calendar.getInstance();
        accessToken.expirationDate.add(Calendar.SECOND, accessToken.expiresIn);

        storage.storeAccessToken(accessToken);
        getStoredAccessToken();
    }

    public OAuth2AccessToken getStoredAccessToken() {
        oauth2AccessToken =  storage.getStoredAccessToken();
        return oauth2AccessToken;
    }

    public void removeAccessToken() {
        storage.removeAccessToken();
        oauth2AccessToken = null;
    }

    public boolean hasAccessToken() {
        return storage.hasAccessToken();
    }

    public void getValidAccessToken(String authorizationCode) {
        if (hasAccessToken()) {
            oauth2AccessToken = getStoredAccessToken();

            if (Calendar.getInstance().after(oauth2AccessToken.expirationDate)) {
                if (tokenObserver == null) {
                    tokenObserver = new TokenObserver(this);
                }
                refreshTokenToken(tokenObserver);
            }
        } else if (authorizationCode != null && tokenObserver != null) {
            generateNewToken(authorizationCode);
        }
    }

    private void generateNewToken(String authorizationCode) {
        RequestTokenBody requestTokenBody = new RequestTokenBody(
            authorizationCode,
            TraktConfig.CLIENT_ID,
            TraktConfig.CLIENT_SECRET,
            TraktConfig.REDIRECT_URI,
            "authorization_code"
        );

        Trakt.getInstance().oauth().getToken(requestTokenBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tokenObserver);
    }

    private void refreshTokenToken(TokenObserver tokenObserver) {
        RefreshTokenBody refreshTokenBody = new RefreshTokenBody(
            oauth2AccessToken.refreshToken,
            TraktConfig.CLIENT_ID,
            TraktConfig.CLIENT_SECRET,
            TraktConfig.REDIRECT_URI,
            "refresh_token"
        );

        Trakt.getInstance().oauth().getToken(refreshTokenBody)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(tokenObserver);
    }

    public AccessToken getAccessToken() {
        if (oauth2AccessToken == null) {
            getStoredAccessToken();
        }

        return AccessToken.from(oauth2AccessToken);
    }

    public void setTokenObserver(TokenObserver tokenObserver) {
        this.tokenObserver = tokenObserver;
    }
}
