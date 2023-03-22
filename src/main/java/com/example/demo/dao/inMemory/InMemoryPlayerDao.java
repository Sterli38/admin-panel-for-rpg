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

//    public InMemoryPlayerDao() {
//
//        Player player = new Player(idGenerate(), "Egor", "title" , Race.HUMAN, Profession.WARRIOR, 1000,
//                10, 5, date, false );
//        createPlayer(player);
//    }

    public List<Player> getPlayers() {
        return new ArrayList<Player>(players.values());
    }

    public void createPlayer(Player player) {
        player.setId(idGenerate());
        players.put(player.getId(), player);
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
                .filter(player -> filter.getAfter() == null && filter.getBefore() == null || player.getBirthday()
                        >= filter.getAfter() && player.getBirthday() <= filter.getBefore())
//                .filter(player -> filter.getBanned() == null || player.getBanned() == filter.getBanned())
                .filter(player -> filter.getMinExperience() == null && filter.getMaxExperience() == null ||
                        player.getExperience() >= filter.getMinExperience() && player.getExperience() <=
                        filter.getMaxExperience())
                .filter(player -> filter.getMinLevel() == null && filter.getMaxLevel() == null || player.getLevel() >=
                        filter.getMinLevel() && player.getLevel() <= filter.getMaxLevel())

                .toList();
    }

    private Long idGenerate() {
        Long min = 0L;
        Long max = 1000000000000000000L;
        return min + (int)(Math.random() * ((max - min) + 1));
    }
}
