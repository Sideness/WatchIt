package com.ktung.watchit.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ktung.watchit.R;
import com.ktung.watchit.api.oauth.Credentials;
import com.ktung.watchit.api.oauth.TokenObserver;
import com.ktung.watchit.api.oauth.entities.OAuth2AccessToken;
import com.ktung.watchit.api.trakt.Trakt;
import com.ktung.watchit.api.trakt.TraktConfig;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ConnectTraktActivity extends AppCompatActivity {
    private Trakt trakt;

    @Bind(R.id.webView)
    protected WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_connect_trakt);
        ButterKnife.bind(this);
        trakt = Trakt.getInstance();
        CookieManager.getInstance().removeAllCookie();

        webView.loadUrl(TraktConfig.AUTH_URL);
        webView.setWebViewClient(webViewClient);
    }

    protected WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            if (url != null && url.startsWith(TraktConfig.REDIRECT_URI)) {
                String code = Uri.parse(url).getQueryParameter("code");
                trakt.getCredentials().setTokenObserver(new TraktTokenObserver(trakt.getCredentials()));
                trakt.login(code);
                return true;
            }

            return false;
        }
    };

    class TraktTokenObserver extends TokenObserver {
        public TraktTokenObserver(Credentials credentials) {
            super(credentials);
        }

        @Override
        public void onNext(OAuth2AccessToken oAuth2AccessToken) {
            super.onNext(oAuth2AccessToken);

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }
}
