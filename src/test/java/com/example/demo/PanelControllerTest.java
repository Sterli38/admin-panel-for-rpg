package com.example.demo;


import com.example.demo.controller.PanelController;
import com.example.demo.dao.PlayerDao;
import com.example.demo.entity.Player;
import com.example.demo.entity.Profession;
import com.example.demo.entity.Race;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.swing.tree.ExpandVetoException;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
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
    private PlayerDao dao;
    private Long id;
    private static Player player = new Player();

    @BeforeAll
    public static void createPlayer() {
        player = new Player();
        player.setName("Vasiliy");
        player.setTitle("Title");
        player.setRace(Race.HUMAN);
        player.setProfession(Profession.WARRIOR);
        player.setExperience(1000);
        player.setBirthday(1679639551000L);
        player.setBanned(true);
    }
    @BeforeEach
    public void addPlayer () {
        id = dao.createPlayer(player).getId();
    }

    @AfterEach
    public void deletePlayer () {
        try {
            dao.deletePlayerById(id);
        } catch(Exception e) {

        }
    }


    @Test
    public void getPlayersByFilterTest() {

    }

    @Test
    public void getPlayersCountTest() {

    }

    @Test
    public void createPlayerTest() throws Exception {
        this.mockMvc.perform(post("/"))

                .andExpect(status().isOk());
    }

    @Test
    public void getPlayerTest() throws Exception {
             this.mockMvc.perform(get("/rest/players/{id}", id))
                     .andExpect(status().isOk())
                     .andExpect(jsonPath("$.name").value("Vasiliy"))
                     .andExpect(jsonPath("$.title").value("Title"))
                     .andExpect(jsonPath("$.race").value(Race.HUMAN.name()))
                     .andExpect(jsonPath("$.profession").value(Profession.WARRIOR.name()))
                     .andExpect(jsonPath("$.experience").value(1000))
//                     .andExpect(jsonPath("$.level").value(4))
//                     .andExpect(jsonPath("$.untilNextLevel").value(500))
                     .andExpect(jsonPath("$.birthday").value(1679639551000L))
                     .andExpect(jsonPath("$.banned").value(true));
    }

    @Test
    public void updatePlayerTest() {
//        this.mockMvc.perform(post("/{id}", id))
    }

    @Test
    public void deletePlayerTest() throws Exception {
        this.mockMvc.perform(delete("/{id}", 1))
                .andExpect(status().isOk());
    }
}
