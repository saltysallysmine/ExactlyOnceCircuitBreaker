package com.mipt.producer;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Producer {

    @Data
    @AllArgsConstructor
    private static class Request {
        Integer id;
    }

    private final AtomicInteger currentId = new AtomicInteger(0);

    private String getRequestBody(Integer id) {
        Request request = new Request(id);
        Gson gson = new Gson();
        log.info("Make request body: " + gson.toJson(request));
        return gson.toJson(request);
    }

    /*
     * Send single request to consumer
     */
    @SneakyThrows
    public void SendRequest(String url) {
        Integer requestId = currentId.incrementAndGet();
        String requestBody = getRequestBody(requestId);
        HttpRequest http_request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpClient http_client = HttpClient.newHttpClient();
        log.info("Send request to url=" + url + " with id=" + requestId);
        HttpResponse<String> response = http_client.send(http_request, HttpResponse.BodyHandlers.ofString());
        String statusCode = String.valueOf(response.statusCode());
        log.info("Returned status code=" + statusCode);
        if (statusCode.equals("504")) {
            throw new TimeoutException();
        }
    }

    /*
     * Send request with timeout catching to consumer.
     * Retry if any errors occurred.
     */
    public void SendRetryingRequest(String url, Long timeout) {
        log.info("Start processing retrying request to url=" + url + " with timeout=" + timeout);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        int attemptsLeft = 3;
        while (attemptsLeft != 0) {
            try {
                attemptsLeft -= 1;
                log.info("Trying to send request");
                Future<?> future = executorService.submit(() -> SendRequest(url));
                future.get(timeout, TimeUnit.SECONDS);
                break;
            } catch (TimeoutException e) {
                log.info("No response from server. Retry");
            } catch (InterruptedException | ExecutionException e) {
                log.info("An error occurred: " + e.getMessage());
            }
        }
        executorService.shutdownNow();
    }

}
