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
//    @Autowired
//    private PlayerDao dao;
    @Autowired
    PlayerService service;
    private Long idVasiliy;
    private Long idGeorgy;
    private Long idLena;
    private static Player playerVasiliy = new Player();
    private static Player playerGeorgy = new Player();
    private static Player playerLena = new Player();

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
    public void addPlayer () {
//        id = dao.createPlayer(player).getId();
        service.createPlayer(playerVasiliy);
        service.createPlayer(playerGeorgy);
        service.createPlayer(playerLena);
        idVasiliy = playerVasiliy.getId();
        idGeorgy = playerGeorgy.getId();
        idLena = playerLena.getId();
    }

    @AfterEach
    public void deletePlayer () {
        try {
//            dao.deletePlayerById(id);
            service.deletePlayerById(idVasiliy);
            service.deletePlayerById(idGeorgy);
            service.deletePlayerById(idLena);
        } catch(Exception ignore) {}
    }


    @Test
    public void getPlayersByFilterTest() throws Exception {
        this.mockMvc.perform(get("/rest/players/").param("name", "eor"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(playerGeorgy))));

        this.mockMvc.perform(get("/rest/players/").param("title", "TitleTitle"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(playerGeorgy, playerLena))));

        this.mockMvc.perform(get("/rest/players/").param("race", "HUMAN"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(playerVasiliy, playerLena))));

        this.mockMvc.perform(get("/rest/players/").param("profession","WARRIOR"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(playerVasiliy, playerGeorgy))));

                this.mockMvc.perform(get("/rest/players/").param("before", "1672946964").param("after", "1668349959"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(playerGeorgy, playerLena))));

        this.mockMvc.perform(get("/rest/players/").param("minLevel","1").param("maxLevel", "4"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(playerVasiliy, playerGeorgy))));

        this.mockMvc.perform(get("/rest/players/").param("minExperience", "500").param("maxExperience" , "1300"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(playerVasiliy, playerGeorgy))));

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
        mockMvc.perform(post("/rest/players/")
                                .content(objectMapper.writeValueAsString(playerVasiliy))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
//                .andExpect(jsonPath("$.id").isNumber())
//                .andExpect(jsonPath("$.name").value("Vasiliy"))
//                .andExpect(jsonPath("$.title").value("Title"))
//                .andExpect(jsonPath("$.race").value(Race.HUMAN))
//                .andExpect(jsonPath("$.profession").value(Profession.WARRIOR))
//                .andExpect(jsonPath("$.experience").value(1000))
//                .andExpect(jsonPath("$.level").value(4))
//                .andExpect(jsonPath("$.untilNextLevel").value(500))
//                .andExpect(jsonPath("$.birthday").value(1679639551000L))
//                .andExpect(jsonPath("$.banned").value(true));
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
        Player playerForUpdate = new Player();
        playerForUpdate.setName("Mickey");
        playerForUpdate.setTitle("new Title");
        playerForUpdate.setRace(Race.ELF);
        playerForUpdate.setProfession(Profession.SORCERER);
        playerForUpdate.setExperience(2000);
        playerForUpdate.setBirthday(167963955100000L);
        playerForUpdate.setBanned(false);

        this.mockMvc.perform(post("/rest/players/{id}", idVasiliy)
                .content(objectMapper.writeValueAsString(playerForUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.name").value("Mickey"));
//                .andExpect(jsonPath("$.title").value("new Title"))
//                .andExpect(jsonPath("$.race").value(Race.ELF.name()))
//                .andExpect(jsonPath("$.profession").value(Profession.SORCERER.name()))
//                .andExpect(jsonPath("$.experience").value(2000))
//                .andExpect(jsonPath("$.level").value(6))
//                .andExpect(jsonPath("$.untilNextLevel").value(800))
//                .andExpect(jsonPath("$.birthday").value(167963955100L))
//                .andExpect(jsonPath("$.banned").value(false));

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
