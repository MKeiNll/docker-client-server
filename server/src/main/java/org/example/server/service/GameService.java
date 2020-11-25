package org.example.server.service;

import org.example.server.configuration.GameProperties;
import org.example.server.dto.BalanceChangeRequestDto;
import org.example.server.dto.BalanceChangeResponseDto;
import org.example.server.entity.Player;
import org.example.server.exception.MaxBalanceExceededException;
import org.example.server.exception.NotEnoughFundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service()
public class GameService {

    @Autowired
    private GameProperties gameProperties;

    private final ArrayList<Player> playerState = new ArrayList<>();

    public BalanceChangeResponseDto addFunds(BalanceChangeRequestDto addRequest) {
        Player player = getPlayer(addRequest.username);

        BigDecimal balanceAfterChange = player.getBalance().add(addRequest.balanceChange);
        if (balanceAfterChange.compareTo(gameProperties.getMaxBalance()) > 0) {
             throw new MaxBalanceExceededException(addRequest, player);
        } else if (balanceAfterChange.compareTo(BigDecimal.ZERO) < 0) {
             throw new NotEnoughFundsException(addRequest, player);
        }

        BalanceChangeResponseDto response = new BalanceChangeResponseDto();
        response.transactionId = addRequest.transactionId;
        response.balanceVersion = player.getBalanceVersion();
        response.balanceChange = addRequest.balanceChange;
        response.balanceAfterChange = player.getBalance();
        return response;
    }

    public BalanceChangeResponseDto withdrawFunds(BalanceChangeRequestDto addRequest) {
        Player player = getPlayer(addRequest.username);

        // TODO - balance change logic as in addFunds()

        BalanceChangeResponseDto response = new BalanceChangeResponseDto();
        response.transactionId = addRequest.transactionId;
        response.balanceVersion = player.getBalanceVersion();
        response.balanceChange = addRequest.balanceChange;
        response.balanceAfterChange = player.getBalance();
        return response;
    }

    // Returns an existing player or creates a new one and returns it
    private Player getPlayer(String username) {
        Player existingPlayer = playerState.stream().filter(p -> username.equals(p.getUsername())).findFirst().orElse(null);
        Player player;
        if (existingPlayer != null) {
            return existingPlayer;
        } else {
            player = new Player(username);
            playerState.add(player);
            return player;
        }
    }
}
