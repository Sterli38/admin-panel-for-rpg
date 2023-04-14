package com.example.demo;


import com.example.demo.controller.PanelController;
import com.example.demo.entity.Player;
import com.example.demo.entity.Profession;
import com.example.demo.entity.Race;
import com.example.demo.service.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class PanelControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PanelController controller;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    PlayerService service;
    private Long idVasiliy;
    private Long idGeorgy;
    private Long idLena;
    private static Player playerVasiliy = new Player();
    private static Player playerGeorgy = new Player();
    private static Player playerLena = new Player();
    private static Player player;
    private static Player updateForPlayer;
    private static Player PlayerForUpdate;

    @BeforeAll
    public static void createPlayer() {
        playerVasiliy = new Player();
        playerVasiliy.setName("Vasiliy");
        playerVasiliy.setTitle("Title");
        playerVasiliy.setRace(Race.HUMAN);
        playerVasiliy.setProfession(Profession.WARRIOR);
        playerVasiliy.setExperience(1000);
        playerVasiliy.setBirthday(1609874962000L);
        playerVasiliy.setBanned(true);
        playerGeorgy = new Player();
        playerGeorgy.setName("Georgy");
        playerGeorgy.setTitle("TitleTitle");
        playerGeorgy.setRace(Race.ORC);
        playerGeorgy.setProfession(Profession.WARRIOR);
        playerGeorgy.setExperience(987);
        playerGeorgy.setBirthday(1672946962000L);
        playerGeorgy.setBanned(true);
        playerLena = new Player();
        playerLena.setName("Lena");
        playerLena.setTitle("TitleTitle");
        playerLena.setRace(Race.HUMAN);
        playerLena.setProfession(Profession.PALADIN);
        playerLena.setExperience(1500);
        playerLena.setBirthday(1670941959000L);
        playerLena.setBanned(false);
    }

    @BeforeEach
    public void addPlayer() {
        service.createPlayer(playerVasiliy);
        service.createPlayer(playerGeorgy);
        service.createPlayer(playerLena);
        idVasiliy = playerVasiliy.getId();
        idGeorgy = playerGeorgy.getId();
        idLena = playerLena.getId();
    }

    @AfterEach
    public void deletePlayer() {
        try {
            service.deletePlayerById(idVasiliy);
            service.deletePlayerById(idGeorgy);
            service.deletePlayerById(idLena);
            service.deletePlayerById(player.getId());
        } catch (Exception ignore) {
        }
    }


    @Test
    public void getPlayersByFilterTestNameTesting() throws Exception {
        this.mockMvc.perform(get("/rest/players/").param("name", "eor"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(playerGeorgy))));
    }

    @Test
    public void getPlayersByFilterTestTitleTesting() throws Exception {
        this.mockMvc.perform(get("/rest/players/").param("title", "TitleTitle"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(playerGeorgy, playerLena))));
    }

    @Test
    public void getPlayersByFilterTestRaceTesting() throws Exception {
        this.mockMvc.perform(get("/rest/players/").param("race", "HUMAN"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(playerVasiliy, playerLena))));
    }

    @Test
    public void getPlayersByFilterTestProfessionTesting() throws Exception {
        this.mockMvc.perform(get("/rest/players/").param("profession", "WARRIOR"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(playerVasiliy, playerGeorgy))));
    }

    @Test
    public void getPlayersByFilterTestBirthdayTesting() throws Exception {
        this.mockMvc.perform(get("/rest/players/").param("before", "1672946964000").param("after", "1668349959000"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(playerGeorgy, playerLena))));
    }

    @Test
    public void getPlayersByFilterTestLevelTesting() throws Exception {
        this.mockMvc.perform(get("/rest/players/").param("minLevel", "1").param("maxLevel", "4"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(playerVasiliy, playerGeorgy))));
    }

    @Test
    public void getPlayersByFilterTestExperienceTesting() throws Exception {
        this.mockMvc.perform(get("/rest/players/").param("minExperience", "500").param("maxExperience", "1300"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(playerVasiliy, playerGeorgy))));
    }

    @Test
    public void getPlayersByFilterTestBannedTesting() throws Exception {
        this.mockMvc.perform(get("/rest/players/").param("banned", "false"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(playerLena))));
    }

    @Test
    public void getPlayersCountTest() throws Exception {
        this.mockMvc.perform(get("/rest/players/count").param("banned", "true"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(playerGeorgy, playerLena).size())));
    }

    @Test
    public void createPlayerTest() throws Exception {
        player = new Player();
        player.setName("player");
        player.setTitle("Title");
        player.setRace(Race.HUMAN);
        player.setProfession(Profession.WARRIOR);
        player.setExperience(1000);
        player.setBirthday(1609874962000L);
        player.setBanned(true);
        mockMvc.perform(post("/rest/players/")
                        .content(objectMapper.writeValueAsString(player))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("player"))
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.race").value(Race.HUMAN.name()))
                .andExpect(jsonPath("$.profession").value(Profession.WARRIOR.name()))
                .andExpect(jsonPath("$.experience").value(1000))
                .andExpect(jsonPath("$.level").value(4))
                .andExpect(jsonPath("$.untilNextLevel").value(500))
                .andExpect(jsonPath("$.birthday").value(1609874962000L))
                .andExpect(jsonPath("$.banned").value(true));
    }

    @Test
    public void getPlayerTest() throws Exception {
        this.mockMvc.perform(get("/rest/players/{id}", idVasiliy))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Vasiliy"))
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.race").value(Race.HUMAN.name()))
                .andExpect(jsonPath("$.profession").value(Profession.WARRIOR.name()))
                .andExpect(jsonPath("$.experience").value(1000))
                .andExpect(jsonPath("$.level").value(4))
                .andExpect(jsonPath("$.untilNextLevel").value(500))
                .andExpect(jsonPath("$.birthday").value(1609874962000L))
                .andExpect(jsonPath("$.banned").value(true));
    }

    @Test
    public void updatePlayerTest() throws Exception {
        PlayerForUpdate = new Player();
        updateForPlayer = new Player();
        updateForPlayer.setName("Mickey");
        updateForPlayer.setTitle("new Title");
        updateForPlayer.setRace(Race.ELF);
        updateForPlayer.setProfession(Profession.SORCERER);
        updateForPlayer.setExperience(2000);
        updateForPlayer.setBirthday(167963955100000L);
        updateForPlayer.setBanned(false);

        PlayerForUpdate.setName("Petya");
        PlayerForUpdate.setTitle("Title");
        PlayerForUpdate.setRace(Race.ELF);
        PlayerForUpdate.setProfession(Profession.SORCERER);
        PlayerForUpdate.setExperience(1000);
        PlayerForUpdate.setBirthday(167963955100000L);
        PlayerForUpdate.setBanned(true);

        this.mockMvc.perform(post("/rest/players/{id}", PlayerForUpdate.getId())
                        .content(objectMapper.writeValueAsString(updateForPlayer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mickey"))
                .andExpect(jsonPath("$.title").value("new Title"))
                .andExpect(jsonPath("$.race").value(Race.ELF.name()))
                .andExpect(jsonPath("$.profession").value(Profession.SORCERER.name()))
                .andExpect(jsonPath("$.experience").value(2000))
                .andExpect(jsonPath("$.level").value(6))
                .andExpect(jsonPath("$.untilNextLevel").value(800))
                .andExpect(jsonPath("$.birthday").value(167963955100000L))
                .andExpect(jsonPath("$.banned").value(false));
    }

    @Test
    public void deletePlayerTest() throws Exception {
        this.mockMvc.perform(delete("/rest/players/{id}", idVasiliy))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/rest/players/"))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(playerGeorgy, playerLena))));
    }
}
