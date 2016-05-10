package com.ktung.watchit.ui.shows;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ktung.watchit.R;
import com.ktung.watchit.api.trakt.Trakt;
import com.ktung.watchit.api.trakt.entities.Episode;
import com.ktung.watchit.api.trakt.entities.EpisodeCheckinRequest;
import com.ktung.watchit.api.trakt.entities.EpisodeCheckinResponse;
import com.ktung.watchit.api.trakt.entities.Show;
import com.ktung.watchit.api.trakt.entities.ShowResponse;
import com.ktung.watchit.api.trakt.entities.SyncEpisode;
import com.ktung.watchit.api.trakt.entities.SyncItems;
import com.ktung.watchit.api.trakt.entities.SyncResponse;
import com.ktung.watchit.api.trakt.enums.Extended;
import com.ktung.watchit.api.trakt.enums.SyncTypes;
import com.ktung.watchit.util.BaseObserver;
import com.ktung.watchit.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.client.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EpisodeActivity extends AppCompatActivity {
    private Trakt trakt;
    private Show show;
    private Episode episode;
    private boolean isCheckin;
    private boolean isWatched;

    @Bind(R.id.epTitle)
    protected TextView epTitle;
    @Bind(R.id.epOverview)
    protected TextView epOverview;
    @Bind(R.id.epRating)
    protected TextView epRating;
    @Bind(R.id.epTrakt)
    protected ImageView epTrakt;
    @Bind(R.id.epImage)
    protected ImageView epImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_episode);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ButterKnife.bind(this);

        int showId = getIntent().getIntExtra("showId", -1);
        int season = getIntent().getIntExtra("season", -1);
        int number = getIntent().getIntExtra("number", -1);
        if (-1 == showId || -1 == season || -1 == number) {
            finish();
            return;
        }

        isCheckin = false;
        isWatched = false;
        trakt = Trakt.getInstance();
        loadEpisode(String.valueOf(showId), String.valueOf(season), String.valueOf(number));
    }

    private void loadEpisode(String showId, String epSeason, String epNumber) {
        trakt.episodes().summary(showId, epSeason, epNumber, Extended.FULLIMAGES)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BaseObserver<Episode>() {
            @Override
            public void onNext(Episode inEpisode) {
                episode = inEpisode;
                loadShow(showId);
            }
        });
    }

    private void loadShow(String showId) {
        trakt.shows().summary(showId, null)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BaseObserver<Show>() {
            @Override
            public void onNext(Show inShow) {
                show = inShow;
                setTitle(String.format("%s S%d E%d", show.title, episode.season, episode.number));
                loadView();
            }
        });
    }

    private void loadView() {
        epTitle.setText(episode.title);
        epOverview.setText(episode.overview);
        epRating.setText(String.format("%.1f", episode.rating));
        epTrakt.setImageResource(R.drawable.ic_trakt);
        Picasso.with(getApplicationContext()).load(episode.images.screenshot.medium).into(epImage);
    }

    @OnClick(R.id.checkinButton)
    protected void checkinClicked() {
        if (isCheckin) {
            trakt.checkin().deleteActiveCheckin()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseObserver<Response>() {
                @Override
                public void onNext(Response response) {
                    if (204 == response.getStatus()) {
                        isCheckin = false;
                        Toast.makeText(getApplicationContext(), R.string.uncheckin, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            EpisodeCheckinRequest request = new EpisodeCheckinRequest();
            request.episode = episode;
            trakt.checkin().checkin(request)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseObserver<EpisodeCheckinResponse>() {
                @Override
                public void onNext(EpisodeCheckinResponse response) {
                    isCheckin = true;
                    Toast.makeText(getApplicationContext(), R.string.checkin, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    @OnClick(R.id.watchedButton)
    protected void watchedClicked() {
        SyncItems epItem = new SyncItems()
            .episodes(new SyncEpisode(episode.ids));

        Observable<SyncResponse> request;
        if (isWatched) {
            request = trakt.sync().removeItemsFromHistory(epItem);
        } else {
            request = trakt.sync().addToWatchedHistory(epItem);
        }

        request
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BaseObserver<SyncResponse>() {
            @Override
            public void onNext(SyncResponse syncResponse) {
                if (syncResponse.added != null) {
                    isWatched = true;
                    Utils.toaster(getApplicationContext(), R.string.watched, Toast.LENGTH_SHORT);
                } else {
                    isWatched = false;
                    Utils.toaster(getApplicationContext(), R.string.unwatched, Toast.LENGTH_SHORT);
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
}
