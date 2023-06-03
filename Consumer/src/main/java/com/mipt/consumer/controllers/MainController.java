package com.mipt.consumer.controllers;

import com.mipt.consumer.model.Action;
import com.mipt.consumer.model.ActionsRepository;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/consumer")
public class MainController {

    @Autowired
    ActionsRepository actionsRepository;

    @Getter
    @Setter
    private Long unansweredRequests = 1L;

    @Data
    private static class RequestDTO {
        Long id;
    }

    /*
     * Process new action from request and return true if it is unique
     */
    private boolean processActionFrom(RequestDTO request) {
        Long actionId = request.getId();
        Optional<Action> actionRecord = actionsRepository.findById(actionId);
        Action action;
        if (actionRecord.isEmpty()) {
            log.info("Action #" + actionId + " is unique. Save it to performed_actions");
            action = new Action(actionId, 0L);
            actionsRepository.save(action);
            return true;
        }
        action = actionRecord.get();
        action.incrementDuplicatesCount();
        log.info("Action #" + actionId + " is not unique. Number of duplicates is #" + action.getDuplicatesCount());
        actionsRepository.deleteById(actionId);
        actionsRepository.save(action);
        return false;
    }

    @PostMapping("/accept-action")
    @ResponseBody
    public ResponseEntity<String> AcceptAction(@RequestBody RequestDTO request) {
        log.info("Get request to /accept-action with id=" + request.getId().toString());
        boolean isUniqueRequest = processActionFrom(request);
        if (!isUniqueRequest) {
            log.info("Accept request. Do not do the action because it is already done. Answer to request");
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
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
        boolean isUniqueRequest = processActionFrom(request);
        if (unansweredRequests <= 2) {
            String actionLog = (isUniqueRequest)? "Do the action. " : "Do not take the action. ";
            log.info("Accept request. " + actionLog + "Do not answer to request");
            setUnansweredRequests(unansweredRequests + 1);
            Thread.sleep(1000);
            return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
        }
        log.info("Accept request. Do not take the action. Answer to request");
        setUnansweredRequests(1L);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
