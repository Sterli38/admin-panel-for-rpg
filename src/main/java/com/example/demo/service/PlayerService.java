package com.example.demo.service;

import com.example.demo.dao.PlayerDao;
import com.example.demo.dao.inMemory.InMemoryPlayerDao;
import com.example.demo.entity.Player;
import com.example.demo.filter.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerDao playerDao;

    public List<Player> getPlayers() {
        return playerDao.getPlayers();
    }

    public void createPlayer(Player player) {
        setCurrentLevel(player);
        setUntilNextLevel(player);
        playerDao.createPlayer(player);
    }

    public void editPlayer(Player player) {
        setCurrentLevel(player);
        setUntilNextLevel(player);
        playerDao.editPlayer(player);
    }

    public void deletePlayerById(long id) {
        playerDao.deletePlayerById(id);
    }

    public Player getPlayerById(long id) {
        return playerDao.getPlayerById(id);
    }

    public List<Player> getPlayersByFilter(Filter filter) {
        return playerDao.getPlayersByFilter(filter);
    }

    private void setCurrentLevel(Player player) {
        player.setLevel((int)(Math.sqrt(2500 + 250 * player.getExperience()) - 50 ) / 100);
    }

    private void setUntilNextLevel(Player player) {
        player.setUntilNextLevel(50 * (player.getLevel() + 1) * (player.getLevel() + 2) - player.getExperience());
    }
}
