package com.example.demo.entity;

import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
public class Player {
    private int id;
    private String name;
    private String title;
    private Race race;
    private Profession profession;
    private int experience;
    private int level;
    private int untilNextLevel;
    private Date birthday;
    private boolean banned;
}
