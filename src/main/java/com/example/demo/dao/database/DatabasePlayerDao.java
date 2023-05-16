package com.example.demo.dao.database;

import com.example.demo.dao.PlayerDao;
import com.example.demo.dao.inMemory.PlayerComparator;
import com.example.demo.entity.Player;
import com.example.demo.entity.Race;
import com.example.demo.filter.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

@ConditionalOnProperty(name = "application.save.mode", havingValue = "database")
@Repository
@RequiredArgsConstructor
public class DatabasePlayerDao implements PlayerDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Player> getPlayers() {
        return jdbcTemplate.query("SELECT * FROM players", new PlayerMapper());
    }

    @Override
    public Player createPlayer(Player player) {
        int raceId = getRaceId(player);
        int professionId = getProfessionId(player);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement("insert into Players(name, title, race_id, profession_Id, experience, level, until_next_level, birthday, banned) values (?, ?, ?, ?, ?, ?, ?, ?, ?)", new String[] { "id" });
                ps.setString(1, player.getName());
                ps.setString(2, player.getTitle());
                ps.setLong(3, raceId);
                ps.setLong(4, professionId);
                ps.setLong(5, player.getExperience());
                ps.setLong(6, player.getLevel());
                ps.setLong(7, player.getUntilNextLevel());
                ps.setDate(8,);
                ps.setBoolean(9, player.getBanned());
                return ps;
            }
        }, keyHolder);

        Player player1 = getPlayerById(keyHolder.getKey().longValue());
        return player1;
    }

    @Override
    public void editPlayer(Long id, Player player) {
        int raceId = getRaceId(player);
        int professionId = getProfessionId(player);
        jdbcTemplate.update("UPDATE players SET name = ?, title = ?, race_id = ? , profession_id = ?, experience = ?, level = ?, until_next_level = ?, birthday = ?, banned = ? WHERE id = ?", player.getName(), player.getTitle(), raceId, professionId,
                player.getExperience(), player.getLevel(), player.getUntilNextLevel(), player.getBirthday(), player.getBanned(), id);
    }

    @Override
    public Player deletePlayerById(long id) {
        jdbcTemplate.update("DELETE FROM players WHERE id=?", id);
        return getPlayerById(id);
    }

    @Override
    public Player getPlayerById(long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM players WHERE id=?", new PlayerMapper(), id);
    }

    @Override
    public List<Player> getPlayersByFilter(Filter filter) {
        List<Player> players =  jdbcTemplate.query("SELECT * FROM players", new PlayerMapper());
        Integer filterRaceId = jdbcTemplate.queryForObject("SELECT id FROM race WHERE name = ?", Integer.class, filter.getRace().name());
        Integer filterProfessionId = jdbcTemplate.queryForObject("SELECT id FROM profession WHERE name = ?", Integer.class, filter.getProfession().name());

        return jdbcTemplate.query("SELECT * FROM players WHERE WHERE name = ? and title = ? and race_id = ? and  profession_id = ? and experience >= ? and experience <= ? ane level >= ? and level <= ? and birthday >= ? and birthday <= ? banned = ?",
                new PlayerMapper(), filter.getName(), filter.getTitle(), filterRaceId, filterProfessionId, filter.getMinExperience(), filter.getMaxExperience(), filter.getMinLevel(), filter.getMaxLevel(), filter.getAfter(), filter.getBefore(), filter.getBanned())

    }

    @Override
    public void clear() {
        jdbcTemplate.update("DELETE FROM players");
    }

    private int getRaceId(Player player) {
        return jdbcTemplate.queryForObject("SELECT id FROM race WHERE name = ?", Integer.class, player.getRace().name());
    }

    private int getProfessionId(Player player) {
        return jdbcTemplate.queryForObject("SELECT id FROM profession WHERE name = ?", Integer.class, player.getProfession().name());
    }
}
