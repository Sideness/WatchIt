package com.ktung.watchit.api.trakt.entities;

import org.joda.time.DateTime;

import java.util.List;

public class Show extends BaseEntity {
    public Integer year;
    public ShowIds ids;

    // Extended info
    public DateTime first_aired;
    public Airs airs;
    public Integer runtime;
    public String certification;
    public String network;
    public String country;
    public String trailer;
    public String homepage;
    public String status;
    public String language;
    public List<String> genres;
    public Integer aired_episodes;
}
