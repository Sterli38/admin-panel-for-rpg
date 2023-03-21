package com.example.demo.controller;

import com.example.demo.dao.PlayerDao;
import com.example.demo.entity.Player;
import com.example.demo.filter.Filter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@AllArgsConstructor
public class UIController {

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

}
