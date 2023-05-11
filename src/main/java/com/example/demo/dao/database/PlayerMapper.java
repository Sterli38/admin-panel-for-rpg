package com.example.demo.dao.database;

import com.example.demo.entity.Player;
import com.example.demo.entity.Profession;
import com.example.demo.entity.Race;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerMapper implements RowMapper<Player> {

    @Override
    public Player mapRow(ResultSet rs, int rowNum) throws SQLException {
        Player player = new Player();

        player.setId(rs.getLong("id"));
        player.setName(rs.getString("name"));
        player.setTitle(rs.getString("title"));
//        player.setRace(Race.valueOf(rs.getString("race_id")));
//        player.setProfession(Profession.valueOf(rs.getString("profession_id")));
        player.setExperience( rs.getInt("experience"));
        player.setLevel(rs.getInt("level"));
        player.setUntilNextLevel(rs.getInt("until_next_level"));
        player.setBirthday(rs.getDate("birthday"));
        player.setBanned(rs.getBoolean("banned"));

        return player;
    }
}
