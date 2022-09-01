package com.game;

import com.game.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PlayerGlobalExceptionHandler {

    //Не существующий id
    @ExceptionHandler
    public ResponseEntity<PlayerIncorrectData> handleException(NoSuchPlayerException exception){
        PlayerIncorrectData data = new PlayerIncorrectData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    //id == 0
    @ExceptionHandler
    public ResponseEntity<PlayerIncorrectData> handleException(ZeroIdException exception){
        PlayerIncorrectData data = new PlayerIncorrectData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    //если дата рождения отрицательная
    @ExceptionHandler
    public ResponseEntity<PlayerIncorrectData> handleException(NegativeBirthdayException exception){
        PlayerIncorrectData data = new PlayerIncorrectData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    //если имя или звание слишком длинные
    @ExceptionHandler
    public ResponseEntity<PlayerIncorrectData> handleException(TitleOrNameLengthTooBigException exception){
        PlayerIncorrectData data = new PlayerIncorrectData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    //если тело запроса на создание игрока пустое (не заполнены данные)
    @ExceptionHandler
    public ResponseEntity<PlayerIncorrectData> handleException(RequestBodyIsEmptyException exception){
        PlayerIncorrectData data = new PlayerIncorrectData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    //если неверно задано значение опыта
    @ExceptionHandler
    public ResponseEntity<PlayerIncorrectData> handleException(ExperienceIsTooBigOrNegativeException exception){
        PlayerIncorrectData data = new PlayerIncorrectData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<PlayerIncorrectData> handleException(EmptyDataForUpdateException exception){
        PlayerIncorrectData data = new PlayerIncorrectData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

}
