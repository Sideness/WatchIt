package com.ktung.watchit.api.trakt.entities;

public abstract class BaseCheckinRequest {
    public SharingSettings sharing;
    public String message;
    public String venue_id;
    public String venue_name;
    public String app_version;
    public String app_date;
}
