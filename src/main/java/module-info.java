module com.taxtelecom.documentapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.sql; // если будет БД
    requires lombok;
    

    opens com.taxtelecom to javafx.fxml;
    exports com.taxtelecom;
}