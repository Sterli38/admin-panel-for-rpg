package com.example.demo.controller;

import com.example.demo.dao.PlayerDao;
import com.example.demo.dao.database.DatabasePlayerDao;
import com.example.demo.entity.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ExperimentController {
    private final PlayerDao dao;

    @GetMapping("/request")
    public List<Player> request() {
        return dao.getPlayers();
    }
}
