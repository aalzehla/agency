/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.utility;

/**
 *
 * @author OlalekanW
 */
public class InvalidPhoneNumberException extends Exception {

    String message;

    public InvalidPhoneNumberException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
