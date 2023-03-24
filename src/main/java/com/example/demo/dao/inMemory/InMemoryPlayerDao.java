package com.example.demo.dao.inMemory;

import com.example.demo.dao.PlayerDao;
import com.example.demo.entity.Player;
import com.example.demo.entity.Profession;
import com.example.demo.entity.Race;
import com.example.demo.filter.Filter;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryPlayerDao implements PlayerDao {

    private final HashMap<Long, Player> players = new HashMap<>();
    private long idGenerator = 0;

    public InMemoryPlayerDao() {
        Long date = 1679639551000L;
        Player player = new Player();
        player.setName("Egor");
        player.setTitle("Title");
        player.setRace(Race.HUMAN);
        player.setProfession(Profession.WARRIOR);
        player.setExperience(1000);
        player.setLevel(5);
        player.setUntilNextLevel(10);
        player.setBirthday(date);
        player.setBanned(true);
        createPlayer(player);
        createPlayer(player);
        createPlayer(player);
        createPlayer(player);
        createPlayer(player);
        createPlayer(player);
        createPlayer(player);
        createPlayer(player);
        createPlayer(player);
        createPlayer(player);
        createPlayer(player);
        createPlayer(player);
        createPlayer(player);
        createPlayer(player);
        createPlayer(player);
        createPlayer(player);
        createPlayer(player);


    }

    public List<Player> getPlayers() {
        return new ArrayList<Player>(players.values());
    }

    public void createPlayer(Player player) {
        player.setId(idGenerator);
        players.put(player.getId(), player);
        idGenerator++;
    }

    public void editPlayer(Long id, Player player) {
        Player editForPlayer = getPlayerById(id);
        editForPlayer.setName(player.getName());
        editForPlayer.setTitle(player.getTitle());
        editForPlayer.setRace(player.getRace());
        editForPlayer.setProfession(player.getProfession());
        editForPlayer.setExperience(player.getExperience());
        editForPlayer.setLevel(player.getLevel());
        editForPlayer.setUntilNextLevel(player.getUntilNextLevel());
        editForPlayer.setBirthday(player.getBirthday());
        editForPlayer.setBanned(player.getBanned());
    }

    public void deletePlayerById(long id) {
        players.remove(id);
    }

    public Player getPlayerById(long id) {
        return players.get(id);
    }

    public List<Player> getPlayersByFilter(Filter filter) {
        return players.values().stream()
                .filter(player -> filter.getName() == null || player.getName().equals(filter.getName()))
                .filter(player -> filter.getTitle() == null || player.getTitle().equals(filter.getTitle()))
                .filter(player -> filter.getRace() == null || player.getRace() == filter.getRace())
                .filter(player -> filter.getProfession() == null || player.getProfession() == filter.getProfession())
                .filter(player -> filter.getAfter() == null && filter.getBefore() == null ||
                        player.getBirthday() >= filter.getAfter() && player.getBirthday() <= filter.getBefore())
                .filter(player -> filter.getBanned() == null || player.getBanned() == filter.getBanned())
                .filter(player -> filter.getMinExperience() == null && filter.getMaxExperience() == null ||
                        player.getExperience() >= filter.getMinExperience() &&
                                player.getExperience() <= filter.getMaxExperience())
                .filter(player -> filter.getMinLevel() == null && filter.getMaxLevel() == null ||
                        player.getLevel() >= filter.getMinLevel() && player.getLevel() <= filter.getMaxLevel())
                .skip(filter.getPageNumber() == null || filter.getPageSize() == null ? 0 : (long) Math.abs((filter.getPageNumber() - 1) * filter.getPageSize()))
                .limit(filter.getPageSize() == null ? Long.MAX_VALUE : filter.getPageSize())
                .toList();
    }

    private Long idGenerate() {
        Long min = 0L;
        Long max = 1000000000000000000L;
        return min + (int)(Math.random() * ((max - min) + 1));
    }
}
