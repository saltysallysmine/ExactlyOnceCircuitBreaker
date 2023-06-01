package com.mipt.consumer.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/consumer")
public class MainController {

    @PostMapping("/do-the-action")
    public ResponseEntity<String> DoTheAction() {
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
