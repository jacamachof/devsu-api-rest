package com.jacamachof.devsuapirest.model;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static java.util.Arrays.stream;

public enum AccountType {
    CHECKING,
    SAVING;

    private String value;

    private static final Map<String, AccountType> VALUES = new HashMap<>();
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("messages");

    static {
        stream(values()).forEach(value -> {
            value.setValue(BUNDLE.getString(value.name()));
            VALUES.put(value.getValue(), value);
        });
    }

    AccountType() {
    }

    private void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AccountType of(String value) {
        return VALUES.get(value);
    }
}
