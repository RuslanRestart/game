package com.game.controller;

import com.game.exceptions.*;
import com.game.entity.Player;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/players")
public class RESTController {

    private final PlayerService playerService;

    @Autowired
    public RESTController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping()
    public List<Player> showAllPlayers(@RequestParam Map<String, String> params){
        return playerService.getAllPlayers(params);
    }

    @GetMapping("{id}")
    public Player getPlayer(@PathVariable int id){
        playerService.validationId(id);
        return playerService.getPlayer(id);
    }

    @PostMapping()
    public Player savePlayer(@RequestBody Player player){

        if (player.getName() == null && player.getTitle() == null && player.getBirthday() == null
                && player.getRace() == null && player.getProfession() == null && player.getBanned() == null
                && player.getExperience() == null){
            RequestBodyIsEmptyException ex = new RequestBodyIsEmptyException("Не указаны данные для создания игрока!");
            System.out.println(ex.getMessage());
            throw ex;
        }

        if (player.getBirthday().getTime()<0 || 1900 + player.getBirthday().getYear()<2000 || 1900 + player.getBirthday().getYear()>3000){
            NegativeBirthdayException ex = new NegativeBirthdayException("Дата рождения отрицательная или выходит за диапазон 2000-3000 включительно!");
            System.out.println(ex.getMessage());
            throw ex;
        }

        if (player.getName().length()>12 || player.getTitle().length()>30){
            TitleOrNameLengthTooBigException ex = new TitleOrNameLengthTooBigException("Звание игрока превышает 30 символов" +
                    " или имя игрока превышает 12 символов!");
            System.out.println(ex.getMessage());
            throw ex;
        }

        if (player.getExperience() <0 || player.getExperience() > 10_000_000){
            ExperienceIsTooBigOrNegativeException ex = new ExperienceIsTooBigOrNegativeException("Указанное значение опыта недопустимо. Требуется 0 - 10 000 000");
            System.out.println(ex.getMessage());
            throw ex;
        }

        playerService.setAndCalculationsLevelAndUntilNextLevel(player);
        playerService.savePlayer(player);
        System.out.println("Игрок успешно сохранён!");
        return player;
    }

    @PostMapping("{id}")
    public Player updatePlayer(@RequestBody Player reqPlayer, @PathVariable int id){
        playerService.validationId(id);
        Player DBPlayer = playerService.getPlayer(id);

        if (reqPlayer.getName() == null && reqPlayer.getTitle() == null && reqPlayer.getBirthday() == null
                && reqPlayer.getRace() == null && reqPlayer.getProfession() == null && reqPlayer.getBanned() == null
                && reqPlayer.getExperience() == null){
            System.out.println("Не указаны данные для обновления характеристик игрока!");
            return DBPlayer;
        }else if (checkExperience(reqPlayer)){
            ExperienceIsTooBigOrNegativeException ex = new ExperienceIsTooBigOrNegativeException("Значение опыта < 0 или > 10.000.000");
            System.out.println(ex.getMessage());
            throw ex;
        }
        else if (reqPlayer.getBirthday() != null && reqPlayer.getBirthday().getTime()<0){
            NegativeBirthdayException ex = new NegativeBirthdayException("Значение даты не может быть отрицательным!");
            System.out.println(ex.getMessage());
            throw ex;
        }
        else {
            if (reqPlayer.getName() == null){
                reqPlayer.setName(DBPlayer.getName());
            }
            if (reqPlayer.getTitle() == null){
                reqPlayer.setTitle(DBPlayer.getTitle());
            }
            if (reqPlayer.getRace() == null){
                reqPlayer.setRace(DBPlayer.getRace());
            }
            if (reqPlayer.getProfession() == null){
                reqPlayer.setProfession(DBPlayer.getProfession());
            }
            if (reqPlayer.getBirthday() == null){
                reqPlayer.setBirthday(DBPlayer.getBirthday());
            }
            if (reqPlayer.getBanned() == null){
                reqPlayer.setBanned(DBPlayer.getBanned());
            }
            if (reqPlayer.getExperience() == null){
                reqPlayer.setExperience(DBPlayer.getExperience());
            }

            reqPlayer.setId(DBPlayer.getId());
            playerService.setAndCalculationsLevelAndUntilNextLevel(reqPlayer);
            Player savePlayer = playerService.savePlayer(reqPlayer);
            System.out.println("Игрок был успешно обновлен!");
            return savePlayer;
        }
    }

    @DeleteMapping("{id}")
    public void deletePlayer(@PathVariable int id){
        playerService.validationId(id);
        playerService.deletePlayer(id);
    }

    @GetMapping("/count")
    public long getCountPlayersByFilters(@RequestParam Map<String, String> params){
        return playerService.getCountPlayersByFilters(params);
    }

    public boolean checkExperience(Player reqPlayer){
        if (reqPlayer.getExperience() != null){
            return reqPlayer.getExperience() < 0 || reqPlayer.getExperience() > 10_000_000;
        }return false;
    }
}
