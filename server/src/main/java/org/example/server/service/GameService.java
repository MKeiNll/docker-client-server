package org.example.server.service;

import org.example.server.configuration.GameProperties;
import org.example.server.dto.BalanceChangeRequestDto;
import org.example.server.dto.BalanceChangeResponseDto;
import org.example.server.entity.Player;
import org.example.server.exception.MaxBalanceExceededException;
import org.example.server.exception.NotEnoughFundsException;
import org.example.server.exception.PlayerBlacklistedException;
import org.example.server.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

@Service()
public class GameService {

    private PlayerRepository playerRepository;
    private GameProperties gameProperties;

    private final ArrayList<Player> playerState = new ArrayList<>();
    private final ArrayList<BalanceChangeResponseDto> transactionsMade = new ArrayList<>();

    Logger logger = LoggerFactory.getLogger(GameService.class);

    public GameService(PlayerRepository playerRepository, GameProperties gameProperties) {
        this.playerRepository = playerRepository;
        this.gameProperties = gameProperties;

        // On startup, load player data from the database into the state
        playerRepository.findAll().forEach(playerState::add);
        logger.debug("load player data OK - {} players loaded into the memory", playerState.size());
        logger.trace("player state: {}", playerState.toString());
    }

    // On shutdown, save player data from the state into the database
    @PreDestroy
    public void preDestroy() {
        backupPlayerState();
    }

    // A periodical task to save player state into the database
    @Scheduled(fixedRate = 60000)
    public void backupPlayerState() {
        logger.trace("player state: {}", playerState.toString());
        this.playerRepository.saveAll(playerState);
        logger.debug("backup player state OK - {} players saved to the database", playerState.size());
    }

    public BalanceChangeResponseDto addFunds(BalanceChangeRequestDto addRequest) {
        logger.info("addFunds - {}", addRequest.toString());
        String username = addRequest.username;
        if (playerIsBlacklisted(username)) {
            logger.trace("addFunds - player is blacklisted");
            throw new PlayerBlacklistedException(addRequest, getExistingPlayer(username));
        }

        BalanceChangeResponseDto existingTransaction = getExistingTransaction(addRequest.transactionId);
        if (existingTransaction != null) {
            logger.info("addFunds - returning an existing transaction - {}", existingTransaction.toString());
            return existingTransaction;
        }

        Player player = getPlayer(username);
        BigDecimal balanceAfterChange = player.getBalance().add(addRequest.balanceChange);
        if (balanceTooSmall(balanceAfterChange)) {
            logger.trace("addFunds - balance after change is too small");
            throw new NotEnoughFundsException(addRequest, player);
        } else if (balanceTooLarge(balanceAfterChange)) {
            logger.trace("addFunds - balance after change is too large");
            throw new MaxBalanceExceededException(addRequest, player);
        }
        player.setBalance(balanceAfterChange);

        BalanceChangeResponseDto response = new BalanceChangeResponseDto();
        response.transactionId = addRequest.transactionId;
        response.balanceVersion = player.getBalanceVersion();
        response.balanceChange = addRequest.balanceChange;
        response.balanceAfterChange = player.getBalance();

        rememberTransaction(response);
        logger.info("addFunds - transaction OK - {}", response.toString());
        return response;
    }

    public BalanceChangeResponseDto withdrawFunds(BalanceChangeRequestDto withdrawRequest) {
        logger.info("withdrawFunds - {}", withdrawRequest.toString());
        String username = withdrawRequest.username;
        if (playerIsBlacklisted(username)) {
            logger.trace("withdrawFunds - player is blacklisted");
            throw new PlayerBlacklistedException(withdrawRequest, getExistingPlayer(username));
        }

        BalanceChangeResponseDto existingTransaction = getExistingTransaction(withdrawRequest.transactionId);
        if (existingTransaction != null) {
            logger.info("withdrawFunds - returning an existing transaction - {}", existingTransaction.toString());
            return existingTransaction;
        }

        Player player = getPlayer(withdrawRequest.username);
        BigDecimal balanceAfterChange = player.getBalance().subtract(withdrawRequest.balanceChange);
        if (balanceTooSmall(balanceAfterChange)) {
            logger.trace("withdrawFunds - balance after change is too small");
            throw new NotEnoughFundsException(withdrawRequest, player);
        } else if (balanceTooLarge(balanceAfterChange)) {
            logger.trace("withdrawFunds - balance after change is too large");
            throw new MaxBalanceExceededException(withdrawRequest, player);
        }
        player.setBalance(balanceAfterChange);

        BalanceChangeResponseDto response = new BalanceChangeResponseDto();
        response.transactionId = withdrawRequest.transactionId;
        response.balanceVersion = player.getBalanceVersion();
        response.balanceChange = withdrawRequest.balanceChange;
        response.balanceAfterChange = player.getBalance();

        rememberTransaction(response);
        logger.info("withdrawFunds - transaction OK - {}", response.toString());
        return response;
    }

    // Returns an existing player or creates a new one and returns it
    private Player getPlayer(String username) {
        Player existingPlayer = playerState.stream().filter(p -> username.equals(p.getUsername())).findFirst().orElse(null);
        Player player;
        if (existingPlayer != null) {
            logger.debug("getPlayer - existing player '{}'", username);
            return existingPlayer;
        } else {
            player = new Player(username);
            playerState.add(player);
            logger.debug("getPlayer - new player '{}'", username);
            return player;
        }
    }

    private Player getExistingPlayer(String username) {
        return playerState.stream().filter(p -> username.equals(p.getUsername())).findFirst().orElse(null);
    }

    private BalanceChangeResponseDto getExistingTransaction(Long transactionId) {
        return transactionsMade.stream().filter(t -> transactionId.equals(t.transactionId)).findFirst().orElse(null);
    }

    private boolean playerIsBlacklisted(String username) {
        return Arrays.stream(gameProperties.getBlacklist()).anyMatch(username::equals);
    }

    private boolean balanceTooLarge(BigDecimal balanceAfterChange) {
        return balanceAfterChange.compareTo(gameProperties.getMaxBalance()) > 0;
    }

    private boolean balanceTooSmall(BigDecimal balanceAfterChange) {
        return balanceAfterChange.compareTo(BigDecimal.ZERO) < 0;
    }

    private void rememberTransaction(BalanceChangeResponseDto transaction) {
        if (transactionsMade.size() >= 1000) {
            transactionsMade.remove(0);
        }
        transactionsMade.add(transaction);
    }
}
