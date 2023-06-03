package com.mipt.producer;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class ProducerTest {

    private final Producer producer = new Producer();

    @Test
    void SendRequestTest() {
        String acceptingUrl = "http://localhost:8080/consumer/accept-action";
        assertDoesNotThrow(() -> producer.SendRequest(acceptingUrl));
        String denyingUrl = "http://localhost:8080/consumer/deny-action";
        assertThrows(TimeoutException.class, () -> producer.SendRequest(denyingUrl));
    }

    @Test
    void TimeoutTest() {
        String timeoutUrl = "http://localhost:8080/consumer/accept-action-randomly";
        assertDoesNotThrow(() -> producer.SendRetryingRequest(timeoutUrl, 2L));
    }

}