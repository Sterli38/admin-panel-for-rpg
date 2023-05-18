package com.example.demo.dao.database;

import com.example.demo.dao.PlayerDao;
import com.example.demo.entity.Player;
import com.example.demo.filter.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ConditionalOnProperty(name = "application.save.mode", havingValue = "database")
@Repository
@RequiredArgsConstructor
public class DatabasePlayerDao implements PlayerDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Player> getPlayers() {
        return jdbcTemplate.query("SELECT * FROM players", new PlayerMapper(jdbcTemplate));
    }

    @Override
    public Player createPlayer(Player player) {
        int raceId = getRaceId(player.getRace().name());
        int professionId = getProfessionId(player.getProfession().name());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("insert into Players(name, title, race_id, profession_Id, experience, level, until_next_level, birthday, banned) values (?, ?, ?, ?, ?, ?, ?, ?, ?)", new String[]{"id"});
            ps.setString(1, player.getName());
            ps.setString(2, player.getTitle());
            ps.setLong(3, raceId);
            ps.setLong(4, professionId);
            ps.setLong(5, player.getExperience());
            ps.setLong(6, player.getLevel());
            ps.setLong(7, player.getUntilNextLevel());
            ps.setDate(8, new Date(player.getBirthday().getTime()));
            ps.setBoolean(9, player.getBanned());
            return ps;
        }, keyHolder);

        Player player1 = getPlayerById(keyHolder.getKey().longValue());
        return player1;
    }

    @Override
    public void editPlayer(Long id, Player player) {
        int raceId = getRaceId(player.getName());
        int professionId = getProfessionId(player.getName());
        jdbcTemplate.update("UPDATE players SET name = ?, title = ?, race_id = ? , profession_id = ?, experience = ?, level = ?, until_next_level = ?, birthday = ?, banned = ? WHERE id = ?", player.getName(), player.getTitle(), raceId, professionId,
                player.getExperience(), player.getLevel(), player.getUntilNextLevel(), player.getBirthday(), player.getBanned(), id);
    }

    @Override
    public Player deletePlayerById(long id) {
        Player returnPlayer = getPlayerById(id);
        jdbcTemplate.update("DELETE FROM players WHERE id=?", id);
        return returnPlayer;
    }

    @Override
    public Player getPlayerById(long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM players WHERE id=?", new PlayerMapper(jdbcTemplate), id);
    }

    @Override
    public List<Player> getPlayersByFilter(Filter filter) {
//        String conditionName = (new StringBuilder(filter.getName())).insert(0, "%").append("%").toString();
        List<Object> filters = new ArrayList<>();
        Integer filterRaceId = getRaceId(checkEmptyFilter(filter.getRace()));
        Integer filterProfessionId = getProfessionId(checkEmptyFilter(filter.getProfession()));
        SqlBuilder sqlBuilder = new SqlBuilder();
        sqlBuilder
                .select("*")
                .from("players");
        if (filter.getName() != null) {
//            sqlBuilder.where("name LIKE");
//            filters.add(conditionName);
            sqlBuilder.where("name = ?");
            filters.add(filter.getName());
        }
        if (filter.getTitle() != null) {
            sqlBuilder.where("title = ?");
            filters.add(filter.getTitle());
        }
        if (filter.getRace() != null) {
            sqlBuilder.where("race_id = ?");
            filters.add(filterRaceId);
        }
        if (filter.getProfession() != null) {
            sqlBuilder.where("profession_id = ?");
            filters.add(filterProfessionId);
        }
        if (filter.getMinExperience() != null) {
            sqlBuilder.where("experience >= ?");
            filters.add(filter.getMinExperience());
        }
        if (filter.getMaxExperience() != null) {
            sqlBuilder.where("experience <= ?");
            filters.add(filter.getMaxExperience());
        }
        if (filter.getMinLevel() != null) {
            sqlBuilder.where("level >= ?");
            filters.add(filter.getMinLevel());
        }
        if (filter.getMaxLevel() != null) {
            sqlBuilder.where("level <= ?");
            filters.add(filter.getMaxLevel());
        }
        if (filter.getAfter() != null) {
            sqlBuilder.where("birthday >= ?");
            filters.add(new Date(filter.getAfter()));
        }
        if (filter.getBefore() != null) {
            sqlBuilder.where("birthday <= ?");
            filters.add(new Date(filter.getBefore()));
        }
        if (filter.getBanned() != null) {
            sqlBuilder.where("banned = ?");
            filters.add(filter.getBanned());
        }
        String sql = sqlBuilder.build();
            return jdbcTemplate.query(sql, new PlayerMapper(jdbcTemplate), filters.toArray());
    }

    @Override
    public void clear() {
        jdbcTemplate.update("DELETE FROM players");
    }

    private int getRaceId(String raceName) {
        if (raceName == null) {
            return 0;
        }
        return jdbcTemplate.queryForObject("SELECT id FROM race WHERE name = ?", Integer.class, raceName);
    }

    private int getProfessionId(String professionName) {
        if (professionName == null) {
            return 0;
        }
        return jdbcTemplate.queryForObject("SELECT id FROM profession WHERE name = ?", Integer.class, professionName);
    }

    private String checkEmptyFilter(Enum filter) { // Проверяет что фильтр не пуст
        if(filter != null) {
            return filter.name();
        }
        return null;
    }
}
