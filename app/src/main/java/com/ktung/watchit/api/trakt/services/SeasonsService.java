package com.ktung.watchit.api.trakt.services;

import com.ktung.watchit.api.trakt.entities.Episode;
import com.ktung.watchit.api.trakt.entities.Season;
import com.ktung.watchit.api.trakt.enums.Extended;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface SeasonsService {
    @GET("/shows/{id}/seasons")
    Observable<List<Season>> summary(
        @Path("id") String showId,
        @Query("extended") Extended extended
    );

    @GET("/shows/{id}/seasons/{season}")
    Observable<List<Episode>> season(
        @Path("id") String showId,
        @Path("season") int season,
        @Query("extended") Extended extended
    );
}
