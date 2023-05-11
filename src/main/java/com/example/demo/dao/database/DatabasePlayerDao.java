package com.example.demo.dao.database;

import com.example.demo.dao.PlayerDao;
import com.example.demo.dao.inMemory.PlayerComparator;
import com.example.demo.entity.Player;
import com.example.demo.entity.Race;
import com.example.demo.filter.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
//        jdbcTemplate.update("INSERT INTO players(name, title, race_id, profession_id, experience, level, until_next_level, birthday, banned) values(?, ?, ?, ?, ?, ?, ?,?,?)",
//                player.getName(), player.getTitle(), raceId, professionId,
//                player.getExperience(), player.getLevel(), player.getUntilNextLevel(), player.getBirthday(), player.getBanned());
//        Player player1 = jdbcTemplate.queryForObject("INSERT INTO players(name, title, race_id, profession_id, experience, level, until_next_level, birthday, banned) values(?, ?, ?, ?, ?, ?, ?,?,?)", new PlayerMapper(),
//                player.getName(), player.getTitle(), raceId, professionId,
//                player.getExperience(), player.getLevel(), player.getUntilNextLevel(), player.getBirthday(), player.getBanned());
        Player player1 = jdbcTemplate.queryForObject("SELECT * FROM players WHERE name = ? and title = ? and race_id = ? and  profession_id = ? and experience = ? and level = ? and until_next_level = ? and  birthday = ? and banned = ?", new PlayerMapper(),
                player.getName(), player.getTitle(), raceId, professionId,
                player.getExperience(), player.getLevel(), player.getUntilNextLevel(), player.getBirthday(), player.getBanned());
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
        return players.stream().
        filter(player -> filter.getName() == null || Pattern.compile(Pattern.quote(filter.getName()), Pattern.CASE_INSENSITIVE).matcher(player.getName()).find())
                .filter(player -> filter.getTitle() == null || player.getTitle().equals(filter.getTitle()))
                .filter(player -> filterRaceId == null || player.getRace() == filter.getRace())
                .filter(player -> filterProfessionId == null || player.getProfession() == filter.getProfession())
                .filter(player -> filter.getAfter() == null && filter.getBefore() == null ||
                        player.getBirthday().getTime() >= filter.getAfter() && player.getBirthday().getTime() <= filter.getBefore())
                .filter(player -> filter.getBanned() == null || player.getBanned() == filter.getBanned())
                .filter(player -> filter.getMinExperience() == null && filter.getMaxExperience() == null ||
                        player.getExperience() >= filter.getMinExperience() &&
                                player.getExperience() <= filter.getMaxExperience())
                .filter(player -> filter.getMinLevel() == null && filter.getMaxLevel() == null ||
                        player.getLevel() >= filter.getMinLevel() && player.getLevel() <= filter.getMaxLevel())
                .sorted(new PlayerComparator(filter))
                .skip(filter.getPageNumber() == null || filter.getPageSize() == null ? 0 : (long) Math.abs((filter.getPageNumber()) * filter.getPageSize()))
                .limit(filter.getPageSize() == null ? Long.MAX_VALUE : filter.getPageSize())
                .toList();
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
