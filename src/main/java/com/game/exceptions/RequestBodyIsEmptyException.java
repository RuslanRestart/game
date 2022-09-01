package com.game.exceptions;

public class RequestBodyIsEmptyException extends RuntimeException{

    public RequestBodyIsEmptyException(String message) {
        super(message);
    }
}
