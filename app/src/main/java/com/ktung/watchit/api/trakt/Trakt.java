package com.ktung.watchit.api.trakt;

import com.ktung.watchit.api.trakt.network.TraktCredentials;
import com.ktung.watchit.api.trakt.network.TraktHttpClient;
import com.ktung.watchit.api.trakt.services.CalendarsService;
import com.ktung.watchit.api.trakt.services.CheckinService;
import com.ktung.watchit.api.trakt.services.EpisodesService;
import com.ktung.watchit.api.trakt.services.MoviesService;
import com.ktung.watchit.api.trakt.services.OAuthService;
import com.ktung.watchit.api.trakt.services.SearchService;
import com.ktung.watchit.api.trakt.services.SeasonsService;
import com.ktung.watchit.api.trakt.services.ShowsService;
import com.ktung.watchit.api.trakt.services.SyncService;
import com.ktung.watchit.api.trakt.services.UsersService;

public class Trakt {
    private static Trakt _instance;
    private TraktCredentials credentials;

    private Trakt() {
    }

    public static synchronized Trakt getInstance() {
        if (_instance == null) {
            _instance = new Trakt();
        }

        return _instance;
    }

    public void login(String authorizationCode) {
        if (credentials == null) {
            throw new RuntimeException("Credentials MUST NOT be null");
        }
        credentials.getValidAccessToken(authorizationCode);
    }

    public void logout() {
        if (credentials == null) {
            throw new RuntimeException("Credentials MUST NOT be null");
        }

        credentials.removeAccessToken();
    }

    public OAuthService oauth() {
        return TraktHttpClient.createNonAuthService(OAuthService.class);
    }

    public CalendarsService calendars() {
        if (credentials == null) {
            throw new RuntimeException("Credentials MUST NOT be null");
        }

        return TraktHttpClient.createAuthService(CalendarsService.class, credentials.getAccessToken());
    }

    public CheckinService checkin() {
        if (credentials == null) {
            throw new RuntimeException("Credentials MUST NOT be null");
        }

        return TraktHttpClient.createAuthService(CheckinService.class, credentials.getAccessToken());
    }

    public MoviesService movies() {
        if (credentials == null) {
            throw new RuntimeException("Credentials MUST NOT be null");
        }

        return TraktHttpClient.createAuthService(MoviesService.class, credentials.getAccessToken());
    }

    public SearchService search() {
        if (credentials == null) {
            throw new RuntimeException("Credentials MUST NOT be null");
        }

        return TraktHttpClient.createAuthService(SearchService.class, credentials.getAccessToken());
    }

    public ShowsService shows() {
        if (credentials == null) {
            throw new RuntimeException("Credentials MUST NOT be null");
        }

        return TraktHttpClient.createAuthService(ShowsService.class, credentials.getAccessToken());
    }

    public SeasonsService seasons() {
        if (credentials == null) {
            throw new RuntimeException("Credentials MUST NOT be null");
        }

        return TraktHttpClient.createAuthService(SeasonsService.class, credentials.getAccessToken());    }


    public EpisodesService episodes() {
        if (credentials == null) {
            throw new RuntimeException("Credentials MUST NOT be null");
        }

        return TraktHttpClient.createAuthService(EpisodesService.class, credentials.getAccessToken());
    }

    public SyncService sync() {
        if (credentials == null) {
            throw new RuntimeException("Credentials MUST NOT be null");
        }

        return TraktHttpClient.createAuthService(SyncService.class, credentials.getAccessToken());
    }

    public UsersService users() {
        if (credentials == null) {
            throw new RuntimeException("Credentials MUST NOT be null");
        }

        return TraktHttpClient.createAuthService(UsersService.class, credentials.getAccessToken());
    }

    public TraktCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(TraktCredentials credentials) {
        this.credentials = credentials;
    }

    public boolean isLogged() {
        return credentials.hasAccessToken();
    }
}
