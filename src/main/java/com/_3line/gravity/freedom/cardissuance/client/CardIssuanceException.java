package com._3line.gravity.freedom.cardissuance.client;

/**
 * @author FortunatusE
 * @date 12/11/2018
 */
public class CardIssuanceException extends RuntimeException {



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
