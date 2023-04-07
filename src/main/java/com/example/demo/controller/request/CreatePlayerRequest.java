package com.example.demo.controller.request;

import com.example.demo.entity.Player;
import com.example.demo.entity.Profession;
import com.example.demo.entity.Race;
import lombok.Value;

public class CreatePlayerRequest extends PlayerRequest {

    public CreatePlayerRequest () {
        setDefaultValue();
    }
    private void setDefaultValue() {
        setBanned(false);
    }
}

