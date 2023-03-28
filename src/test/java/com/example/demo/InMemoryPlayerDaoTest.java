package com.example.demo;

import com.example.demo.dao.inMemory.InMemoryPlayerDao;

public class InMemoryPlayerDaoTest extends PlayerDaoTest<InMemoryPlayerDao> {
    InMemoryPlayerDaoTest() {
        playerDao = new InMemoryPlayerDao();
    }
}
