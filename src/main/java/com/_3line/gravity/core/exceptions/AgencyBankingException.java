package com._3line.gravity.core.exceptions;

public class AgencyBankingException extends GravityException {

    public AgencyBankingException() {
        super("Failed to perform the requested action");
    }

    public AgencyBankingException(Throwable cause) {
        super("Failed to perform the requested action", cause);
    }

    public AgencyBankingException(String message) {
        super(message);
    }

    public AgencyBankingException(String message, Throwable cause) {
        super(message, cause);
    }
}
