package com.example.demo.dao;

import com.example.demo.entity.Player;

import java.util.List;

public interface PlayerDao {
    List<Player> getPlayers();

    void createPlayer(Player player);

    void editPlayer(Player player);

    void deletePlayerById(long id);

    Player getPlayerById(long id);

    List<Player> getPlayersByFilter();
}
