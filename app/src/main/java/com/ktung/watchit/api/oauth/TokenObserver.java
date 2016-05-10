package com.ktung.watchit.api.oauth;

import com.ktung.watchit.api.oauth.entities.OAuth2AccessToken;

import rx.Observer;

public class TokenObserver implements Observer<OAuth2AccessToken> {
    private Credentials credentials;

    public TokenObserver(Credentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(OAuth2AccessToken oAuth2AccessToken) {
        credentials.storeAccessToken(oAuth2AccessToken);
    }
}