package com.example.demo.service;

import com.example.demo.dao.PlayerDao;
import com.example.demo.entity.Player;
import com.example.demo.filter.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerDao inMemoryDao;

    public List<Player> getPlayers() {
        return inMemoryDao.getPlayers();
    }

    public Player createPlayer(Player player) {
        setCurrentLevel(player);
        setUntilNextLevel(player);
        return inMemoryDao.createPlayer(player);
    }

    public void editPlayer(Long id, Player player) {
        setCurrentLevel(player);
        setUntilNextLevel(player);
        inMemoryDao.editPlayer(id ,player);
    }

    public Player deletePlayerById(long id) {
        return inMemoryDao.deletePlayerById(id);
    }

    public Player getPlayerById(long id) {
        return inMemoryDao.getPlayerById(id);
    }

    public List<Player> getPlayersByFilter(Filter filter) {
        return inMemoryDao.getPlayersByFilter(filter);
    }

    private void setCurrentLevel(Player player) {
        player.setLevel((int)(Math.sqrt(2500 + 250 * player.getExperience()) - 50 ) / 100);
    }

    private void setUntilNextLevel(Player player) {
        player.setUntilNextLevel(50 * (player.getLevel() + 1) * (player.getLevel() + 2) - player.getExperience());
    }
}
