package org.example.server.dto;

import java.math.BigDecimal;

public class BalanceChangeResponseDto {
    public Long transactionId;
    public Long balanceVersion;
    public BigDecimal balanceChange;
    public BigDecimal balanceAfterChange;
    public int errorCode;

    @Override
    public String toString() {
        return "transactionId: '"
                .concat(transactionId.toString())
                .concat("' balanceVersion: '")
                .concat(balanceVersion != null ? balanceVersion.toString() : "null")
                .concat("' balanceChange: '")
                .concat(balanceChange.toString())
                .concat("' balanceAfterChange: '")
                .concat(balanceAfterChange != null ? balanceAfterChange.toString() : "null")
                .concat("' errorCode: '")
                .concat(Integer.toString(errorCode))
                .concat("'");
    }
}
