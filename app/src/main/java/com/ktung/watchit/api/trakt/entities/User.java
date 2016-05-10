package com.ktung.watchit.api.trakt.entities;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class User {
    public String username;
    @SerializedName("private")
    public Boolean isPrivate;
    public String name;
    public Boolean vip;
    public Boolean vip_ep;
    public DateTime joined_at;
    public String location;
    public String about;
    public String gender;
    public Integer age;
    public Avatar images;
}
