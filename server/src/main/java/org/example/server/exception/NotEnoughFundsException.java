package org.example.server.exception;

import org.example.server.dto.BalanceChangeRequestDto;
import org.example.server.entity.Player;

public class NotEnoughFundsException extends RuntimeException implements GameException {
    public static final int ERROR_CODE = 200;

    private Player player;
    private BalanceChangeRequestDto request;

    public NotEnoughFundsException(BalanceChangeRequestDto request, Player player) {
        super();
        this.player = player;
        this.request = request;
    }

    public Player getPlayer() {
        return player;
    }

    public BalanceChangeRequestDto getRequest() {
        return request;
    }

    public int getErrorCode() {
        return NotEnoughFundsException.ERROR_CODE;
    }
}
