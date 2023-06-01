package com.example.demo.dao.database;

import com.example.demo.dao.PlayerDao;
import com.example.demo.dao.inMemory.PlayerComparator;
import com.example.demo.entity.Player;
import com.example.demo.filter.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConditionalOnProperty(name = "application.save.mode", havingValue = "database")
@Repository
@RequiredArgsConstructor
public class DatabasePlayerDao implements PlayerDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Player> getPlayers() {
        String sql = "SELECT players.id, players.name, title, race.name as raceName, profession.name as professionName, experience, level, until_next_Level, birthday, banned  " +
                "FROM players INNER JOIN race on players.race_id = race.id " +
                "INNER JOIN profession on players.profession_id = profession.id";
        return jdbcTemplate.query(sql, new PlayerMapper());
    }

    @Override
    public Player createPlayer(Player player) {
        int raceId = getRaceId(player.getRace().name());
        int professionId = getProfessionId(player.getProfession().name());
        String sql = "insert into Players(name, title, race_id, profession_Id, experience, level, until_next_level, birthday, banned) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, player.getName());
            ps.setString(2, player.getTitle());
            ps.setLong(3, raceId);
            ps.setLong(4, professionId);
            ps.setLong(5, player.getExperience());
            ps.setLong(6, player.getLevel());
            ps.setLong(7, player.getUntilNextLevel());
            ps.setTimestamp(8, new Timestamp(player.getBirthday().getTime()));
            ps.setBoolean(9, player.getBanned());
            return ps;
        }, keyHolder);
        Player player1 = getPlayerById(keyHolder.getKey().longValue());
        return player1;
    }

    @Override
    public void editPlayer(Long id, Player player) {
        int raceId = getRaceId(player.getRace().name());
        int professionId = getProfessionId(player.getProfession().name());
        String sql = "UPDATE players SET name = ?, title = ?, race_id = ? , profession_id = ?, experience = ?, level = ?, until_next_level = ?, birthday = ?, banned = ? WHERE id = ?";
        jdbcTemplate.update(sql, player.getName(), player.getTitle(), raceId, professionId,
                player.getExperience(), player.getLevel(), player.getUntilNextLevel(), player.getBirthday(), player.getBanned(), id);
    }

    @Override
    public Player deletePlayerById(long id) {
        Player returnPlayer = getPlayerById(id);
        String sql = "DELETE FROM players WHERE id=?";
        jdbcTemplate.update(sql, id);
        return returnPlayer;
    }

    @Override
    public Player getPlayerById(long id) {
        String sql = "SELECT players.id, players.name, title, race.name as raceName, profession.name as professionName, experience, level, until_next_Level, birthday, banned  " +
                "FROM players INNER JOIN race on players.race_id = race.id " +
                "INNER JOIN profession on players.profession_id = profession.id WHERE players.id = ?";
        return jdbcTemplate.queryForObject(sql, new PlayerMapper(), id);
    }

    @Override
    public List<Player> getPlayersByFilter(Filter filter) {
        Map<String, Object> values = new HashMap<>();
        SqlBuilder sqlBuilder = new SqlBuilder();
        sqlBuilder
                .select("players.id, players.name, title, race.name as raceName, profession.name as professionName, experience, level, until_next_Level, birthday, banned")
                .from("players INNER JOIN race on players.race_id = race.id INNER JOIN profession on players.profession_id = profession.id");
        if (filter.getName() != null) {
            String conditionName = (new StringBuilder(filter.getName())).insert(0, "%").append("%").toString();
            sqlBuilder.where("players.name ILIKE :name");
            values.put("name", conditionName);
        }
        if (filter.getTitle() != null) {
            String conditionTitle = (new StringBuilder(filter.getTitle())).insert(0, "%").append("%").toString();
            sqlBuilder.where("title ILIKE :title");
            values.put("title", conditionTitle);
        }
        if (filter.getRace() != null) {
            sqlBuilder.where("race.name = :raceName");
            values.put("raceName", filter.getRace().name());
        }
        if (filter.getProfession() != null) {
            sqlBuilder.where("profession.name = :professionName");
            values.put("professionName", filter.getProfession().name());
        }
        if (filter.getMinExperience() != null) {
            sqlBuilder.where("experience >= :minExperience");
            values.put("minExperience", filter.getMinExperience());
        }
        if (filter.getMaxExperience() != null) {
            sqlBuilder.where("experience <= :maxExperience");
            values.put("maxExperience", filter.getMaxExperience());
        }
        if (filter.getMinLevel() != null) {
            sqlBuilder.where("level >= :minLevel");
            values.put("minLevel", filter.getMinLevel());
        }
        if (filter.getMaxLevel() != null) {
            sqlBuilder.where("level <= :maxLevel");
            values.put("maxLevel", filter.getMaxLevel());
        }
        if (filter.getAfter() != null) {
            sqlBuilder.where("birthday >= :after");
            values.put("after", filter.getAfter());
        }
        if (filter.getBefore() != null) {
            sqlBuilder.where("birthday <= :before");
            values.put("before", filter.getBefore());
        }
        if (filter.getBanned() != null) {
            sqlBuilder.where("banned = :banned");
            values.put("banned", filter.getBanned());
        }
        sqlBuilder.build();
        if (filter.getOrder() != null) {
            sqlBuilder.condition(" ORDER BY ", filter.getOrder().name());
        }
        if (filter.getPageSize() != null) {
            sqlBuilder.condition(" LIMIT ", String.valueOf(filter.getPageSize()));
        }
        if (filter.getPageNumber() != null) {
            int condition = filter.getPageNumber() * filter.getPageSize();
            sqlBuilder.condition(" OFFSET ", String.valueOf(condition));
        }
        String sql = sqlBuilder.getSQL();
        List<Player> players = namedParameterJdbcTemplate.query(sql, values, new PlayerMapper());
        return players;
    }

    @Override
    public void clear() {
        jdbcTemplate.update("DELETE FROM players");
    }

    private int getRaceId(String raceName) {
        String sql = "SELECT id FROM race WHERE name = ?";
        if (raceName == null) {
            return 0;
        }
        return jdbcTemplate.queryForObject(sql, Integer.class, raceName);
    }

    private int getProfessionId(String professionName) {
        String sql = "SELECT id FROM profession WHERE name = ?";
        if (professionName == null) {
            return 0;
        }
        return jdbcTemplate.queryForObject(sql, Integer.class, professionName);
    }
}
