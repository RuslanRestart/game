package com.game.exception_handling;

/**
 * Класс для передачи текста из исключения в ответ сервера
 */
public class PlayerIncorrectData {

    private String info; //текст для исключения

    public PlayerIncorrectData() {}

    public String getInfo(){
        return info;
    }

    public void setInfo(String info){
        this.info = info;
    }
}
