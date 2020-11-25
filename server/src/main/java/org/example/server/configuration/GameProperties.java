package org.example.server.configuration;

import org.example.server.exception.ConfigurationPropertyMissingException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Validated()
@Component()
@ConfigurationProperties("game")
public class GameProperties {

    @DecimalMin("0.01")
    @Digits(integer=100, fraction=2)
    private BigDecimal maxBalance;

    private String[] blacklist = {};

    public BigDecimal getMaxBalance() {
        return maxBalance;
    }

    public String[] getBlacklist() {
        return blacklist;
    }

    public void setMaxBalance(String maxBalance) {
        if (maxBalance == null || maxBalance.isBlank()) {
            throw new ConfigurationPropertyMissingException("Property is not present");
        }
        this.maxBalance = new BigDecimal(maxBalance);
    }

    public void setBlacklist(String blacklist) {
        if (blacklist != null || !blacklist.isBlank()) {
            this.blacklist = blacklist.split(",");
        }
    }
}
