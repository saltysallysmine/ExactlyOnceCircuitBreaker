package com.mipt.producer;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

/*
 * This is integration tests to producer and consumer.
 *
 * - Open ConsumerMain and run it. Then run SendRequestTest().
 * You should see that first SendRequest is approved and second is denied by consumer.
 * In consumer log you should see that all of them are unique.
 *
 * - Now open ConsumerMain and run it again. Then run TimeoutTest().
 * You should see in test console that 1st and 2nd request are denied and 3rd is approved.
 * In consumer log you should see that 1st is unique, and it is done by consumer.
 * Other requests are not unique, so they have not done.
 *
 * - Open ConsumerMain and run it again. Then run CircuitBreakerTest().
 * First 10 requests should trigger closing Circuit Breaker.
 * Next 5 requests should not be sent because of this reason.
 * Then wait 10 seconds. After that Circuit Breaker will become opened and next 5 requests will pass.
 */
class ProducerTest {

    private final Producer producer = new Producer();

    @Test
    void SendRequestTest() {
        String acceptingUrl = "http://localhost:8080/consumer/accept-action";
        assertDoesNotThrow(() -> producer.SendRequest(acceptingUrl, 1L));
        String denyingUrl = "http://localhost:8080/consumer/deny-action";
        assertThrows(TimeoutException.class, () -> producer.SendRequest(denyingUrl, 2L));
    }

    @Test
    void TimeoutTest() {
        String timeoutUrl = "http://localhost:8080/consumer/accept-action-randomly";
        assertDoesNotThrow(() -> producer.SendRetryingRequest(timeoutUrl, 2L));
    }

    @Test
    void CircuitBreakerTest() throws InterruptedException {
        String timeoutUrl = "http://localhost:8080/consumer/deny-action";
        for (int i = 0; i < 10; i++) {
            assertThrows(TimeoutException.class, () -> producer.SendRequest(timeoutUrl));
        }
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> producer.SendRequest(timeoutUrl));
        }
        // Wait for opening
        Thread.sleep(producer.circuitBreaker.getDuration() * 1000L);
        for (int i = 0; i < 5; i++) {
            assertThrows(TimeoutException.class, () -> producer.SendRequest(timeoutUrl));
        }
    }

}