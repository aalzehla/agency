package com._3line.gravity.freedom.wallet.exceptions;

import com._3line.gravity.core.exceptions.GravityException;

public class WalletException extends GravityException {

    public WalletException() {
        super("Failed to perform the requested action");
    }

    public WalletException(Throwable cause) {
        super("Failed to perform the requested action", cause);
    }

    public WalletException(String message) {
        super(message);
    }

    public WalletException(String message, Throwable cause) {
        super(message, cause);
    }

    public WalletException(String message, Object obj) {
        super(message, obj);
    }

}
