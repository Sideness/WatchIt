package com.ktung.watchit.ui.base;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ktung.watchit.R;
import com.ktung.watchit.api.trakt.Trakt;
import com.ktung.watchit.api.trakt.entities.Settings;
import com.ktung.watchit.api.trakt.network.TraktCredentials;
import com.ktung.watchit.ui.ConnectTraktActivity;
import com.ktung.watchit.ui.MainActivity;
import com.ktung.watchit.ui.movies.DiscoverActivity;
import com.ktung.watchit.ui.settings.SettingsActivity;
import com.ktung.watchit.util.BaseObserver;
import com.ktung.watchit.util.RoundedTransformation;
import com.ktung.watchit.util.Utils;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.io.File;

import rx.android.schedulers.AndroidSchedulers;

public abstract class BaseDrawerActivity extends RxAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected Toolbar toolbar;
    protected DrawerLayout drawer;
    protected NavigationView navigationView;
    protected ImageView drawerHeader;
    protected ImageView drawerAvatar;
    protected TextView drawerUsername;

    protected Trakt trakt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.handleTheme(getApplicationContext(), this, false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_drawer_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerHeader = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.drawerHeader);
        drawerAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.drawerAvatar);
        drawerUsername = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawerUsername);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        trakt = Trakt.getInstance();
        trakt.setCredentials(new TraktCredentials(getApplicationContext()));
        if (!trakt.isLogged()) {
            launchConnectActivity();
        } else {
            customDrawerHeader();
        }
    }

    private void customDrawerHeader() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String img_cover = preferences.getString("img_cover", null);

        if (img_cover != null) {
            File img = new File(img_cover);
            if (img.exists()) {
                Glide.with(this).load(img).diskCacheStrategy(DiskCacheStrategy.ALL).into(drawerHeader);
            }
        }

        trakt.users().settings()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BaseObserver<Settings>() {
            @Override
            public void onNext(Settings settings) {
                if (settings.user.images.avatar.full != null) {
                    Picasso.with(getApplicationContext()).load(settings.user.images.avatar.full).into(drawerAvatar);
                }
                if (settings.account.cover_image != null) {
                    Picasso.with(getApplicationContext()).load(settings.account.cover_image).transform(new RoundedTransformation()).into(drawerHeader);
                }
                drawerUsername.setText(settings.user.username);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_shows) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        } else if (id == R.id.nav_movies) {
            Intent i = new Intent(getApplicationContext(), DiscoverActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        } else if (id == R.id.nav_collection) {

        } else if (id == R.id.nav_settings) {
            Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_logout) {
            trakt.logout();
            launchConnectActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void launchConnectActivity() {
        Intent i = new Intent(this, ConnectTraktActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_menu).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search_menu) {
            onSearchRequested();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
