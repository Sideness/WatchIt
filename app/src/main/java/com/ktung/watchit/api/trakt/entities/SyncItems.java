package com.ktung.watchit.api.trakt.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SyncItems {
    public List<SyncMovie> movies;
    public List<SyncShow> shows;
    public List<SyncEpisode> episodes;
    public List<Integer> ids;

    public SyncItems movies(SyncMovie movie) {
        return movies(new ArrayList<>(Collections.singletonList(movie)));
    }

    public SyncItems movies(List<SyncMovie> movies) {
        this.movies = movies;
        return this;
    }

    public SyncItems shows(SyncShow show) {
        return shows(new ArrayList<>(Collections.singletonList(show)));
    }

    public SyncItems shows(List<SyncShow> shows) {
        this.shows = shows;
        return this;
    }

    public SyncItems episodes(SyncEpisode episode) {
        return episodes(new ArrayList<>(Collections.singletonList(episode)));
    }

    public SyncItems episodes(List<SyncEpisode> episodes) {
        this.episodes = episodes;
        return this;
    }
}
