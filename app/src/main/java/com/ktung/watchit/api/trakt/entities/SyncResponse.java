package com.ktung.watchit.api.trakt.entities;

public class SyncResponse {
    public SyncStats added;
    public SyncStats existing;
    public SyncStats deleted;
    public SyncErrors not_found;
}
