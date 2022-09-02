package com.game.controller;

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
    public List<Player> getAllPlayers(@RequestParam Map<String, String> params){
        return playerService.getAllPlayers(params);
    }

    @GetMapping("{id}")
    public Player getPlayer(@PathVariable int id){
        playerService.validationId(id);
        return playerService.getPlayer(id);
    }

    @PostMapping()
    public Player savePlayer(@RequestBody Player player){
        playerService.validationPlayer(player);
        playerService.calculationAndSetLevelAndUntilNextLevel(player);
        playerService.savePlayer(player);
        System.out.println("Игрок успешно сохранён!");
        return player;
    }

    @PostMapping("{id}")
    public Player updatePlayer(@RequestBody Player requestPlayer, @PathVariable int id){
        playerService.validationId(id);
        Player DBPlayer = playerService.getPlayer(id);

        if (!playerService.validateParameters(requestPlayer)){ return DBPlayer;}

        playerService.validationPlayer(requestPlayer);
        playerService.setPlayerParameters(requestPlayer, DBPlayer);
        Player savedPlayer = playerService.savePlayer(requestPlayer);
        System.out.println("Игрок был успешно обновлен!");
        return savedPlayer;
    }

    @DeleteMapping("{id}")
    public void deletePlayer(@PathVariable int id){
        playerService.validationId(id);
        playerService.deletePlayer(id);
        System.out.println("Игрок был успешно удален!");
    }

    @GetMapping("count")
    public long getCountPlayersByFilters(@RequestParam Map<String, String> params){
        return playerService.getCountPlayersByFilters(params);
    }
}
