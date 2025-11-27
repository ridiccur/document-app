package com.taxtelecom.util;

import com.taxtelecom.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvDocumentIO {

    private static final String[] HEADERS = {
        "type", "number", "date", "user", "amount",
        "currency", "exchange_rate", "product", "quantity",
        "employee", "counterparty", "commission"
    };

    // Экспорт списка документов в CSV
    public static void exportToCsv(List<Document> documents, File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.println(String.join(",", HEADERS));
            for (Document doc : documents) {
                writer.println(toCsvLine(doc));
            }
        }
    }

    // Импорт документов из CSV
    public static List<Document> importFromCsv(File file) throws IOException {
        List<Document> documents = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String header = reader.readLine();
            if (header == null || !header.startsWith("type")) {
                throw new IllegalArgumentException("Неверный формат CSV-файла.");
            }

            String line;
            while ((line = reader.readLine()) != null) {
                Document doc = fromCsvLine(line);
                if (doc != null) {
                    documents.add(doc);
                }
            }
        }
        return documents;
    }

    private static String toCsvLine(Document doc) {
        String type = doc.getClass().getSimpleName(); // Invoice, PaymentOrder, PaymentRequest

        String number = escapeCsvField(doc.getNumber());
        String date = doc.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String user = escapeCsvField(doc.getUser());
        String amount = String.valueOf(doc.getAmount());

        String currency = "";
        String exchangeRate = "";
        String product = "";
        String quantity = "";
        String employee = "";
        String counterparty = "";
        String commission = "";

        if (doc instanceof Invoice inv) {
            currency = escapeCsvField(inv.getCurrency());
            exchangeRate = String.valueOf(inv.getExchangeRate());
            product = escapeCsvField(inv.getProduct());
            quantity = String.valueOf(inv.getQuantity());
        } else if (doc instanceof PaymentOrder po) {
            employee = escapeCsvField(po.getEmployee());
        } else if (doc instanceof PaymentRequest pr) {
            counterparty = escapeCsvField(pr.getCounterparty());
            currency = escapeCsvField(pr.getCurrency());
            exchangeRate = String.valueOf(pr.getExchangeRate());
            commission = String.valueOf(pr.getCommission());
        }

        return String.join(",",
            escapeCsvField(type),
            number, date, user, amount,
            currency, exchangeRate, product, quantity,
            employee, counterparty, commission
        );
    }

    private static Document fromCsvLine(String line) {
        String[] fields = splitCsvLine(line);

        if (fields.length < 5) return null;

        String type = fields[0];
        String number = unescapeCsvField(fields[1]);
        LocalDate date = LocalDate.parse(fields[2]);
        String user = unescapeCsvField(fields[3]);
        double amount = Double.parseDouble(fields[4]);

        switch (type) {
            case "Invoice":
                if (fields.length >= 9) {
                    return new Invoice(
                        number, date, user, amount,
                        unescapeCsvField(fields[5]), // currency
                        Double.parseDouble(fields[6]), // exchange_rate
                        unescapeCsvField(fields[7]), // product
                        Double.parseDouble(fields[8])  // quantity
                    );
                }
                break;
            case "PaymentOrder":
                if (fields.length >= 10) {
                    return new PaymentOrder(
                        number, date, user, amount,
                        unescapeCsvField(fields[9]) // employee
                    );
                }
                break;
            case "PaymentRequest":
                if (fields.length >= 12) {
                    return new PaymentRequest(
                        number, date, user, amount,
                        unescapeCsvField(fields[10]), // counterparty
                        unescapeCsvField(fields[5]),  // currency
                        Double.parseDouble(fields[6]), // exchange_rate
                        Double.parseDouble(fields[11]) // commission
                    );
                }
                break;
        }
        return null;
    }

    private static String escapeCsvField(String field) {
        if (field == null) return "";
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }

    private static String unescapeCsvField(String field) {
        if (field == null) return "";
        if (field.startsWith("\"") && field.endsWith("\"")) {
            return field.substring(1, field.length() - 1).replace("\"\"", "\"");
        }
        return field;
    }

    private static String[] splitCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
                current.append(c);
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        return result.toArray(new String[0]);
    }
}