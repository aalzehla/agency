package com._3line.gravity.core.integration.exception;

import com._3line.gravity.core.exceptions.GravityException;

/**
 * @author FortunatusE
 * @date 11/19/2018
 */
public class WalletException extends GravityException {

    public WalletException(String message){
        super(message);
    }

    public WalletException(String message, Throwable cause){
        super(message, cause);
    }
}
