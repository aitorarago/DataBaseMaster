module com.example.projectofinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires mysql.connector.java;


    opens com.example.projectofinal to javafx.fxml;
    opens com.example.projectofinal.Clases.Funciones to javafx.fxml;
    exports com.example.projectofinal;
    exports com.example.projectofinal.Clases.Funciones;
    exports com.example.projectofinal.Clases.Personalizacion;
    opens com.example.projectofinal.Clases.Personalizacion to javafx.fxml;
}