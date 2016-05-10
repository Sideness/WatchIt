package com.ktung.watchit.api.trakt.entities;

import org.joda.time.DateTime;

public class SyncShow {
    public ShowIds ids;
    public DateTime watched_at;

    public SyncShow(ShowIds ids) {
        this.ids = ids;
    }

    public SyncShow watchedAt(DateTime watched_at) {
        this.watched_at = watched_at;
        return this;
    }
}
