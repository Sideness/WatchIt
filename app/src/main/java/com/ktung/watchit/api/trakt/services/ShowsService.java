package com.ktung.watchit.api.trakt.services;

import com.ktung.watchit.api.trakt.entities.Ratings;
import com.ktung.watchit.api.trakt.enums.Extended;
import com.ktung.watchit.api.trakt.entities.Show;
import com.ktung.watchit.api.trakt.entities.ShowResponse;
import com.ktung.watchit.api.trakt.enums.Period;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface ShowsService {
    @GET("/shows/watched/{period}")
    Observable<List<ShowResponse>> mostWatched(
        @Path("period") Period period,
        @Query("extended") Extended extended
    );

    @GET("/shows/{id}")
    Observable<Show> summary(
        @Path("id") String showId,
        @Query("extended") Extended extended
    );

    @GET("/shows/{id}/progress/watched")
    Observable<ShowResponse> watchedProgress(
        @Path("id") String showId,
        @Query("hidden") Boolean hidden,
        @Query("specials") Boolean specials,
        @Query("extended") Extended extended
    );

    @GET("/shows/{id}/ratings")
    Observable<Ratings> ratings(@Path("id") String showId);
}
