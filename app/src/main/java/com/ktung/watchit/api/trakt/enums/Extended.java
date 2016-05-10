package com.ktung.watchit.api.trakt.enums;

public enum Extended {
    DEFAULT("min"),
    IMAGES("images"),
    FULL("full"),
    FULLIMAGES("full,images");

    private final String value;

    Extended(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

