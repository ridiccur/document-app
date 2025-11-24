module com.taxtelecom.documentapp {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.sql;
    
    requires com.h2database;

    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    
    requires lombok;

    opens com.taxtelecom.model to org.hibernate.orm;
    opens com.taxtelecom to javafx.fxml;

    exports com.taxtelecom;
}