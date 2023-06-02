package com.mipt.producer;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Producer {

    @Data
    @AllArgsConstructor
    private static class Request {
        Integer id;
        String meta;
    }

    private final AtomicInteger currentId = new AtomicInteger(0);

    private String getRequestBody(Integer id) {
        Request request = new Request(id, "");
        Gson gson = new Gson();
        log.info("Make request body: " + gson.toJson(request));
        return gson.toJson(request);
    }

    /*
     * Send request with timeout catching to consumer.
     * Retry if any errors occurred.
     */
    public void SendRequest(String url) throws URISyntaxException, IOException, InterruptedException {
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
    }

}
