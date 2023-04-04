package com.example.demo.controller.request;

import com.example.demo.entity.Profession;
import com.example.demo.entity.Race;
import lombok.Data;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class PlayerRequest {
    @Size(max = 12)
    @NotBlank
    private String name;
    @Size(max = 30)
    @NotBlank
    private String title;
    @NotNull
    private Race race;
    @NotNull
    private Profession profession;
    @Max(10000000)
    @NotNull
    private Integer experience;
    @NotNull
    private Date birthday;
    @NotNull
    private Boolean banned;
}
