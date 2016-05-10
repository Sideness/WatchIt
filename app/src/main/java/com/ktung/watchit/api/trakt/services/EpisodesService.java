package com.ktung.watchit.api.trakt.services;

import com.ktung.watchit.api.trakt.entities.Episode;
import com.ktung.watchit.api.trakt.enums.Extended;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface EpisodesService {
    @GET("/shows/{id}/seasons/{season}/episodes/{episode}")
    Observable<Episode> summary(
        @Path("id") String showId,
        @Path("season") String season,
        @Path("episode") String episode,
        @Query("extended") Extended extended
    );
}
