package org.example.server.controller;

import org.example.server.dto.BalanceChangeRequestDto;
import org.example.server.dto.BalanceChangeResponseDto;
import org.example.server.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}