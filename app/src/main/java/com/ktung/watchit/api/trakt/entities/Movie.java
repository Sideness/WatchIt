package com.ktung.watchit.api.trakt.entities;

import org.joda.time.DateTime;

import java.util.List;

public class Movie extends BaseEntity {
    public Integer year;
    public MovieIds ids;

    // Extended info
    public String tagline;
    public DateTime released;
    public Integer runtime;
    public String trailer;
    public String homepage;
    public String language;
    public List<String> genres;
    public String certification;
}
