package com.example.demo.dao.inMemory;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import com.example.demo.dao.PlayerDao;
import com.example.demo.entity.Player;
import com.example.demo.entity.Profession;
import com.example.demo.entity.Race;
import com.example.demo.filter.Filter;

import com.example.demo.filter.PlayerOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ConditionalOnProperty(name = "application.save.mode", havingValue = "inMemory")
@Repository
public class InMemoryPlayerDao implements PlayerDao {
    private final HashMap<Long, Player> players = new HashMap<>();
    private long idGenerator = 0;

    public List<Player> getPlayers() {
        return new ArrayList<Player>(players.values());
    }

    public Player createPlayer(Player player) {
        player.setId(idGenerator);
        players.put(player.getId(), player);
        idGenerator++;
        return player;
    }

    public void editPlayer(Long id, Player player) {
        Player editForPlayer = getPlayerById(id);
        if(player.getName() != null) {
            editForPlayer.setName(player.getName());
        }
        if(player.getTitle() != null) {
            editForPlayer.setTitle(player.getTitle());
        }
        if(player.getRace() != null) {
            editForPlayer.setRace(player.getRace());
        }
        if(player.getProfession() != null) {
            editForPlayer.setProfession(player.getProfession());
        }
        if(player.getExperience() != null) {
            Integer experience = player.getExperience();
            editForPlayer.setExperience(experience);
            editForPlayer.setLevel((int)(Math.sqrt(2500 + 250 * experience) - 50 ) / 100);
            editForPlayer.setUntilNextLevel(50 * (player.getLevel() + 1) * (player.getLevel() + 2) - player.getExperience());
        }
        if(player.getBirthday() != null) {
            editForPlayer.setBirthday(player.getBirthday());
        }
        if(player.getBanned() != null) {
            editForPlayer.setBanned(player.getBanned());
        }
    }

    public Player deletePlayerById(long id) {
        return players.remove(id);
    }

    public Player getPlayerById(long id) {
        return players.get(id);
    }

    public List<Player> getPlayersByFilter(Filter filter) {
        return players.values().stream()
                .filter(player -> filter.getName() == null || Pattern.compile(Pattern.quote(filter.getName()), Pattern.CASE_INSENSITIVE).matcher(player.getName()).find())
                .filter(player -> filter.getTitle() == null || Pattern.compile(Pattern.quote(filter.getTitle()), Pattern.CASE_INSENSITIVE).matcher(player.getTitle()).find())
                .filter(player -> filter.getRace() == null || player.getRace() == filter.getRace())
                .filter(player -> filter.getProfession() == null || player.getProfession() == filter.getProfession())
                .filter(player -> filter.getAfter() == null && filter.getBefore() == null ||
                        player.getBirthday().after(filter.getAfter()) && player.getBirthday().before(filter.getBefore()))
                .filter(player -> filter.getBanned() == null || player.getBanned() == filter.getBanned())
                .filter(player -> filter.getMinExperience() == null && filter.getMaxExperience() == null ||
                        player.getExperience() >= filter.getMinExperience() &&
                                player.getExperience() <= filter.getMaxExperience())
                .filter(player -> filter.getMinLevel() == null && filter.getMaxLevel() == null ||
                        player.getLevel() >= filter.getMinLevel() && player.getLevel() <= filter.getMaxLevel())
                .sorted(new PlayerComparator(filter))
                .skip(filter.getPageNumber() == null || filter.getPageSize() == null ? 0 : (long) Math.abs((filter.getPageNumber()) * filter.getPageSize()))
                .limit(filter.getPageSize() == null ? Long.MAX_VALUE : filter.getPageSize())
                .toList();
    }

    public void clear() {
        players.clear();
    }
}
