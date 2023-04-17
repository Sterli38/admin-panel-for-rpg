package com.example.demo.dao;

import com.example.demo.entity.Player;
import com.example.demo.filter.Filter;

import java.util.List;

public interface PlayerDao {
    List<Player> getPlayers();

    Player createPlayer(Player player);

    void editPlayer(Long id, Player player);

    Player deletePlayerById(long id);

    Player getPlayerById(long id);

    List<Player> getPlayersByFilter(Filter filter);

    void clear();
}