package com.example.demo;

import com.example.demo.service.RequestSender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TestSender {
    @MockBean
    private RequestSender requestSender;

    @BeforeEach
    public void init() {
        when(requestSender.sendMessage(ArgumentMatchers.anyString())).thenReturn("Заказ оплачен");
    }

    @Test
    public void Test() throws Exception {
        String expectMessage = "Заказ оплачен";
        String actualMessage = requestSender.sendMessage("Пока");
        Assertions.assertEquals(expectMessage, actualMessage);
    }
}
