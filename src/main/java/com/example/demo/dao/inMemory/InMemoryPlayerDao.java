package com.example.demo.dao.inMemory;

import com.example.demo.dao.PlayerDao;
import com.example.demo.entity.Player;
import com.example.demo.filter.Filter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class InMemoryPlayerDao implements PlayerDao {

    private final HashMap<Long, Player> players = new HashMap<>();

    public List<Player> getPlayers() {
        return new ArrayList<Player>(players.values());
    }

    public void createPlayer(Player player) {
        players.put(player.getId(), player);
    }

    public void editPlayer(Player player) {
        Player editForPlayer = getPlayerById(player.getId());
        editForPlayer.setName(player.getName());
        editForPlayer.setTitle(player.getTitle());
        editForPlayer.setRace(player.getRace());
        editForPlayer.setProfession(player.getProfession());
        editForPlayer.setExperience(player.getExperience());
        editForPlayer.setLevel(player.getLevel());
        editForPlayer.setUntilNextLevel(player.getUntilNextLevel());
        editForPlayer.setBirthday(player.getBirthday());
        editForPlayer.setBanned(player.isBanned());
    }

    public void deletePlayerById(long id) {
        players.remove(id);
    }

    public Player getPlayerById(long id) {
        return players.get(id);
    }

    public List<Player> getPlayersByFilter(Filter filter) {
        return players.values().stream()
                .filter(player -> filter.getBanned() == null || player.isBanned() == filter.getBanned())
                .toList();
    }
}
