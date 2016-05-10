package com.ktung.watchit.api.trakt.services;

import com.ktung.watchit.api.trakt.entities.Movie;
import com.ktung.watchit.api.trakt.entities.MovieResponse;
import com.ktung.watchit.api.trakt.entities.Show;
import com.ktung.watchit.api.trakt.enums.Extended;
import com.ktung.watchit.api.trakt.enums.Period;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface MoviesService {
    @GET("/movies/watched/{period}")
    Observable<List<MovieResponse>> mostWatched(
        @Path("period") Period period,
        @Query("extended") Extended extended
    );

    @GET("/movies/{id}")
    Observable<Movie> summary(
        @Path("id") String movieId,
        @Query("extended") Extended extended
    );
}
