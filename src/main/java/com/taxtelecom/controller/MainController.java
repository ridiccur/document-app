package com.taxtelecom.controller;

import com.taxtelecom.model.*;
import com.taxtelecom.util.CsvDocumentIO;
import com.taxtelecom.util.HibernateUtil;
import com.taxtelecom.dialogs.InvoiceDialog;
import com.taxtelecom.dialogs.PaymentOrderDialog;
import com.taxtelecom.dialogs.PaymentRequestDialog;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.geometry.Pos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    @FXML
    private ListView<HBox> documentListView;

    @FXML
    private Button deleteSelectedBtn;

    private final ObservableList<Document> documents = FXCollections.observableArrayList();
    private final ObservableList<Boolean> selections = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        documentListView.setCellFactory(lv -> new ListCell<HBox>() {
            @Override
            protected void updateItem(HBox item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : item);
            }
        });

        documentListView.setItems(FXCollections.observableArrayList());

        loadDocumentsFromDatabase();

        // Выделение документов
        documents.addListener((ListChangeListener<Document>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (int i = 0; i < c.getAddedSubList().size(); i++) {
                        selections.add(false);
                    }
                } else if (c.wasRemoved()) {
                    for (int i = 0; i < c.getRemovedSize(); i++) {
                        selections.remove(selections.size() - 1);
                    }
                }
            }
            refreshView();
        });

        refreshView();
    }

    // Обновление отображения списка документов
    private void refreshView() {
        List<HBox> items = new ArrayList<>();
        for (int i = 0; i < documents.size(); i++) {
            Document doc = documents.get(i);
            CheckBox checkBox = new CheckBox();
            Label label = new Label(doc.getDisplayTitle());
            HBox item = new HBox(10, checkBox, label);
            item.setAlignment(Pos.CENTER_LEFT);

            boolean isSelected = selections.get(i);
            checkBox.setSelected(isSelected);

            int index = i;
            checkBox.setOnAction(e -> selections.set(index, checkBox.isSelected()));

            items.add(item);
        }
        documentListView.getItems().setAll(items);
    }

    private void loadDocumentsFromDatabase() {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            TypedQuery<Document> query = em.createQuery("SELECT d FROM Document d ORDER BY d.date DESC", Document.class);
            List<Document> docs = query.getResultList();

            documents.setAll(docs);
            selections.clear();
            for (int i = 0; i < docs.size(); i++) {
                selections.add(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось загрузить документы из БД: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @FXML
    private void onCreateInvoice() {
        InvoiceDialog dialog = new InvoiceDialog();
        dialog.showAndWait().ifPresent(invoice -> {
            EntityManager em = HibernateUtil.createEntityManager();
            try {
                em.getTransaction().begin();
                em.persist(invoice);
                em.getTransaction().commit();
                documents.add(invoice);
                selections.add(false); // Добавляем выделение для нового документа
                showAlert("Создано", "Накладная создана и сохранена в БД.");
            } catch (Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
                showAlert("Ошибка", "Не удалось сохранить накладную: " + e.getMessage());
            } finally {
                em.close();
            }
        });
    }

    @FXML
    private void onCreatePaymentOrder() {
        PaymentOrderDialog dialog = new PaymentOrderDialog();
        dialog.showAndWait().ifPresent(po -> {
            EntityManager em = HibernateUtil.createEntityManager();
            try {
                em.getTransaction().begin();
                em.persist(po);
                em.getTransaction().commit();
                documents.add(po);
                selections.add(false);
                showAlert("Создано", "Платёжка создана и сохранена в БД.");
            } catch (Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
                showAlert("Ошибка", "Не удалось сохранить платёжку: " + e.getMessage());
            } finally {
                em.close();
            }
        });
    }

    @FXML
    private void onCreatePaymentRequest() {
        PaymentRequestDialog dialog = new PaymentRequestDialog();
        dialog.showAndWait().ifPresent(pr -> {
            EntityManager em = HibernateUtil.createEntityManager();
            try {
                em.getTransaction().begin();
                em.persist(pr);
                em.getTransaction().commit();
                documents.add(pr);
                selections.add(false);
                showAlert("Создано", "Заявка создана и сохранена в БД.");
            } catch (Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
                showAlert("Ошибка", "Не удалось сохранить заявку: " + e.getMessage());
            } finally {
                em.close();
            }
        });
    }

    @FXML
    private void onDeleteSelected() {
        List<Document> toDelete = new ArrayList<>();
        for (int i = 0; i < selections.size(); i++) {
            if (selections.get(i)) {
                toDelete.add(documents.get(i));
            }
        }

        if (toDelete.isEmpty()) {
            showAlert("Удаление", "Нет выбранных документов для удаления.");
            return;
        }

        EntityManager em = HibernateUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            for (Document doc : toDelete) {
                if (doc.getId() != null) {
                    Document managed = em.merge(doc);
                    em.remove(managed);
                }
                documents.remove(doc);
            }
            em.getTransaction().commit();
            showAlert("Удаление", "Выбранные документы удалены из БД.");
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            showAlert("Ошибка", "Не удалось удалить документы: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @FXML
    private void onView() {
        int index = documentListView.getSelectionModel().getSelectedIndex();
        if (index < 0 || index >= documents.size()) {
            showAlert("Просмотр", "Выберите документ для просмотра.");
            return;
        }

        Document selected = documents.get(index);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Просмотр документа");
        alert.setHeaderText(selected.getDisplayTitle());
        alert.setContentText(selected.toString());
        alert.showAndWait();
    }

    @FXML
    private void onSave() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Экспорт документов в CSV");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = chooser.showSaveDialog(documentListView.getScene().getWindow());

        if (file != null) {
            try {
                List<Document> docs = new ArrayList<>(documents);
                CsvDocumentIO.exportToCsv(docs, file);
                showAlert("Экспорт", "Документы экспортированы в " + file.getName());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Ошибка", "Не удалось экспортировать: " + e.getMessage());
            }
        }
    }

    @FXML
    private void onLoad() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Импорт документов из CSV");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = chooser.showOpenDialog(documentListView.getScene().getWindow());

        if (file != null && file.exists()) {
            try {
                List<Document> docs = CsvDocumentIO.importFromCsv(file);

                EntityManager em = HibernateUtil.createEntityManager();
                em.getTransaction().begin();
                for (Document doc : docs) {
                    em.persist(doc);
                }
                em.getTransaction().commit();
                em.close();

                loadDocumentsFromDatabase();
                showAlert("Импорт", "Документы импортированы из " + file.getName());
            } catch (IOException | IllegalArgumentException e) {
                e.printStackTrace();
                showAlert("Ошибка", "Не удалось импортировать: " + e.getMessage());
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}