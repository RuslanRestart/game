package com.game.exception_handling;

/**
 * Исключение бросается, когда были введены некорректные данные при создании/редактировании игрока
 */
public class IncorrectDataException extends RuntimeException {
    public IncorrectDataException(String message) {
        super(message);
    }
}
