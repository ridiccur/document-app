package com.taxtelecom.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@DiscriminatorValue("PaymentRequest")
public class PaymentRequest extends Document {

    @Column(nullable = true)
    private String counterparty;

    @Column(nullable = true)
    private String currency;

    @Column(name = "exchange_rate", nullable = true)
    private double exchangeRate;

    @Column(nullable = true)
    private double commission;

    public PaymentRequest() {}

    public PaymentRequest(String number, LocalDate date, String user, double amount,
                          String counterparty, String currency, double exchangeRate, double commission) {
        super(number, date, user, amount);
        this.counterparty = counterparty;
        this.currency = currency;
        this.exchangeRate = exchangeRate;
        this.commission = commission;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public String getCurrency() {
        return currency;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public double getCommission() {
        return commission;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    @Override
    public String getDisplayTitle() {
        return "Заявка №" + getNumber() + " от " +
                getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}