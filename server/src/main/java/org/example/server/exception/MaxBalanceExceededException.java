package org.example.server.exception;

import org.example.server.dto.BalanceChangeRequestDto;
import org.example.server.entity.Player;

public class MaxBalanceExceededException extends RuntimeException implements GameException {
    public static final int ERROR_CODE = 100;

    private Player player;
    private BalanceChangeRequestDto request;

    public MaxBalanceExceededException(BalanceChangeRequestDto request, Player player) {
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
        return MaxBalanceExceededException.ERROR_CODE;
    }
}
