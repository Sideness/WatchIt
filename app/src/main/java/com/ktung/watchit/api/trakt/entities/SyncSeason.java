package com.ktung.watchit.api.trakt.entities;

import org.joda.time.DateTime;

import java.util.List;

public class SyncSeason {
    public Integer number;
    public List<SyncEpisode> episodes;
    public DateTime watched_at;
}
