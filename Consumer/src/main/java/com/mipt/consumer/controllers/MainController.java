package com.mipt.consumer.controllers;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/consumer")
public class MainController {

    @Getter
    @Setter
    private Integer unansweredRequests = 1;

    @Data
    private static class RequestDTO {
        Integer id;
    }

    @PostMapping("/accept-action")
    @ResponseBody
    public ResponseEntity<String> AcceptAction(@RequestBody RequestDTO request) {
        log.info("Get request to /accept-action with id=" + request.getId().toString());
        log.info("Accept request. Do the action. Answer to request");
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/deny-action")
    @ResponseBody
    public ResponseEntity<String> DenyAction(@NotNull @RequestBody RequestDTO request) {
        log.info("Get request to /deny-action with id=" + request.getId().toString());
        log.info("Deny request. Do not take the action. Answer to request");
        return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
    }

    @PostMapping("/accept-action-randomly")
    @ResponseBody
    public ResponseEntity<String> AcceptActionRandomly(@NotNull @RequestBody RequestDTO request) throws InterruptedException {
        log.info("Get request to /accept-action-randomly with id=" + request.getId().toString() +
                ". Relative id #" + unansweredRequests);
        if (unansweredRequests <= 2) {
            log.info("Accept request. Do the action. Do not answer to request");
            setUnansweredRequests(unansweredRequests + 1);
            Thread.sleep(1000);
            return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
        }
        log.info("Accept request. Do the action. Answer to request");
        setUnansweredRequests(1);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
