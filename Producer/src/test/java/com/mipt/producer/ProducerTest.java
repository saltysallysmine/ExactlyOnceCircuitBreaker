package com.mipt.producer;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class ProducerTest {

    private final Producer producer = new Producer();

    @Test
    void SendRequestTest() throws URISyntaxException, IOException, InterruptedException {
        String acceptingUrl = "http://localhost:8080/consumer/accept-action";
        assertDoesNotThrow(() -> producer.SendRequest(acceptingUrl));
        String denyingUrl = "http://localhost:8080/consumer/deny-action";
        assertDoesNotThrow(() -> producer.SendRequest(denyingUrl));
    }

}