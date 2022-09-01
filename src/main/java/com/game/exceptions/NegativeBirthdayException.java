package com.game.exceptions;

public class NegativeBirthdayException extends RuntimeException{

    public NegativeBirthdayException(String message) {
        super(message);
    }
}
