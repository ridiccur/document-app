package com.taxtelecom.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@DiscriminatorValue("PaymentOrder")
// Платёжка
public class PaymentOrder extends Document {

    @Column(nullable = true)
    private String employee;

    public PaymentOrder() {}

    public PaymentOrder(String number, LocalDate date, String user, double amount,
                        String employee) {
        super(number, date, user, amount);
        this.employee = employee;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    @Override
    public String getDisplayTitle() {
        return "Платёжка №" + getNumber() + " от " +
                getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Override
    public String toString() {
        return super.toString() + String.format("""
                Тип: Платёжка
                Сотрудник: %s
                """, employee);
    }
}