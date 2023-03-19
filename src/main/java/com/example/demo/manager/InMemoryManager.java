package com.example.demo.manager;

import com.example.demo.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryManager implements Manager {

    private final HashMap<Integer, Player> players = new HashMap<>();

    public List<Player> getPlayers() {
        return new ArrayList<Player>(players.values());
    }

    public void createPlayer(Player player) {
        currentLevel(player);
        untilNextLevel(player);
        players.put(player.getId(), player);
    }

    public void editPlayer(Player player) {
        currentLevel(player);
        untilNextLevel(player);
    }

    public void deletePlayer(Player player) {
        players.remove(player.getId());
    }

    public Player getPlayerById(int id) {
        return players.get(id);
    }

    public List<Player> getPlayersByFilter() {
        return null;
    }

    public void currentLevel(Player player) {
        player.setLevel((int)(Math.sqrt(2500 + 250 * player.getExperience()) - 50 ) / 100);
    }

    public void untilNextLevel(Player player) {
        player.setUntilNextLevel(50 * (player.getLevel() + 1) * (player.getLevel() + 2) - player.getExperience());
    }
}
