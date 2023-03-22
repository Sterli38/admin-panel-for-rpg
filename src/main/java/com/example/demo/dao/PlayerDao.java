package com.example.demo.dao;

import com.example.demo.entity.Player;
import com.example.demo.filter.Filter;

import java.util.List;

public interface PlayerDao {
    List<Player> getPlayers();

    void createPlayer(Player player);

    void editPlayer(Long id, Player player);

    void deletePlayerById(long id);

    Player getPlayerById(long id);

    List<Player> getPlayersByFilter(Filter filter);
}