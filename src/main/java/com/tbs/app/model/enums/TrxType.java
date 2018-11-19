package com.tbs.app.model.enums;

import java.util.Arrays;

public enum TrxType {
    DEPOSIT("deposit"),
    WITHDRAWAL("withdrawal");

    private String value;

    private TrxType(String value) {
        this.value = value;
    }

    public static TrxType fromValue(String value) {
        for (TrxType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException(
                "Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
