package com.annie.base.common;

public enum Role {
    ADMIN("ADMIN"),
    PATIENT("PATIENT"),
    DOCTOR("DOCTOR");

    private String value;

    Role(String text) {
        this.value = text;
    }
    public String getValue() {
        return this.value;
    }

    public static Role fromString(String text) {
        for (Role b : Role.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
