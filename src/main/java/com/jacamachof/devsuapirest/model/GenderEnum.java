package com.jacamachof.devsuapirest.model;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static java.util.Arrays.stream;

public enum GenderEnum {
    FEMALE,
    MALE;

    private String value;

    private static final Map<String, GenderEnum> VALUES = new HashMap<>();
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("messages");

    static {
        stream(values()).forEach(value -> {
            value.setValue(BUNDLE.getString(value.name()));
            VALUES.put(value.getValue(), value);
        });
    }

    GenderEnum() {
    }

    private void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static GenderEnum of(String value) {
        return VALUES.get(value);
    }
}
