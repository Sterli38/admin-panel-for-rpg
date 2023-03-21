package com.example.demo.controller;

import com.example.demo.dao.PlayerDao;
import com.example.demo.entity.Player;
import com.example.demo.filter.Filter;
import com.example.demo.service.PlayerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//@Controller
@RestController
@AllArgsConstructor
public class PanelController {
    private PlayerDao playerDao;
    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/getPlayers")
    public List<Player> getPlayers() {
        return playerDao.getPlayers();
    }
    @PostMapping("/createPlayer")
    public void createPlayer(@RequestBody Player player) {
        playerDao.createPlayer(player);
    }

    @PatchMapping("/editPlayer")
    public void editPlayer(@RequestBody Player player) {
        playerDao.editPlayer(player);
    }

    @DeleteMapping("/deletePlayerById/{id}")
    public void deletePlayerById(@PathVariable long id) {
        playerDao.deletePlayerById(id);
    }

    @GetMapping("/getPlayerById/{id}")
    public Player getPlayerById(@PathVariable long id) {
        return playerDao.getPlayerById(id);
    }

    @GetMapping("/getPlayersByFilter")
    public List<Player> getPlayersByFilter(@RequestBody Filter filter) {
        return playerDao.getPlayersByFilter(filter);
    }
}
