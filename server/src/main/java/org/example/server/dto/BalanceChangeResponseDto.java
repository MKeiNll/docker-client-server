package org.example.server.dto;

import java.math.BigDecimal;

public class BalanceChangeResponseDto {
    public Long transactionId;
    public Long balanceVersion;
    public BigDecimal balanceChange;
    public BigDecimal balanceAfterChange;
    public int errorCode;
}
