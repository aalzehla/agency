package com._3line.gravity.core.integration.exception;

import com._3line.gravity.core.exceptions.GravityException;

/**
 * @author FortunatusE
 * @date 11/19/2018
 */
public class AccountException extends GravityException {

    public AccountException(String message){
        super(message);
    }

    public AccountException(String message, Throwable cause){
        super(message, cause);
    }
}
