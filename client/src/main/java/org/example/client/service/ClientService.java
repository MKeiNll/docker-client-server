package org.example.client.service;

import net.minidev.json.JSONObject;
import org.example.client.configuration.ClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

@Service()
public class ClientService {

    @Autowired
    private ClientProperties clientProperties;
    private RestTemplate restTemplate;
    private HttpHeaders headers;
    private Random random;
    private Long existingTransactionId;

    public ClientService() {
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        random = new Random();
    }

    // A periodical task to imitate gameplay via generating random balance changes
    @Scheduled(fixedRate = 1000)
    public void imitateGameplay() throws URISyntaxException {
        try {
            JSONObject balanceChangeRequest = new JSONObject();
            Long transactionId = random.nextLong() & Long.MAX_VALUE;
            // Simulate repeating of existing transactions
            balanceChangeRequest.put("transactionId", random.nextBoolean() ? transactionId : existingTransactionId);
            existingTransactionId = transactionId;
            balanceChangeRequest.put("username", "player" + random.nextInt(25));
            balanceChangeRequest.put("balanceChange", random.nextInt(100000) + "." + random.nextInt(100));
            System.out.println("REQUEST: " + balanceChangeRequest.toString());
            HttpEntity<String> request = new HttpEntity<>(balanceChangeRequest.toString(), headers);
            restTemplate.postForObject(new URI(clientProperties.getServerUrl().concat(random.nextBoolean() ? "/addFunds" : "/withdrawFunds")), request, String.class);
        } catch (HttpClientErrorException e) {
            // Silently ignore known errors
            if (!e.getMessage().contains("\"errorCode\"")) {
                System.out.println(e);
            }
        }
    }
}
