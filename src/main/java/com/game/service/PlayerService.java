package com.game.service;

import com.game.entity.Player;

import java.util.List;
import java.util.Map;

public interface PlayerService {

     List<Player> getAllPlayers(Map<String, String> params); //получить список всех игроков

     Player savePlayer(Player player); //создать игрока

     void deletePlayer(int id); //удалить игрока

     Player getPlayer(int id); //получить игрока по id

    long getCountPlayersByFilters(Map<String, String> params); //получить кол-о игроков по заданным фильтрам

     void calculationAndSetLevelAndUntilNextLevel(Player player); //подсчет уровня и кол-а опыта до след. уровня и установка значений

    void setPlayerParameters(Player requestPlayer, Player DbPlayer); //установка значений игроку при обновлении

    //Валидация:
    void validationId(long id); //валидация id

    Boolean validationPlayer(Player player); //валидация всех параметров Entity

    Boolean validateParameters(Player player);

    Boolean validateBirthdate(Player player);

    Boolean validateTitleAndName(Player player);

    Boolean validateExperience(Player player);
}
