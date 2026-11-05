module org.example.assignmentmvc {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;

    exports View;
    opens View to javafx.fxml;
}