package com.example.bank.Transaction;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private Long toId;
    private Long fromId;
    private BigDecimal amount;
    private Date date;

    public Transaction(Long toId, Long fromId, BigDecimal amount, Date date){
        this.toId = toId;
        this.fromId = fromId;
        this.amount = amount;
        this.date = date;
    }

    public Long getToId() {
        return toId;
    }

    public Long getFromId() {
        return fromId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
