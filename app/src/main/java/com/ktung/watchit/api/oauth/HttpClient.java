package com.ktung.watchit.api.oauth;

import com.ktung.watchit.api.trakt.TraktConfig;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public abstract class HttpClient {
    protected static final RequestInterceptor jsonInterceptor =
        request -> request.addHeader("Content-Type", "application/json");

    protected static RestAdapter.Builder builder =
        new RestAdapter.Builder()
            .setEndpoint(TraktConfig.BASE_URL)
            .setClient(new OkClient(new OkHttpClient()));
//            .setLogLevel(RestAdapter.LogLevel.FULL);

    protected static <S> S createService(Class<S> serviceClass) {
        RestAdapter adapter = builder
                .build();
        return adapter.create(serviceClass);
    }
}
