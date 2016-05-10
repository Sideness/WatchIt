package com.ktung.watchit.api.trakt.entities;

import org.joda.time.DateTime;

import java.util.List;

public abstract class BaseEntity {
    public String title;

    // Extended info
    public String overview;
    public Double rating;
    public Integer votes;
    public DateTime updated_at;
    public List<String> available_translations;
    public Images images;
}

