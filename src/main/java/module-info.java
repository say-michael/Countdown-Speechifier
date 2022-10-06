module com.itsmyco.countdownspeechifier {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.itsmyco.countdownspeechifier to javafx.fxml;
    exports com.itsmyco.countdownspeechifier;
}