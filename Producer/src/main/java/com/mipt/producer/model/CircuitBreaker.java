package com.mipt.producer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;

@Slf4j
public class CircuitBreaker {

    private Long errorsCount;
    private boolean isCircuitBreakerClosed;
    private LocalTime openingTime;

    @Getter
    @Setter
    private long errorsMaxCount = 11L;

    /*
     * Duration of Circuit Breaker covering in seconds
     */
    @Getter
    @Setter
    private int duration = 10;


    public CircuitBreaker() {
        errorsCount = 0L;
        isCircuitBreakerClosed = false;
        openingTime = null;
    }

    private LocalTime getOpeningTime() {
        LocalTime currentTime = LocalTime.now();
        return LocalTime.of(currentTime.getHour(), currentTime.getMinute(), currentTime.getSecond() + duration);
    }

    /*
     * Return true if breaker is closed
     */
    public boolean processCircuitBreaker() {
        if (isCircuitBreakerClosed) {
            if (LocalTime.now().isBefore(openingTime)) {
                return true;
            }
            errorsCount = 0L;
            openingTime = null;
            isCircuitBreakerClosed = false;
            log.info("Circuit Breaker opens");
            return false;
        }
        errorsCount += 1;
        if (errorsCount >= errorsMaxCount) {
            openingTime = getOpeningTime();
            log.info("Circuit Breaker closes");
            isCircuitBreakerClosed = true;
        }
        return isCircuitBreakerClosed;
    }

}
