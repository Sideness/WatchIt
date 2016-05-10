package com.ktung.watchit.api.trakt.enums;

public enum Type {
    MOVIE("movie"),
    SHOW("show"),
    EPISODE("episode"),
    PERSON("person"),
    LIST("list");

    private final String value;

    Type(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
