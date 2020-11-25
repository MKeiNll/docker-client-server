package org.example.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Entity
@Table(name = "player")
public class Player {

    @Id
    @Column(length = 32, nullable = false, unique = true)
    private String username;

    private Long balanceVersion = 1L;

    @Digits(integer = 100, fraction = 2)
    private BigDecimal balance = new BigDecimal(0);

    public Player(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getBalanceVersion() {
        return balanceVersion;
    }

    public void setBalanceVersion(Long balanceVersion) {
        this.balanceVersion = balanceVersion;
    }

    @Override
    public String toString() {
        return "username: '"
                .concat(username)
                .concat("' balanceVersion: '")
                .concat(balanceVersion.toString())
                .concat("' balance: '")
                .concat(balance.toString())
                .concat("'");
    }
}