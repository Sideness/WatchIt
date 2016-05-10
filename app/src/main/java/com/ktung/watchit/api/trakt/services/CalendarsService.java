package com.ktung.watchit.api.trakt.services;

import com.ktung.watchit.api.trakt.entities.CalendarMovieEntry;
import com.ktung.watchit.api.trakt.entities.CalendarShowEntry;
import com.ktung.watchit.api.trakt.enums.Extended;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface CalendarsService {
    @GET("/calendars/my/shows/{start_date}/{days}")
    Observable<List<CalendarShowEntry>> myShows(
        @Path("start_date") String startDate,
        @Path("days") int days,
        @Query("extended") Extended extended
    );

    @GET("/calendars/my/shows/new/{start_date}/{days}")
    Observable<List<CalendarShowEntry>> myNewShows(
        @Path("start_date") String startDate,
        @Path("days") int days
    );

    @GET("/calendars/my/shows/premieres/{start_date}/{days}")
    Observable<List<CalendarShowEntry>> mySeasonPremieres(
        @Path("start_date") String startDate,
        @Path("days") int days
    );

    @GET("/calendars/my/movies/{start_date}/{days}")
    Observable<List<CalendarMovieEntry>> myMovies(
        @Path("start_date") String startDate,
        @Path("days") int days
    );

    @GET("/calendars/all/shows/{start_date}/{days}")
    Observable<List<CalendarShowEntry>> shows(
        @Path("start_date") String startDate,
        @Path("days") int days
    );

    @GET("/calendars/all/shows/new/{start_date}/{days}")
    Observable<List<CalendarShowEntry>> newShows(
        @Path("start_date") String startDate,
        @Path("days") int days
    );

    @GET("/calendars/all/shows/premieres/{start_date}/{days}")
    Observable<List<CalendarShowEntry>> seasonPremieres(
        @Path("start_date") String startDate,
        @Path("days") int days
    );

    @GET("/calendars/all/movies/{start_date}/{days}")
    Observable<List<CalendarMovieEntry>> movies(
        @Path("start_date") String startDate,
        @Path("days") int days
    );
}
