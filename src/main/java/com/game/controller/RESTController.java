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
        if (!playerService.validateParameters(player)){
            System.out.println("Проверьте корректность ввода данных. Игрок не сохранен!");
            return null;
        };
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
