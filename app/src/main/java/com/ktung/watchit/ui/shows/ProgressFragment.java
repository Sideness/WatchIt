package com.ktung.watchit.ui.shows;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ktung.watchit.R;
import com.ktung.watchit.api.trakt.Trakt;
import com.ktung.watchit.api.trakt.entities.ShowResponse;
import com.ktung.watchit.api.trakt.enums.Extended;
import com.ktung.watchit.ui.shows.adapter.ProgressAdapter;
import com.ktung.watchit.util.BaseObserver;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ProgressFragment extends RxFragment implements AdapterView.OnItemClickListener {
    @Bind(R.id.showsSwipeContainer)
    protected SwipeRefreshLayout showsSwipeContainer;
    @Bind(R.id.showsListView)
    protected ListView showsListView;

    private Trakt trakt;
    private List<ShowResponse> shows;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_shows_progress, container, false);
        ButterKnife.bind(this, view);

        shows = new ArrayList<>();
        trakt = Trakt.getInstance();
        showsSwipeContainer.setOnRefreshListener(this::loadList);
        showsSwipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);
        showsListView.setOnItemClickListener(this);
        loadList();

        return view;
    }

    private void loadList() {
        if (trakt.isLogged()) {
            trakt.sync().watchedShows(Extended.FULLIMAGES)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(bindToLifecycle())
            .subscribe(new BaseObserver<List<ShowResponse>>() {
                @Override
                public void onNext(List<ShowResponse> showResponseList) {
                    for (ShowResponse response : showResponseList) {
                        shows.add(response);
                    }
                    setProgressAdapter();
                    showsSwipeContainer.setRefreshing(false);
                }
            });
        }
    }

    private void setProgressAdapter() {
        ProgressAdapter adapter = new ProgressAdapter(getContext(), shows);
        showsListView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getContext(), ShowActivity.class);
        intent.putExtra("showId", shows.get(i).show.ids.trakt);

        if (shows.get(i).next_episode != null) {
            intent = new Intent(getContext(), EpisodeActivity.class);
            intent.putExtra("showId", shows.get(i).show.ids.trakt);
            intent.putExtra("season", shows.get(i).next_episode.season);
            intent.putExtra("number", shows.get(i).next_episode.number);
        }

        startActivity(intent);
    }
}
