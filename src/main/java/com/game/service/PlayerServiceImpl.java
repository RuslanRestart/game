package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exceptions.NoSuchPlayerException;
import com.game.exceptions.ZeroIdException;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService{

    @Autowired
    private PlayerRepository playerRepository;


    @Override
    public List<Player> getAllPlayers(Map<String, String> params) {
        String name = params.getOrDefault("name", null);
        String title = params.getOrDefault("title", null);

        Race race = params.containsKey("race") ? Race.valueOf(params.get("race")) : null;
        Profession profession = params.containsKey("profession") ? Profession.valueOf(params.get("profession")) : null;

        Date after = params.containsKey("after") ? new Date(Long.parseLong(params.get("after"))) : null;
        Date before = params.containsKey("before") ? new Date(Long.parseLong(params.get("before"))) : null;

        Boolean banned = params.containsKey("banned") ? Boolean.parseBoolean(params.get("banned")) : null;

        Integer minExperience = params.containsKey("minExperience") ? Integer.parseInt(params.get("minExperience")) : null;
        Integer maxExperience = params.containsKey("maxExperience") ? Integer.parseInt(params.get("maxExperience")) : null;
        Integer minLevel = params.containsKey("minLevel") ? Integer.parseInt(params.get("minLevel")) : null;
        Integer maxLevel = params.containsKey("maxLevel") ? Integer.parseInt(params.get("maxLevel")) : null;

        Integer pageNumber = params.containsKey("pageNumber") ? Integer.parseInt(params.get("pageNumber")) : 0;
        Integer pageSize = params.containsKey("pageSize") ? Integer.parseInt(params.get("pageSize")) : 3;
        PlayerOrder order = params.containsKey("order") ? PlayerOrder.valueOf(params.get("order")) : PlayerOrder.ID;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.DEFAULT_DIRECTION, order.getFieldName());
        return playerRepository.getAllByFilters(name, title, race, profession, after, before,
                banned, minExperience, maxExperience, minLevel, maxLevel, pageable).getContent();
    }

    @Override
    public long getCountPlayersByFilters(Map<String, String> params) {
        String name = params.getOrDefault("name", null);
        String title = params.getOrDefault("title", null);

        Race race = params.containsKey("race") ? Race.valueOf(params.get("race")) : null;
        Profession profession = params.containsKey("profession") ? Profession.valueOf(params.get("profession")) : null;

        Date after = params.containsKey("after") ? new Date(Long.parseLong(params.get("after"))) : null;
        Date before = params.containsKey("before") ? new Date(Long.parseLong(params.get("before"))) : null;

        Boolean banned = params.containsKey("banned") ? Boolean.parseBoolean(params.get("banned")) : null;

        Integer minExperience = params.containsKey("minExperience") ? Integer.parseInt(params.get("minExperience")) : null;
        Integer maxExperience = params.containsKey("maxExperience") ? Integer.parseInt(params.get("maxExperience")) : null;
        Integer minLevel = params.containsKey("minLevel") ? Integer.parseInt(params.get("minLevel")) : null;
        Integer maxLevel = params.containsKey("maxLevel") ? Integer.parseInt(params.get("maxLevel")) : null;

        Integer pageNumber = params.containsKey("pageNumber") ? Integer.parseInt(params.get("pageNumber")) : 0;
        Integer pageSize = params.containsKey("pageSize") ? Integer.parseInt(params.get("pageSize")) : 3;
        PlayerOrder order = params.containsKey("order") ? PlayerOrder.valueOf(params.get("order")) : PlayerOrder.ID;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.DEFAULT_DIRECTION, order.getFieldName());

        return playerRepository.getAllByFilters(name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel, pageable).getTotalElements();
    }

    @Override
    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public void deletePlayer(int id) {
        playerRepository.deleteById((long)id);
    }

    @Override
    public Player getPlayer(int id) {
        Player player = null;

        Optional<Player> playerOptional = playerRepository.findById((long)id);
        if (playerOptional.isPresent()){
            player = playerOptional.get();
        }
        return player;
    }

    @Override
    public void setAndCalculationsLevelAndUntilNextLevel(Player player){
        int level= (int) (Math.sqrt(2500 + (200 * player.getExperience())) - 50) / 100;
        player.setLevel(level);
        Integer untilNextLevel = 50 * (player.getLevel()+1) * (player.getLevel()+2) - player.getExperience();
        player.setUntilNextLevel(untilNextLevel);
    }

    @Override
    public Boolean validationId(long id) {
        if (id <= 0){
            ZeroIdException ex = new ZeroIdException("id должен быть > 0!");
            System.out.println(ex.getMessage());
            throw ex;
        }else if (!playerRepository.findById(id).isPresent()){
            NoSuchPlayerException ex = new NoSuchPlayerException("По заданному id = " + id + " игрок не найден!");
            System.out.println(ex.getMessage());
            throw ex;
        }else return true;
    }

}
