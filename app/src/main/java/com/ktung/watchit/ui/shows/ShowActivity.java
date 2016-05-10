package com.ktung.watchit.ui.shows;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ktung.watchit.R;
import com.ktung.watchit.api.trakt.Trakt;
import com.ktung.watchit.api.trakt.entities.Episode;
import com.ktung.watchit.api.trakt.entities.Season;
import com.ktung.watchit.api.trakt.entities.Show;
import com.ktung.watchit.api.trakt.entities.SyncItems;
import com.ktung.watchit.api.trakt.entities.SyncResponse;
import com.ktung.watchit.api.trakt.entities.SyncShow;
import com.ktung.watchit.api.trakt.enums.Extended;
import com.ktung.watchit.util.BaseObserver;
import com.ktung.watchit.util.Utils;
import com.squareup.picasso.Picasso;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShowActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    @Bind(R.id.showPoster)
    protected ImageView showPoster;
    @Bind(R.id.showSynopsis)
    protected TextView showSynopsis;
    @Bind(R.id.showGenres)
    protected TextView showGenres;
    @Bind(R.id.showCertification)
    protected TextView showCertification;
    @Bind(R.id.showNetwork)
    protected TextView showNetwork;
    @Bind(R.id.showFirstAired)
    protected TextView showFirstAired;
    @Bind(R.id.seasonSpinner)
    protected Spinner seasonsSpinner;
    @Bind(R.id.episodesListView)
    protected TwoWayView episodesListView;
    @Bind(R.id.addToWatchlistButton)
    protected Button addToWatchlistButton;

    private ArrayAdapter<String> seasonsAdapter;
    private ArrayAdapter<String> episodesAdapter;

    private Trakt trakt;
    private Show show;
    private Map<Integer, List<Episode>> seasons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_show);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ButterKnife.bind(this);

        int showId = getIntent().getIntExtra("showId", -1);
        if (showId == -1) {
            finish();
            return;
        }

        seasons = new HashMap<>();
        seasonsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<>());
        seasonsSpinner.setAdapter(seasonsAdapter);
        seasonsSpinner.setOnItemSelectedListener(this);
        episodesAdapter = new ArrayAdapter<>(this, R.layout.simple_list_item, new ArrayList<>());
        episodesListView.setAdapter(episodesAdapter);
        episodesListView.setOnItemClickListener(this);
        trakt = Trakt.getInstance();
        loadShow(showId);
        loadSeasons(showId);
    }

    private void loadSeasons(int showId) {
        trakt.seasons().summary(String.valueOf(showId), null)
        .observeOn(Schedulers.newThread())
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(new BaseObserver<List<Season>>() {
            @Override
            public void onNext(List<Season> seasons) {
                setSeasons(seasons);
            }
        });
    }

    private void loadEpisodes(String showId, int numSeason) {
        if (seasons.get(numSeason).isEmpty()) {
            trakt.seasons().season(showId, numSeason, null)
            .observeOn(Schedulers.newThread())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseObserver<List<Episode>>() {
                @Override
                public void onNext(List<Episode> episodes) {
                    setEpisodes(numSeason, episodes);
                }
            });
        }
    }

    private void loadShow(int showId) {
        trakt.shows().summary(String.valueOf(showId), Extended.FULLIMAGES)
        .observeOn(Schedulers.newThread())
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(new BaseObserver<Show>() {
            @Override
            public void onNext(Show show) {
                setShow(show);
                loadView();
            }
        });
    }

    private void loadView() {
        runOnUiThread(() -> {
            setTitle(show.title);
            showSynopsis.setText(show.overview);
            showGenres.setText(show.genres.toString());
            showCertification.setText(show.certification);
            showNetwork.setText(show.network);
            showFirstAired.setText(show.first_aired.toString("d, MMM YY"));
            Picasso.with(getApplicationContext()).load(show.images.poster.thumb).into(showPoster);
        });
    }

    @OnClick(R.id.addToWatchlistButton)
    protected void addToWatchlistCb() {
        SyncItems item = new SyncItems()
            .shows(new SyncShow(show.ids));
        trakt.sync().addToWatchlist(item)
        .observeOn(Schedulers.newThread())
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(new BaseObserver<SyncResponse>() {
            @Override
            public void onNext(SyncResponse syncResponse) {
                if (1 == syncResponse.added.shows) {
                    Utils.toaster(
                            getApplicationContext(),
                            String.format("%s %s", show.title, getString(R.string.added_to_watchlist)),
                            Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == episodesListView.getId()) {
            int numEp = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
            Intent intent = new Intent(this, EpisodeActivity.class);
            intent.putExtra("showId", show.ids.trakt);
            intent.putExtra("season", Integer.parseInt(seasonsSpinner.getSelectedItem().toString()));
            intent.putExtra("number", numEp);
            startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == seasonsSpinner.getId()) {
            if (show != null) {
                String showId = String.valueOf(show.ids.trakt);
                int seasonNumber = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
                loadEpisodes(showId, seasonNumber);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons.clear();
        for (Season season : seasons) {
            this.seasons.put(season.number, new ArrayList<>());
        }

        runOnUiThread(() -> {
            seasonsAdapter.clear();
            for (int number : this.seasons.keySet()) {
                seasonsAdapter.add(String.valueOf(number));
            }
            seasonsAdapter.notifyDataSetChanged();
        });
    }

    public void setEpisodes(int numSeason, List<Episode> episodes) {
        seasons.put(numSeason, episodes);
        runOnUiThread(() -> {
            episodesAdapter.clear();
            for (Episode ep : episodes) {
                episodesAdapter.add(ep.number.toString());
            }
            episodesAdapter.notifyDataSetChanged();
        });

    }
}
