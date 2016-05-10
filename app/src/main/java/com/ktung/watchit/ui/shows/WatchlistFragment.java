package com.ktung.watchit.ui.shows;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ktung.watchit.R;
import com.ktung.watchit.api.trakt.Trakt;
import com.ktung.watchit.api.trakt.entities.Show;
import com.ktung.watchit.api.trakt.entities.WatchlistShow;
import com.ktung.watchit.api.trakt.enums.Extended;
import com.ktung.watchit.ui.shows.adapter.WatchlistAdapter;
import com.ktung.watchit.util.BaseObserver;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WatchlistFragment extends RxFragment implements AdapterView.OnItemClickListener {
    @Bind(R.id.showsListView)
    protected ListView showsListView;

    private Trakt trakt;
    private List<Show> shows;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_shows_discover, container, false);
        ButterKnife.bind(this, view);

        shows = new ArrayList<>();
        trakt = Trakt.getInstance();
        loadList();
        showsListView.setOnItemClickListener(this);

        return view;
    }

    private void loadList() {
        if (trakt.isLogged()) {
            trakt.sync().watchlistShows(Extended.FULLIMAGES)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(bindToLifecycle())
            .subscribe(new BaseObserver<List<WatchlistShow>>() {
                @Override
                public void onNext(List<WatchlistShow> watchlistShows) {
                    WatchlistAdapter adapter = new WatchlistAdapter(getContext(), watchlistShows);
                    showsListView.setAdapter(adapter);
                    for (WatchlistShow response : watchlistShows) {
                        shows.add(response.show);
                    }
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getContext(), ShowActivity.class);
        intent.putExtra("showId", shows.get(i).ids.trakt);
        startActivity(intent);
    }
}
