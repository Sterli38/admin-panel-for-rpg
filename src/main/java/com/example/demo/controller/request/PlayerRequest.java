package com.example.demo.controller.request;

import com.example.demo.entity.Profession;
import com.example.demo.entity.Race;
import lombok.Data;
import lombok.Value;

import java.util.Date;

@Data
public class PlayerRequest {
    private String name;
    private String title;
    private Race race;
    private Profession profession;
    private Integer experience;
    private Integer level;
    private Integer untilNextLevel;
    private Date birthday;
    private Boolean banned;
}
