module com.example.blocky_blocky_v2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.almasb.fxgl.all;

    opens com.example.blocky_blocky_v2 to javafx.fxml;
    exports com.example.blocky_blocky_v2;
}