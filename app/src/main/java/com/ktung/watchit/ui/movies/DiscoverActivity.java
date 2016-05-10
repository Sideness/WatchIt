package com.ktung.watchit.ui.movies;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ktung.watchit.R;
import com.ktung.watchit.api.trakt.entities.MovieResponse;
import com.ktung.watchit.api.trakt.entities.SearchResult;
import com.ktung.watchit.api.trakt.enums.Extended;
import com.ktung.watchit.api.trakt.enums.Period;
import com.ktung.watchit.api.trakt.enums.Type;
import com.ktung.watchit.ui.adapter.SearchResultAdapter;
import com.ktung.watchit.ui.base.BaseDrawerActivity;
import com.ktung.watchit.ui.movies.adapter.DiscoverAdapter;
import com.ktung.watchit.util.BaseObserver;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class DiscoverActivity extends BaseDrawerActivity implements AdapterView.OnItemClickListener {
    @Bind(R.id.searchResultList)
    protected ListView searchResultList;
    @Bind(R.id.moviesList)
    protected ListView moviesList;

    private SearchResultAdapter searchAdapter;
    private DiscoverAdapter adapter;
    private List<MovieResponse> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = getLayoutInflater().inflate(R.layout.ac_movies_discover, null, false);
        drawer.addView(contentView, 0);
        ButterKnife.bind(this);

        handleIntent(getIntent());
        searchAdapter = new SearchResultAdapter(this, new ArrayList<>());
        searchResultList.setAdapter(searchAdapter);
        searchResultList.setOnItemClickListener(this);

        adapter = new DiscoverAdapter(this, movies);
        moviesList.setAdapter(adapter);

        moviesList.setOnItemClickListener(this);

        loadList();
    }

    private void loadList() {
        trakt.movies().mostWatched(Period.WEEKLY, Extended.FULLIMAGES)
        .observeOn(AndroidSchedulers.mainThread())
        .compose(bindToLifecycle())
        .subscribe(new BaseObserver<List<MovieResponse>>() {
            @Override
            public void onNext(List<MovieResponse> movieResponses) {
                for (MovieResponse movie : movieResponses) {
                    adapter.add(movie);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, MovieActivity.class);
        if (adapterView.getId() == searchResultList.getId()) {
            intent.putExtra("movieId", searchAdapter.getItem(i).movie.ids.trakt);
        } else if (adapterView.getId() == moviesList.getId()) {
            intent.putExtra("movieId", movies.get(i).movie.ids.trakt);
        }
        startActivity(intent);
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query, Type.MOVIE);
            setTitle(getString(R.string.search_query_title) + query);
        }
    }
}
