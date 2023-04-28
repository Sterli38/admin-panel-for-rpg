package com.example.demo.dao.database;

import com.example.demo.dao.PlayerDao;
import com.example.demo.entity.Player;
import com.example.demo.entity.Race;
import com.example.demo.filter.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        int raceId = jdbcTemplate.queryForObject("SELECT id FROM race WHERE name = ?", Integer.class, player.getRace().name());
        int professionId = jdbcTemplate.queryForObject("SELECT id FROM profession WHERE name = ?", Integer.class, player.getProfession().name());
        jdbcTemplate.update("INSERT INTO players(name, title, race_id, profession_id, experience, level, until_next_level, birthday, banned) values(?, ?, ?, ?, ?, ?, ?,?,?)",
                player.getName(), player.getTitle(), raceId, professionId,
                player.getExperience(), player.getLevel(), player.getUntilNextLevel(), player.getBirthday(), player.getBanned());
        return null;
    }

    @Override
    public void editPlayer(Long id, Player player) {
        jdbcTemplate.update("UPDATE players SET name=?,title=?,race_id=?,profession_id=?,experience=?,level=?,until_next_level=,birthday?,banned=?", player.getName(), player.getTitle(), player.getRace(), player.getRace(), player.getProfession(),
                player.getExperience(), player.getLevel(), player.getUntilNextLevel(), player.getBirthday(), player.getBanned());
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
        return null;
    }

    @Override
    public void clear() {
        jdbcTemplate.update("DELETE FROM players");
    }
}
