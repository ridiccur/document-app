package com.taxtelecom.dialogs;

import com.taxtelecom.model.PaymentRequest;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;

public class PaymentRequestDialog extends Dialog<PaymentRequest> {

    public PaymentRequestDialog() {
        setTitle("Создать заявку на оплату");
        setHeaderText("Введите данные заявки");

        TextField numberField = new TextField();
        DatePicker dateField = new DatePicker(LocalDate.now());
        TextField userField = new TextField();
        TextField amountField = new TextField();
        TextField counterpartyField = new TextField();
        TextField currencyField = new TextField("RUB");
        TextField exchangeRateField = new TextField("1.0");
        TextField commissionField = new TextField("0.0");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label("Номер:"), 0, 0);
        grid.add(numberField, 1, 0);
        grid.add(new Label("Дата:"), 0, 1);
        grid.add(dateField, 1, 1);
        grid.add(new Label("Пользователь:"), 0, 2);
        grid.add(userField, 1, 2);
        grid.add(new Label("Сумма:"), 0, 3);
        grid.add(amountField, 1, 3);
        grid.add(new Label("Контрагент:"), 0, 4);
        grid.add(counterpartyField, 1, 4);
        grid.add(new Label("Валюта:"), 0, 5);
        grid.add(currencyField, 1, 5);
        grid.add(new Label("Курс Валюты:"), 0, 6);
        grid.add(exchangeRateField, 1, 6);
        grid.add(new Label("Комиссия:"), 0, 7);
        grid.add(commissionField, 1, 7);

        getDialogPane().setContent(grid);

        ButtonType createButtonType = new ButtonType("Создать", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                try {
                    return new PaymentRequest(
                            numberField.getText(),
                            dateField.getValue(),
                            userField.getText(),
                            Double.parseDouble(amountField.getText()),
                            counterpartyField.getText(),
                            currencyField.getText(),
                            Double.parseDouble(exchangeRateField.getText()),
                            Double.parseDouble(commissionField.getText())
                    );
                } catch (NumberFormatException e) {
                    System.err.println("Неверный формат числа: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });
    }
}