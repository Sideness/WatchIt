package com.ktung.watchit.api.trakt.network;

import com.ktung.watchit.api.oauth.AccessToken;
import com.ktung.watchit.api.oauth.HttpClient;
import com.ktung.watchit.api.trakt.TraktConfig;

import retrofit.converter.GsonConverter;

public final class TraktHttpClient extends HttpClient {

    public static <S> S createAuthService(Class<S> serviceClass, AccessToken accessToken) {
        builder.setConverter(new GsonConverter(TraktGsonConverter.getGsonBuilder().create()));
        builder.setRequestInterceptor(jsonInterceptor);
        builder.setRequestInterceptor(request -> {
            request.addHeader("trakt-api-version", TraktConfig.API_VERSION);
            request.addHeader("trakt-api-key", TraktConfig.CLIENT_ID);
            request.addHeader("Authorization", accessToken.getAuthorization());
        });

        return createService(serviceClass);
    }

    public static <S> S createNonAuthService(Class<S> serviceClass) {
        builder.setRequestInterceptor(jsonInterceptor);
        return createService(serviceClass);
    }
}
