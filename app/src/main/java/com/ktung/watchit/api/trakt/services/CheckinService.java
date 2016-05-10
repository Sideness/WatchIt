package com.ktung.watchit.api.trakt.services;

import com.ktung.watchit.api.trakt.entities.EpisodeCheckinRequest;
import com.ktung.watchit.api.trakt.entities.EpisodeCheckinResponse;
import com.ktung.watchit.api.trakt.entities.MovieCheckinRequest;
import com.ktung.watchit.api.trakt.entities.MovieCheckinResponse;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.POST;
import rx.Observable;

public interface CheckinService {
    @POST("/checkin")
    Observable<MovieCheckinResponse> checkin(
        @Body MovieCheckinRequest movieCheckin
    );

    @POST("/checkin")
    Observable<EpisodeCheckinResponse> checkin(
        @Body EpisodeCheckinRequest episodeCheckin
    );

    @DELETE("/checkin")
    Observable<Response> deleteActiveCheckin();
}
