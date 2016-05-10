package com.ktung.watchit.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ktung.watchit.R;
import com.ktung.watchit.api.trakt.entities.SearchResult;
import com.ktung.watchit.api.trakt.enums.Type;
import com.ktung.watchit.ui.adapter.SearchResultAdapter;
import com.ktung.watchit.ui.base.BaseDrawerActivity;
import com.ktung.watchit.ui.shows.ShowActivity;
import com.ktung.watchit.ui.shows.adapter.ShowsPagerAdapter;
import com.ktung.watchit.util.BaseObserver;
import com.ktung.watchit.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class MainActivity extends BaseDrawerActivity implements AdapterView.OnItemClickListener {
    @Bind(R.id.searchResultList)
    protected ListView searchResultList;
    @Bind(R.id.showsTabLayout)
    protected TabLayout showsTabLayout;
    @Bind(R.id.showsViewPager)
    protected ViewPager showsViewPager;

    private SearchResultAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.handleTheme(getApplicationContext(), this, false);
        super.onCreate(savedInstanceState);
        View contentView = getLayoutInflater().inflate(R.layout.ac_main, null, false);
        drawer.addView(contentView, 0);
        Timber.plant(new Timber.DebugTree());
        ButterKnife.bind(this);

        handleIntent(getIntent());

        searchAdapter = new SearchResultAdapter(getApplicationContext(), new ArrayList<>());
        searchResultList.setAdapter(searchAdapter);
        searchResultList.setOnItemClickListener(this);

        showsViewPager.setAdapter(new ShowsPagerAdapter(getSupportFragmentManager(), getApplicationContext()));
        showsTabLayout.setupWithViewPager(showsViewPager);
    }

    private void doSearch(String query, Type type) {
        onSearchRequested();

        trakt.search().textQuery(query, type, null, null, null)
        .compose(bindToLifecycle())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BaseObserver<List<SearchResult>>() {
            @Override
            public void onNext(List<SearchResult> searchResults) {
                searchAdapter.clear();
                searchAdapter.addAll(searchResults);
                searchAdapter.notifyDataSetChanged();
                searchResultList.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
        intent.putExtra("showId", searchAdapter.getItem(i).show.ids.trakt);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query, Type.SHOW);
            setTitle(getString(R.string.search_query_title) + query);
        }
    }
}
