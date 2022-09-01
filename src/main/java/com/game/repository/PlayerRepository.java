package com.game.repository;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    String query = "select p from Player p where " +
            "(p.name like concat('%', :name, '%') or :name is null) and (p.title like concat('%', :title, '%') or :title is null)" +
            "and (p.race like :race or :race is null ) and (p.profession like :profession or :profession is null)" +
            "and (p.birthday >= :after or :after is null) and (p.birthday <= :before or :before is null)" +
            "and (p.banned = :banned or :banned is null )" +
            "and (p.experience >= :minExperience or :minExperience is null) and (p.experience <= :maxExperience or :maxExperience is null)" +
            "and (p.level >= :minLevel or :minLevel is null) and (p.level <= :maxLevel or :maxLevel is null )";

    @Query(query)
    Page<Player> getAllByFilters(
            @Param("name") String name, @Param("title") String title,
            @Param("race") Race race, @Param("profession")Profession profession,
            @Param("after") Date after, @Param("before") Date before,
            @Param("banned")Boolean banned,
            @Param("minExperience") Integer minExperience, @Param("maxExperience") Integer maxExperience,
            @Param("minLevel") Integer minLevel, @Param("maxLevel") Integer maxLevel,
            Pageable pageable
    );

    @Query(query)
    long getCountPlayersByFilters(
            @Param("name") String name, @Param("title") String title,
            @Param("race") Race race, @Param("profession")Profession profession,
            @Param("after") Date after, @Param("before") Date before,
            @Param("banned")Boolean banned,
            @Param("minExperience") Integer minExperience, @Param("maxExperience") Integer maxExperience,
            @Param("minLevel") Integer minLevel, @Param("maxLevel") Integer maxLevel
    );
}
