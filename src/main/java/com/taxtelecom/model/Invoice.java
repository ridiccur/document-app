package com.taxtelecom.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@DiscriminatorValue("Invoice")
// Накладная
public class Invoice extends Document {
    @Column(nullable = true)
    private String currency;

    @Column(name = "exchange_rate", nullable = true)
    private double exchangeRate;

    @Column(nullable = true)
    private String product;

    @Column(nullable = true)
    private double quantity;

    public Invoice() {}

    public Invoice(String number, LocalDate date, String user, double amount,
                   String currency, double exchangeRate, String product, double quantity) {
        super(number, date, user, amount);
        this.currency = currency;
        this.exchangeRate = exchangeRate;
        this.product = product;
        this.quantity = quantity;
    }

    public String getCurrency() {
        return currency;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public String getProduct() {
        return product;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getDisplayTitle() {
        return "Накладная №" + getNumber() + " от " +
                getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Override
    public String toString() {
        return super.toString() + String.format("""
                Тип: Накладная
                Валюта: %s
                Курс Валюты: %.4f
                Товар: %s
                Количество: %.3f
                """, currency, exchangeRate, product, quantity);
    }
}