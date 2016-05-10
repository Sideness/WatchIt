package com.ktung.watchit.api.trakt.services;

import com.ktung.watchit.api.trakt.entities.ShowResponse;
import com.ktung.watchit.api.trakt.entities.SyncItems;
import com.ktung.watchit.api.trakt.entities.SyncResponse;
import com.ktung.watchit.api.trakt.entities.WatchlistShow;
import com.ktung.watchit.api.trakt.enums.Extended;
import com.ktung.watchit.api.trakt.enums.SyncTypes;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface SyncService {
    @GET("/sync/watched/shows")
    Observable<List<ShowResponse>> watchedShows(@Query("extended") Extended extended);

    @GET("/sync/history/{type}")
    Observable<List<ShowResponse>> history(
        @Path("type") SyncTypes type,
        @Query("extended") Extended extended
    );

    @GET("/sync/history/{type}/{id}")
    Observable<List<ShowResponse>> history(
        @Path("type") SyncTypes type,
        @Path("id") int id,
        @Query("extended") Extended extended
    );

    @POST("/sync/history")
    Observable<SyncResponse> addToWatchedHistory(@Body SyncItems items);

    @POST("/sync/history/remove")
    Observable<SyncResponse> removeItemsFromHistory(@Body SyncItems items);

    @GET("/sync/watchlist/shows")
    Observable<List<WatchlistShow>> watchlistShows(@Query("extended") Extended extended);

    @POST("/sync/watchlist")
    Observable<SyncResponse> addToWatchlist(@Body SyncItems items);

    @POST("/sync/watchlist/remove")
    Observable<SyncResponse> removeItemsFromWatchlist(@Body SyncItems items);
}
