package org.example.server.dto;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class BalanceChangeRequestDto {
    @Min(1)
    public Long transactionId;

    @NotEmpty()
    public String username;

    @NotNull()
    @DecimalMin("0.01")
    @Digits(integer=100, fraction=2)
    public BigDecimal balanceChange;
}
