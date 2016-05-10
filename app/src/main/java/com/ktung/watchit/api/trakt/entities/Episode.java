package com.ktung.watchit.api.trakt.entities;

import org.joda.time.DateTime;

public class Episode extends BaseEntity {
    public Integer season;
    public Integer number;
    public EpisodeIds ids;

    // Extended info
    public DateTime first_aired;
}

