module com.itsmyco.countdownspeechifier {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.sun.xml.bind;

    opens com.itsmyco.countdownspeechifier to javafx.fxml, jakarta.xml.bind;
    exports com.itsmyco.countdownspeechifier;
}