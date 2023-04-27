package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestSender {

    public String sendMessage (String string) {
        throw new RuntimeException("cannot send message");
    }
}
