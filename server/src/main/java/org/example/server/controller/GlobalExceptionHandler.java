package org.example.server.controller;

import org.example.server.dto.BalanceChangeRequestDto;
import org.example.server.dto.BalanceChangeResponseDto;
import org.example.server.entity.Player;
import org.example.server.exception.GameException;
import org.example.server.exception.MaxBalanceExceededException;
import org.example.server.exception.NotEnoughFundsException;
import org.example.server.exception.PlayerBlacklistedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({
            MaxBalanceExceededException.class,
            NotEnoughFundsException.class,
            PlayerBlacklistedException.class,
    })
    public final ResponseEntity<BalanceChangeResponseDto> handleException(GameException ex, WebRequest request) {
        BalanceChangeRequestDto balanceChangeRequest = ex.getRequest();
        Player player = ex.getPlayer();

        BalanceChangeResponseDto response = new BalanceChangeResponseDto();
        response.transactionId = balanceChangeRequest.transactionId;
        response.balanceVersion = player != null ? player.getBalanceVersion() : null;
        response.balanceChange = balanceChangeRequest.balanceChange;
        response.balanceAfterChange = player != null ? player.getBalance() : null;
        response.errorCode = ex.getErrorCode();

        logger.info("handleGameException - response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}