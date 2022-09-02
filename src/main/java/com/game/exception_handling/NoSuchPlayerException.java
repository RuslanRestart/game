package com.game.exception_handling;

/**
 * Исключение бросается, когда по указанному id игрок в БД отсутствует
 */
public class NoSuchPlayerException extends RuntimeException{

    public NoSuchPlayerException(String message) {
        super(message);
    }
}
