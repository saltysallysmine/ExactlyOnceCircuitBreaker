package com.mipt.producer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Producer {

    /*
     * Send request with timeout catching to consumer.
     * Retry if any errors occurred.
     */
    public static void SendRequest(String url) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest http_request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        HttpClient http_client = HttpClient.newHttpClient();
        HttpResponse<String> response = http_client.send(
                http_request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

}
