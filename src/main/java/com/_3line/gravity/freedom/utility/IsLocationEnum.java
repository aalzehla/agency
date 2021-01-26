package com._3line.gravity.freedom.utility;

/**
 * Created by JohnA on 07-Feb-18.
 */
public enum IsLocationEnum {

    ALLOW("ALLOW"),
    DISALLOW("DISALLOW");

    private String status;

    private IsLocationEnum(final String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return this.status;
    }

    public String getName() {
        return this.name();
    }
}
