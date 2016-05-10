package com.ktung.watchit.api.oauth;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.ktung.watchit.api.oauth.entities.OAuth2AccessToken;

public class SharedPreferencesAccessTokenStorage {
    public static final String PREF_KEY = "AccessTokenStorage";
    public static final String ACCESS_TOKEN_PREF_KEY = "OAuth2AccessToken";
    private final SharedPreferences preferences;

    public SharedPreferencesAccessTokenStorage(SharedPreferences preferences) {
        if (preferences == null) {
            throw new RuntimeException("SharedPreferences MUST NOT be null");
        }

        this.preferences = preferences;
    }

    public void storeAccessToken(OAuth2AccessToken accessToken) {
        preferences.edit().putString(ACCESS_TOKEN_PREF_KEY, new Gson().toJson(accessToken)).apply();
    }

    public OAuth2AccessToken getStoredAccessToken() {
        String json = preferences.getString(ACCESS_TOKEN_PREF_KEY, null);
        return new Gson().fromJson(json, OAuth2AccessToken.class);
    }

    public void removeAccessToken() {
        preferences.edit().remove(ACCESS_TOKEN_PREF_KEY).apply();
    }

    public boolean hasAccessToken() {
        return preferences.contains(ACCESS_TOKEN_PREF_KEY);
    }
}
