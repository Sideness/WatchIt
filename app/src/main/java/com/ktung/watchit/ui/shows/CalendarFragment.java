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
import com.ktung.watchit.api.trakt.entities.CalendarShowEntry;
import com.ktung.watchit.api.trakt.enums.Extended;
import com.ktung.watchit.ui.shows.adapter.CalendarAdapter;
import com.ktung.watchit.util.BaseObserver;
import com.trello.rxlifecycle.components.support.RxFragment;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CalendarFragment extends RxFragment implements AdapterView.OnItemClickListener {
    @Bind(R.id.showsListView)
    protected ListView showsListView;

    private Trakt trakt;
    private List<CalendarShowEntry> shows;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_shows_calendar, container, false);
        ButterKnife.bind(this, view);

        shows = new ArrayList<>();
        trakt = Trakt.getInstance();
        loadList();
        showsListView.setOnItemClickListener(this);

        return view;
    }

    private void loadList() {
        if (trakt.isLogged()) {
            LocalDate date = LocalDate.now();
            trakt.calendars().myShows(date.toString(), 7, Extended.FULLIMAGES)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(bindToLifecycle())
            .subscribe(new BaseObserver<List<CalendarShowEntry>>() {
                @Override
                public void onNext(List<CalendarShowEntry> showEntries) {
                    shows = showEntries;
                    CalendarAdapter adapter = new CalendarAdapter(getContext(), shows);
                    showsListView.setAdapter(adapter);
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getContext(), EpisodeActivity.class);
        intent.putExtra("showId", shows.get(i).show.ids.trakt);
        intent.putExtra("season", shows.get(i).episode.season);
        intent.putExtra("number", shows.get(i).episode.number);

        startActivity(intent);
    }
}
