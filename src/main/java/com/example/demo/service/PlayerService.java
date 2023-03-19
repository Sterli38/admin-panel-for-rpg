package com.example.demo.service;

import com.example.demo.dao.inMemory.InMemoryPlayerDao;
import com.example.demo.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PlayerService {

    InMemoryPlayerDao inMemoryPlayerDao;

    @Autowired
    public PlayerService(InMemoryPlayerDao inMemoryPlayerDao) {
        this.inMemoryPlayerDao = inMemoryPlayerDao;
    }

    public void createPlayer(Player player) {
        setCurrentLevel(player);
        setUntilNextLevel(player);
        inMemoryPlayerDao.createPlayer(player);
    }

    public void editPlayer(Player player) {
        setCurrentLevel(player);
        setUntilNextLevel(player);
        inMemoryPlayerDao.editPlayer(player);
    }

    public void deletePlayer(long id) {
        inMemoryPlayerDao.deletePlayerById(id);
    }

    public Player getPlayerById(int id) {
        return inMemoryPlayerDao.getPlayerById(id);
    }

    public List<Player> getPlayersByFilter() {
        return null;
    }

    private void setCurrentLevel(Player player) {
        player.setLevel((int)(Math.sqrt(2500 + 250 * player.getExperience()) - 50 ) / 100);
    }

    private void setUntilNextLevel(Player player) {
        player.setUntilNextLevel(50 * (player.getLevel() + 1) * (player.getLevel() + 2) - player.getExperience());
    }
}
