package org.example.client.service;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

@Service()
public class ClientService {

    private RestTemplate restTemplate;
    private HttpHeaders headers;
    private Random random;

    public ClientService() {
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        random = new Random();
    }

    // A periodical task to imitate gameplay via generating random balance changes
    @Scheduled(fixedRate = 1000)
    public void imitateGameplay() throws URISyntaxException {
        JSONObject balanceChangeRequest = new JSONObject();
        balanceChangeRequest.put("transactionId", random.nextLong());
        balanceChangeRequest.put("username", "player" + random.nextInt(25));
        balanceChangeRequest.put("balanceChange", random.nextInt(100000) + "." + random.nextInt(100));
        HttpEntity<String> request = new HttpEntity<>(balanceChangeRequest.toString(), headers);
        restTemplate.postForObject(new URI("http://localhost:9090/".concat(random.nextBoolean() ? "addFunds" : "withdrawFunds")), request, String.class);
    }
}
