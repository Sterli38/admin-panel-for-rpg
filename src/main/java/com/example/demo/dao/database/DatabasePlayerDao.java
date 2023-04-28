package com.example.demo.dao.database;

import com.example.demo.dao.PlayerDao;
import com.example.demo.entity.Player;
import com.example.demo.filter.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("DatabaseDao")
@RequiredArgsConstructor
public class DatabasePlayerDao implements PlayerDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Player> getPlayers() {
        return jdbcTemplate.query("SELECT * FROM players", new PlayerMapper());
    }

    @Override
    public Player createPlayer(Player player) {
        jdbcTemplate.update("INSERT INTO players(name, title, race_id, profession_id, experience, level, until_next_level, birthday, banned) values(?, ?, ?, ?, ?, ?, ?,?,?)",
                player.getName(), player.getTitle(), player.getRace(), player.getRace(), player.getProfession(),
                player.getExperience(), player.getLevel(), player.getUntilNextLevel(), player.getBirthday(), player.getBanned());


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
        return jdbcTemplate.query("SELECT * FROM players WHERE id=?", )
    }

    @Override
    public List<Player> getPlayersByFilter(Filter filter) {

    }

    @Override
    public void clear() {
        jdbcTemplate.update("DELETE FROM players");
    }
}
