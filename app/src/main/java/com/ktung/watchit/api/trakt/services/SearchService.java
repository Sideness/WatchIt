package com.ktung.watchit.api.trakt.services;

import com.ktung.watchit.api.trakt.entities.SearchResult;
import com.ktung.watchit.api.trakt.enums.Type;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface SearchService {
    @GET("/search")
    Observable<List<SearchResult>> textQuery(
        @Query("query") String query,
        @Query("type") Type type,
        @Query("year") Integer year,
        @Query("page") Integer page,
        @Query("limit") Integer limit
    );
}
