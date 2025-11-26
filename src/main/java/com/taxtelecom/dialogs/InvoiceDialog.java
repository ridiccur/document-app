package com.taxtelecom.dialogs;

import com.taxtelecom.model.Invoice;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;

public class InvoiceDialog extends Dialog<Invoice> {

    public InvoiceDialog() {
        setTitle("Создать накладную");
        setHeaderText("Введите данные накладной");

        // Поля ввода
        TextField numberField = new TextField();
        DatePicker dateField = new DatePicker(LocalDate.now());
        TextField userField = new TextField();
        TextField amountField = new TextField();
        TextField currencyField = new TextField("RUB");
        TextField exchangeRateField = new TextField("1.0");
        TextField productField = new TextField();
        TextField quantityField = new TextField("1.0");

        // Макет
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
        grid.add(new Label("Валюта:"), 0, 4);
        grid.add(currencyField, 1, 4);
        grid.add(new Label("Курс Валюты:"), 0, 5);
        grid.add(exchangeRateField, 1, 5);
        grid.add(new Label("Товар:"), 0, 6);
        grid.add(productField, 1, 6);
        grid.add(new Label("Количество:"), 0, 7);
        grid.add(quantityField, 1, 7);

        getDialogPane().setContent(grid);

        ButtonType createButtonType = new ButtonType("Создать", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                try {
                    return new Invoice(
                            numberField.getText(),
                            dateField.getValue(),
                            userField.getText(),
                            Double.parseDouble(amountField.getText()),
                            currencyField.getText(),
                            Double.parseDouble(exchangeRateField.getText()),
                            productField.getText(),
                            Double.parseDouble(quantityField.getText())
                    );
                } catch (NumberFormatException e) {
                    // В реальном приложении показать Alert
                    System.err.println("Неверный формат числа: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });
    }
}