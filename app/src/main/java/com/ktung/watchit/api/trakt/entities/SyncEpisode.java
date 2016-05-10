package com.ktung.watchit.api.trakt.entities;

import org.joda.time.DateTime;

public class SyncEpisode {
    public EpisodeIds ids;
    public DateTime watched_at;

    public SyncEpisode(EpisodeIds ids) {
        this.ids = ids;
    }

    public SyncEpisode watchedAt(DateTime watched_at) {
        this.watched_at = watched_at;
        return this;
    }
}
