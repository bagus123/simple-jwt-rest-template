/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbs.app.model.enums;

import java.util.Arrays;

/**
 *
 * @author Hp
 */
public enum TrxSide {
    DEBET("debet"),
    CREDIT("credit");

    private String value;

    private TrxSide(String value) {
        this.value = value;
    }

    public static TrxSide fromValue(String value) {
        for (TrxSide type : values()) {
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
