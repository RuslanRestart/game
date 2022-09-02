package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exception_handling.*;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService{

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Page<Player> getAllPlayersPage(Map<String, String> params){
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

        int pageNumber = params.containsKey("pageNumber") ? Integer.parseInt(params.get("pageNumber")) : 0;
        int pageSize = params.containsKey("pageSize") ? Integer.parseInt(params.get("pageSize")) : 3;
        PlayerOrder order = params.containsKey("order") ? PlayerOrder.valueOf(params.get("order")) : PlayerOrder.ID;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.DEFAULT_DIRECTION, order.getFieldName());
        return playerRepository.getAllByFilters(name, title, race, profession, after, before,
                banned, minExperience, maxExperience, minLevel, maxLevel, pageable);
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
    public void calculationAndSetLevelAndUntilNextLevel(Player player){
        int level= (int) (Math.sqrt(2500 + (200 * player.getExperience())) - 50) / 100;
        player.setLevel(level);
        Integer untilNextLevel = 50 * (player.getLevel()+1) * (player.getLevel()+2) - player.getExperience();
        player.setUntilNextLevel(untilNextLevel);
    }

    @Override
    public void validationId(long id) {
        if (id <= 0){
            IncorrectDataException ex = new IncorrectDataException("id должен быть > 0!");
            System.out.println(ex.getMessage());
            throw ex;
        }else if (!playerRepository.findById(id).isPresent()){
            NoSuchPlayerException ex = new NoSuchPlayerException("По заданному id = " + id + " игрок не найден!");
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public Boolean validationPlayer(Player player) {
        if (!(validateParameters(player) && validateBirthdate(player) && validateTitleAndName(player) && validateExperience(player))){
            IncorrectDataException incorrectDataException = new IncorrectDataException("Введены некорректные данные при сохранении/обновлении игрока! " +
                    "Проверьте правильность и повторите попытку!");
            System.out.println(incorrectDataException.getMessage());
            throw incorrectDataException;
        }
        return validateParameters(player) && validateBirthdate(player) && validateTitleAndName(player) && validateExperience(player);
    }

    @Override
    public Boolean validateParameters(Player player){
        return player.getName() != null || player.getTitle() != null || player.getBirthday() != null
                || player.getRace() != null || player.getProfession() != null || player.getBanned() != null
                || player.getExperience() != null;
    }

    @Override
    public Boolean validateBirthdate(Player player){
        return player.getBirthday() == null || (player.getBirthday().getTime() >= 0
                && 1900 + player.getBirthday().getYear() >= 2000 && 1900 + player.getBirthday().getYear() <= 3000);
    }

    @Override
    public Boolean validateTitleAndName(Player player){
        return player.getName() == null || player.getTitle() == null || (player.getName().length() <= 12 && player.getTitle().length() <= 30);
    }

    @Override
    public Boolean validateExperience(Player player){
        return player.getExperience() == null || (player.getExperience() >= 0 && player.getExperience() <= 10_000_000);
    }

    @Override
    public void setPlayerParameters(Player requestPlayer, Player DBPlayer){

        if (requestPlayer.getName() == null){
            requestPlayer.setName(DBPlayer.getName());
        }
        if (requestPlayer.getTitle() == null){
            requestPlayer.setTitle(DBPlayer.getTitle());
        }
        if (requestPlayer.getRace() == null){
            requestPlayer.setRace(DBPlayer.getRace());
        }
        if (requestPlayer.getProfession() == null){
            requestPlayer.setProfession(DBPlayer.getProfession());
        }
        if (requestPlayer.getBirthday() == null){
            requestPlayer.setBirthday(DBPlayer.getBirthday());
        }
        if (requestPlayer.getBanned() == null){
            requestPlayer.setBanned(DBPlayer.getBanned());
        }
        if (requestPlayer.getExperience() == null){
            requestPlayer.setExperience(DBPlayer.getExperience());
        }
        requestPlayer.setId(DBPlayer.getId());
        calculationAndSetLevelAndUntilNextLevel(requestPlayer);
    }
}
