package org.example.server.controller;

import org.example.server.dto.BalanceChangeRequestDto;
import org.example.server.dto.BalanceChangeResponseDto;
import org.example.server.entity.Player;
import org.example.server.exception.GameException;
import org.example.server.exception.MaxBalanceExceededException;
import org.example.server.exception.NotEnoughFundsException;
import org.example.server.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping()
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/addFunds")
    public BalanceChangeResponseDto addFunds(@Valid @RequestBody BalanceChangeRequestDto addRequest) {
        return gameService.addFunds(addRequest);
    }

    @PostMapping("/withdrawFunds")
    public BalanceChangeResponseDto withdrawFunds(@Valid @RequestBody BalanceChangeRequestDto withdrawRequest) {
        return gameService.withdrawFunds(withdrawRequest);
    }

    @ResponseBody
    @ExceptionHandler({
            MaxBalanceExceededException.class,
            NotEnoughFundsException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private BalanceChangeResponseDto handleException(GameException e) {
        BalanceChangeRequestDto request = e.getRequest();
        Player player = e.getPlayer();

        BalanceChangeResponseDto response = new BalanceChangeResponseDto();
        response.transactionId = request.transactionId;
        response.balanceVersion = player.getBalanceVersion();
        response.balanceChange = request.balanceChange;
        response.balanceAfterChange = player.getBalance();
        response.errorCode = e.getErrorCode();
        return response;
    }
}