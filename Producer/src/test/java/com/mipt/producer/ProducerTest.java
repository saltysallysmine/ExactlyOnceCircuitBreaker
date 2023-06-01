package com.mipt.producer;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class ProducerTest {

    @Test
    void SendRequestTest() throws URISyntaxException, IOException, InterruptedException {
        Producer.SendRequest("localhost:8080/consumer/do-the-action");
    }

}