package com.example.demo.manager;

import com.example.demo.entity.Player;

import java.util.List;

public interface Manager {
    List<Player> getPlayers();

    void createPlayer(Player player);

    void editPlayer(Player player);

    void deletePlayer(Player player);

    Player getPlayerById(int id);

    List<Player> getPlayersByFilter();
}
