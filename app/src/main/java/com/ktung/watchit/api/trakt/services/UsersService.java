package com.ktung.watchit.api.trakt.services;

import com.ktung.watchit.api.trakt.entities.Settings;

import retrofit.http.GET;
import rx.Observable;

public interface UsersService {
    @GET("/users/settings")
    Observable<Settings> settings();
}

