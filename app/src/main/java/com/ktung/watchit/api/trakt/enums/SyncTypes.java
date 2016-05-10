package com.ktung.watchit.api.trakt.enums;

public enum SyncTypes {
    MOVIES("movies"),
    SHOWS("shows"),
    SEASONS("seasons"),
    EPISODES("episodes");

    private final String value;

    SyncTypes(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
