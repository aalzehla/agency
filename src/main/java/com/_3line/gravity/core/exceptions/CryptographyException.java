package com._3line.gravity.core.exceptions;


public class CryptographyException extends AgencyBankingException {

    public CryptographyException() {
        super("Failed to perform the requested action");
    }

    public CryptographyException(Throwable cause) {
        super("Failed to perform the requested action", cause);
    }

    public CryptographyException(String message) {
        super(message);
    }

    public CryptographyException(String message, Throwable cause) {
        super(message, cause);
    }
}
