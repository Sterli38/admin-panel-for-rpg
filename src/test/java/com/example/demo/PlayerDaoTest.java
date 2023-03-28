package com.example.demo;

import com.example.demo.dao.PlayerDao;
import com.example.demo.entity.Player;
import com.example.demo.entity.Profession;
import com.example.demo.entity.Race;
import com.example.demo.filter.Filter;
import com.example.demo.filter.PlayerOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public abstract class PlayerDaoTest<T extends PlayerDao> {
    protected T playerDao;

    Player player;
    Player player1;

    @BeforeEach
    void setup() {
        player = new Player();
        player.setName("Egor");
        player.setTitle("Title");
        player.setRace(Race.HUMAN);
        player.setProfession(Profession.WARRIOR);
        player.setExperience(2000);
        player.setLevel(5);
        player.setUntilNextLevel(200);
        player.setBirthday(1679935186L);
        player.setBanned(false);
        playerDao.createPlayer(player);
        player1 = new Player();
        player1.setName("Five");
        player1.setTitle("Title");
        player1.setRace(Race.HUMAN);
        player1.setProfession(Profession.WARRIOR);
        player1.setExperience(2000);
        player1.setLevel(5);
        player1.setUntilNextLevel(200);
        player1.setBirthday(1679935188L);
        player1.setBanned(true);
        playerDao.createPlayer(player1);
    }

    @AfterEach
    void clear() {
        playerDao.deletePlayerById(player.getId());
        playerDao.deletePlayerById(player1.getId());
    }

    @Test
    void getPlayersTest() {
        List<Player> expected = new ArrayList<>();
        Player expectedPlayer = new Player();
        expectedPlayer.setId(0L);
        expectedPlayer.setName("Egor");
        expectedPlayer.setTitle("Title");
        expectedPlayer.setRace(Race.HUMAN);
        expectedPlayer.setProfession(Profession.WARRIOR);
        expectedPlayer.setExperience(2000);
        expectedPlayer.setLevel(5);
        expectedPlayer.setUntilNextLevel(200);
        expectedPlayer.setBirthday(1679935186L);
        expectedPlayer.setBanned(false);
        Player expectedPlayer1 = new Player();
        expectedPlayer1.setId(1L);
        expectedPlayer1.setName("Five");
        expectedPlayer1.setTitle("Title");
        expectedPlayer1.setRace(Race.HUMAN);
        expectedPlayer1.setProfession(Profession.WARRIOR);
        expectedPlayer1.setExperience(2000);
        expectedPlayer1.setLevel(5);
        expectedPlayer1.setUntilNextLevel(200);
        expectedPlayer1.setBirthday(1679935188L);
        expectedPlayer1.setBanned(true);
        expected.add(expectedPlayer);
        expected.add(expectedPlayer1);
        Assertions.assertEquals(expected, playerDao.getPlayers());
    }

    @Test
    void createPlayerTest() {
        Player expectedPlayer = new Player();
        expectedPlayer.setId(2L);
        expectedPlayer.setName("Michael");
        expectedPlayer.setTitle("Title");
        expectedPlayer.setRace(Race.HUMAN);
        expectedPlayer.setProfession(Profession.WARRIOR);
        expectedPlayer.setExperience(5500);
        expectedPlayer.setLevel(6);
        expectedPlayer.setUntilNextLevel(200);
        expectedPlayer.setBirthday(1679935186L);
        expectedPlayer.setBanned(true);
        Player newPLayer = new Player();
        newPLayer.setId(2L);
        newPLayer.setName("Michael");
        newPLayer.setTitle("Title");
        newPLayer.setRace(Race.HUMAN);
        newPLayer.setProfession(Profession.WARRIOR);
        newPLayer.setExperience(5500);
        newPLayer.setLevel(6);
        newPLayer.setUntilNextLevel(200);
        newPLayer.setBirthday(1679935186L);
        newPLayer.setBanned(true);
        playerDao.createPlayer(newPLayer);
        Assertions.assertEquals(expectedPlayer, playerDao.getPlayerById(2));
    }

    @Test
    void editPlayerTest() {
        Player newPlayerData = new Player();
        newPlayerData.setId(0L);
        newPlayerData.setName("Michael");
        newPlayerData.setTitle("Title");
        newPlayerData.setRace(Race.HUMAN);
        newPlayerData.setProfession(Profession.WARRIOR);
        newPlayerData.setExperience(5500);
        newPlayerData.setLevel(6);
        newPlayerData.setUntilNextLevel(200);
        newPlayerData.setBirthday(1679935186L);
        newPlayerData.setBanned(true);
        Player expectedPlayer = newPlayerData;
        playerDao.editPlayer(0L, newPlayerData);
        Assertions.assertEquals(expectedPlayer, playerDao.getPlayerById(0));
    }

    @Test
    void deletePlayerByIdTest() {
        playerDao.deletePlayerById(0);
        Assertions.assertEquals(1L, playerDao.getPlayers().size());
    }

    @Test
    void getPlayerByIdTest() {
        Player expectedPlayer = new Player();
        expectedPlayer.setId(0L);
        expectedPlayer.setName("Egor");
        expectedPlayer.setTitle("Title");
        expectedPlayer.setRace(Race.HUMAN);
        expectedPlayer.setProfession(Profession.WARRIOR);
        expectedPlayer.setExperience(2000);
        expectedPlayer.setLevel(5);
        expectedPlayer.setUntilNextLevel(200);
        expectedPlayer.setBirthday(1679935186L);
        expectedPlayer.setBanned(false);
        Assertions.assertEquals(expectedPlayer, playerDao.getPlayerById(0));
    }

    @Test
    void getPlayersByFilterTest() {
        Player expectedPlayer = new Player();
        expectedPlayer.setId(0L);
        expectedPlayer.setName("Egor");
        expectedPlayer.setTitle("Title");
        expectedPlayer.setRace(Race.HUMAN);
        expectedPlayer.setProfession(Profession.WARRIOR);
        expectedPlayer.setExperience(2000);
        expectedPlayer.setLevel(5);
        expectedPlayer.setUntilNextLevel(200);
        expectedPlayer.setBirthday(1679935186L);
        expectedPlayer.setBanned(false);

        Player expectedPlayer1 = new Player();
        expectedPlayer1.setId(1L);
        expectedPlayer1.setName("Five");
        expectedPlayer1.setTitle("Title");
        expectedPlayer1.setRace(Race.HUMAN);
        expectedPlayer1.setProfession(Profession.WARRIOR);
        expectedPlayer1.setExperience(2000);
        expectedPlayer1.setLevel(5);
        expectedPlayer1.setUntilNextLevel(200);
        expectedPlayer1.setBirthday(1679935188L);
        expectedPlayer1.setBanned(true);


        List<Player> expected = new ArrayList<>();
        Filter filter = new Filter();
        filter.setName("Five");
        expected.add(expectedPlayer1);
        Assertions.assertEquals(expected, playerDao.getPlayersByFilter(filter));

        List<Player> expected1 = new ArrayList<>();
        Filter filter1 = new Filter();
        filter1.setTitle("Title");
        expected1.add(expectedPlayer);
        expected1.add(expectedPlayer1);
        Assertions.assertEquals(expected1, playerDao.getPlayersByFilter(filter1));

        List<Player> expected2 = new ArrayList<>();
        Filter filter2 = new Filter();
        filter2.setRace(Race.HUMAN);
        expected2.add(expectedPlayer);
        expected2.add(expectedPlayer1);
        Assertions.assertEquals(expected2, playerDao.getPlayersByFilter(filter2));

        List<Player> expected3 = new ArrayList<>();
        Filter filter3 = new Filter();
        filter3.setProfession(Profession.WARRIOR);
        expected3.add(expectedPlayer);
        expected3.add(expectedPlayer1);
        Assertions.assertEquals(expected3, playerDao.getPlayersByFilter(filter3));

        List<Player> expected4 = new ArrayList<>();
        Filter filter4 = new Filter();
        filter4.setAfter(1679591394L);
        filter4.setBefore(1680109794L);
        expected4.add(expectedPlayer);
        expected4.add(expectedPlayer1);
        Assertions.assertEquals(expected4, playerDao.getPlayersByFilter(filter4));

        List<Player> expected5 = new ArrayList<>();
        expected5.add(expectedPlayer1);
        Filter filter5 = new Filter();
        filter5.setBanned(true);
        Assertions.assertEquals(expected5, playerDao.getPlayersByFilter(filter5));

        List<Player> expected6 = new ArrayList<>();
        Filter filter6 = new Filter();
        filter6.setMinExperience(1000);
        filter6.setMaxExperience(3000);
        expected6.add(expectedPlayer);
        expected6.add(expectedPlayer1);
        Assertions.assertEquals(expected6, playerDao.getPlayersByFilter(filter6));

        List<Player> expected7 = new ArrayList<>();
        Filter filter7 = new Filter();
        filter7.setMinLevel(3);
        filter7.setMaxLevel(7);
        expected7.add(expectedPlayer);
        expected7.add(expectedPlayer1);
        Assertions.assertEquals(expected7, playerDao.getPlayersByFilter(filter7));

    }
}
