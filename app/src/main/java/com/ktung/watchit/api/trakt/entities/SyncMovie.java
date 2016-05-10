package com.ktung.watchit.api.trakt.entities;

import org.joda.time.DateTime;

public class SyncMovie {
    public MovieIds ids;
    public DateTime watched_at;

    public SyncMovie(MovieIds ids) {
        this.ids = ids;
    }

    public SyncMovie watchedAt(DateTime watched_at) {
        this.watched_at = watched_at;
        return this;
    }
}
