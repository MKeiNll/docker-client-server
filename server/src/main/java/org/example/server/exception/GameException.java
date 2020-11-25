package org.example.server.exception;

import org.example.server.dto.BalanceChangeRequestDto;
import org.example.server.entity.Player;

public interface GameException {
    int getErrorCode();

    Player getPlayer();

    BalanceChangeRequestDto getRequest();
}
