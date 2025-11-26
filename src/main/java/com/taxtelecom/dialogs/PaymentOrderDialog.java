package com.taxtelecom.dialogs;

import com.taxtelecom.model.PaymentOrder;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;

public class PaymentOrderDialog extends Dialog<PaymentOrder> {

    public PaymentOrderDialog() {
        setTitle("Создать платёжку");
        setHeaderText("Введите данные платёжки");

        TextField numberField = new TextField();
        DatePicker dateField = new DatePicker(LocalDate.now());
        TextField userField = new TextField();
        TextField amountField = new TextField();
        TextField employeeField = new TextField();

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
        grid.add(new Label("Сотрудник:"), 0, 4);
        grid.add(employeeField, 1, 4);

        getDialogPane().setContent(grid);

        ButtonType createButtonType = new ButtonType("Создать", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                try {
                    return new PaymentOrder(
                            numberField.getText(),
                            dateField.getValue(),
                            userField.getText(),
                            Double.parseDouble(amountField.getText()),
                            employeeField.getText()
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