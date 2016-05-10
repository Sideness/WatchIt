package com.ktung.watchit.api.trakt.entities;

import org.joda.time.DateTime;

public abstract class BaseCheckinResponse {
    public DateTime watched_at;
    public SharingSettings sharing;
}
