package com.ktung.watchit.ui.shows;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ktung.watchit.R;
import com.ktung.watchit.api.trakt.Trakt;
import com.ktung.watchit.api.trakt.entities.ShowResponse;
import com.ktung.watchit.api.trakt.enums.Extended;
import com.ktung.watchit.api.trakt.enums.Period;
import com.ktung.watchit.ui.shows.adapter.DiscoverAdapter;
import com.ktung.watchit.util.BaseObserver;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DiscoverFragment extends RxFragment implements AdapterView.OnItemClickListener {
    @Bind(R.id.showsListView)
    protected ListView showsListView;

    private Trakt trakt;
    private List<ShowResponse> shows;

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
            trakt.shows().mostWatched(Period.WEEKLY, Extended.FULLIMAGES)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(bindToLifecycle())
            .subscribe(new BaseObserver<List<ShowResponse>>() {
                @Override
                public void onNext(List<ShowResponse> showResponseList) {
                    for (ShowResponse response : showResponseList) {
                        shows.add(response);
                    }
                    setDiscoverAdapter();
                }
            });
        }
    }

    private void setDiscoverAdapter() {
        DiscoverAdapter adapter = new DiscoverAdapter(getContext(), shows);
        showsListView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getContext(), ShowActivity.class);
        intent.putExtra("showId", shows.get(i).show.ids.trakt);
        startActivity(intent);
    }
}
