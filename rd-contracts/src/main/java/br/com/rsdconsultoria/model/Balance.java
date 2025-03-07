package br.com.rsdconsultoria.model;

import java.math.BigDecimal;

public class Balance {
    private BigDecimal amount;

    public Balance(BigDecimal initialAmount) {
        setAmount(initialAmount);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void add(BigDecimal amount) {
        setAmount(this.amount.add(amount));
    }

    public void subtract(BigDecimal amount) {
        setAmount(this.amount.subtract(amount));
    }
}
