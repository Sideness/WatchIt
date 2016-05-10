package com.ktung.watchit.api.trakt.enums;

public enum Period {
    WEEKLY("weekly"),
    MONTHLY("monthly"),
    YEARLY("yearly"),
    ALL("all");

    private final String value;

    Period(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
