package com.ktung.watchit.ui.movies;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ktung.watchit.R;
import com.ktung.watchit.api.trakt.Trakt;
import com.ktung.watchit.api.trakt.entities.Movie;
import com.ktung.watchit.api.trakt.entities.SyncItems;
import com.ktung.watchit.api.trakt.entities.SyncMovie;
import com.ktung.watchit.api.trakt.entities.SyncResponse;
import com.ktung.watchit.api.trakt.enums.Extended;
import com.ktung.watchit.util.BaseObserver;
import com.ktung.watchit.util.Utils;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;

public class MovieActivity extends RxAppCompatActivity {
    @Bind(R.id.poster)
    protected ImageView poster;
    @Bind(R.id.synopsis)
    protected TextView synopsis;
    @Bind(R.id.genres)
    protected TextView genres;
    @Bind(R.id.certification)
    protected TextView certification;
    @Bind(R.id.released)
    protected TextView released;
    @Bind(R.id.addToWatchlistButton)
    protected Button addToWatchlistButton;

    private Trakt trakt;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_movie);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ButterKnife.bind(this);

        int movieId = getIntent().getIntExtra("movieId", -1);
        if (movieId == -1) {
            finish();
            return;
        }

        trakt = Trakt.getInstance();
        loadMovie(movieId);
    }

    private void loadMovie(int movieId) {
        trakt.movies().summary(String.valueOf(movieId), Extended.FULLIMAGES)
        .compose(bindToLifecycle())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BaseObserver<Movie>() {
            @Override
            public void onNext(Movie movie) {
                setMovie(movie);
                loadView();
            }
        });
    }

    private void loadView() {
        setTitle(movie.title);
        synopsis.setText(movie.overview);
        genres.setText(movie.genres.toString());
        certification.setText(movie.certification);
        released.setText(movie.released.toString("YYYY-MM-dd"));
        Picasso.with(this).load(movie.images.poster.thumb).into(poster);
    }

    @OnClick(R.id.addToWatchlistButton)
    protected void addToWatchlistCb() {
        SyncItems item = new SyncItems()
            .movies(new SyncMovie(movie.ids));
        trakt.sync().addToWatchlist(item)
        .compose(bindToLifecycle())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BaseObserver<SyncResponse>() {
            @Override
            public void onNext(SyncResponse syncResponse) {
                if (1 == syncResponse.added.movies) {
                    Utils.toaster(
                            getApplicationContext(),
                            String.format("%s %s", movie.title, getString(R.string.added_to_watchlist)),
                            Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
