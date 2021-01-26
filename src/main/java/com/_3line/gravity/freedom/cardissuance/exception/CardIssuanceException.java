package com._3line.gravity.freedom.cardissuance.exception;

import com._3line.gravity.core.exceptions.GravityException;

/**
 * @author FortunatusE
 * @date 12/11/2018
 */
public class CardIssuanceException extends GravityException {



    public CardIssuanceException() {
    }


    public CardIssuanceException(String message) {
        super(message);
    }

    public CardIssuanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CardIssuanceException(Throwable cause) {
        super(cause);
    }

}
