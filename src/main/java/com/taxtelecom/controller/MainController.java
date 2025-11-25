package com.taxtelecom.controller;

import com.taxtelecom.model.Document;
import com.taxtelecom.util.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class MainController {

    @FXML
    private ListView<String> documentListView;

    private final ObservableList<String> documentTitles = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadDocumentsFromDatabase();
        documentListView.setItems(documentTitles);
    }

    private void loadDocumentsFromDatabase() {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            TypedQuery<Document> query = em.createQuery("SELECT d FROM Document d ORDER BY d.date DESC", Document.class);
            List<Document> documents = query.getResultList();

            documentTitles.clear();
            for (Document doc : documents) {
                documentTitles.add(doc.getDisplayTitle());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}