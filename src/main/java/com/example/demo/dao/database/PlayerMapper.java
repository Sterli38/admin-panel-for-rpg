package com.example.demo.dao.database;

import com.example.demo.entity.Player;
import com.example.demo.entity.Profession;
import com.example.demo.entity.Race;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
public class PlayerMapper implements RowMapper<Player> {
//    private final JdbcTemplate jdbcTemplate;

    @Override
    public Player mapRow(ResultSet rs, int rowNum) throws SQLException {
        Player player = new Player();

        player.setId(rs.getLong("id"));
        player.setName(rs.getString("name"));
        player.setTitle(rs.getString("title"));
        player.setRace(Race.valueOf(rs.getString("raceName")));
        player.setProfession(Profession.valueOf(rs.getString("ProfessionName")));
        player.setExperience( rs.getInt("experience"));
        player.setLevel(rs.getInt("level"));
        player.setUntilNextLevel(rs.getInt("until_next_level"));
        player.setBirthday(rs.getTimestamp("birthday"));
        player.setBanned(rs.getBoolean("banned"));
        return player;
    }
}
