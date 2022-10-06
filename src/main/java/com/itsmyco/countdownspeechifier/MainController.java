package com.itsmyco.countdownspeechifier;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MainController {
    @FXML
    TextField step1Heading, step1Min, step1Max, step1SpokenPhrase;

    @FXML
    TextField step2Heading, step2Min, step2Max, step2SpokenPhrase;

    @FXML
    TextField step3Heading, step3Min, step3Max, step3SpokenPhrase;

    @FXML
    TextField step4Heading, step4Min, step4Max, step4SpokenPhrase;

    TextField[] step1Flds = {step1Heading, step1Min, step1Max, step1SpokenPhrase};
    TextField[] step2Flds = {step2Heading, step2Min, step2Max, step2SpokenPhrase};
    TextField[] step3Flds = {step3Heading, step3Min, step3Max, step3SpokenPhrase};
    TextField[] step4Flds = {step4Heading, step4Min, step4Max, step4SpokenPhrase};

    @FXML
    StackPane selectVoiceBtn, saveSettingsBtn, startBtn, stopBtn;

    @FXML Pane settingScreen, displayScreen;
    
    public void onSelectVoice(){

    }

    public void onSaveSettings(){

    }

    public void onStart(){

    }

    public void onStop(){

    }

}