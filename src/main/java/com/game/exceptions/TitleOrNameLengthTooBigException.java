package com.game.exceptions;

public class TitleOrNameLengthTooBigException extends RuntimeException{

    public TitleOrNameLengthTooBigException(String message) {
        super(message);
    }
}
