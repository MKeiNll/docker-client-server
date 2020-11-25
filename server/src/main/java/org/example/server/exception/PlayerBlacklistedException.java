package org.example.server.exception;

import org.example.server.dto.BalanceChangeRequestDto;
import org.example.server.entity.Player;

public class PlayerBlacklistedException extends RuntimeException implements GameException {
    public static final int ERROR_CODE = 300;

    private Player player;
    private BalanceChangeRequestDto request;

    public PlayerBlacklistedException(BalanceChangeRequestDto request, Player player) {
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
        return PlayerBlacklistedException.ERROR_CODE;
    }
}
