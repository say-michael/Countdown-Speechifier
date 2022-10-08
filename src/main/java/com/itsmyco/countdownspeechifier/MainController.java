package com.itsmyco.countdownspeechifier;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class MainController {
    @FXML
    TextField step1heading, step1Min, step1Max, step1SpokenPhrase;

    @FXML
    TextField step2heading, step2Min, step2Max, step2SpokenPhrase;

    @FXML
    TextField step3heading, step3Min, step3Max, step3SpokenPhrase;

    @FXML
    TextField step4heading, step4Min, step4Max, step4SpokenPhrase;

    TextField[] step1Flds = {step1heading, step1Min, step1Max, step1SpokenPhrase};
    TextField[] step2Flds = {step2heading, step2Min, step2Max, step2SpokenPhrase};
    TextField[] step3Flds = {step3heading, step3Min, step3Max, step3SpokenPhrase};
    TextField[] step4Flds = {step4heading, step4Min, step4Max, step4SpokenPhrase};

    @FXML
    HBox selectVoiceBtn, saveSettingsBtn, startBtn, stopBtn;

    @FXML
    BorderPane displayScreen;
    @FXML
    VBox settingsScreen;

    @FXML
    private void initialize(){
        var steps = settings.load();
        if (steps != null){
            step1Flds[0].setText(steps[0].getHeading());
            step1Flds[1].setText(String.valueOf(steps[0].getMin()));
            step1Flds[2].setText(String.valueOf(steps[0].getMax()));
            step1Flds[3].setText(steps[0].getSpokenPhrase());

            step2Flds[0].setText(steps[1].getHeading());
            step2Flds[1].setText(String.valueOf(steps[1].getMin()));
            step2Flds[2].setText(String.valueOf(steps[1].getMax()));
            step2Flds[3].setText(steps[1].getSpokenPhrase());

            step3Flds[0].setText(steps[2].getHeading());
            step3Flds[1].setText(String.valueOf(steps[2].getMin()));
            step3Flds[2].setText(String.valueOf(steps[2].getMax()));
            step3Flds[3].setText(steps[2].getSpokenPhrase());

            step4Flds[0].setText(steps[3].getHeading());
            step4Flds[1].setText(String.valueOf(steps[3].getMin()));
            step4Flds[2].setText(String.valueOf(steps[3].getMax()));
            step4Flds[3].setText(steps[3].getSpokenPhrase());
        }
    }

    private final Settings settings = new Settings();

    public void onSelectVoice(){

    }

    public void onSaveSettings(){
        Step[] steps = new Step[4];
        steps[0] = new Step(
                step1Flds[0].getText(),
                Integer.parseInt(step1Flds[1].getText()),
                Integer.parseInt(step1Flds[2].getText()),
                step1Flds[3].getText()
        );
        steps[1] = new Step(
                step2Flds[0].getText(),
                Integer.parseInt(step2Flds[1].getText()),
                Integer.parseInt(step2Flds[2].getText()),
                step2Flds[3].getText()
        );
        steps[2] = new Step(
                step3Flds[0].getText(),
                Integer.parseInt(step3Flds[1].getText()),
                Integer.parseInt(step3Flds[2].getText()),
                step3Flds[3].getText()
        );
        steps[3] = new Step(
                step4Flds[0].getText(),
                Integer.parseInt(step4Flds[1].getText()),
                Integer.parseInt(step4Flds[2].getText()),
                step4Flds[3].getText()
        );

        settings.save(steps);
    }

    public void onStart(){

    }

    public void onStop(){

    }
    public void btnHover(HBox btn){
        btn.setBackground(new Background(new BackgroundFill(Color.web("#127369"), new CornerRadii(20), null)));
    }

    public void btnHoverDone(HBox btn){
        btn.setBackground(new Background(new BackgroundFill(Color.web("#10403b"), new CornerRadii(20), null)));
    }

    public void startHover(){
        startBtn.setBackground(new Background(new BackgroundFill(Color.web("#127369"), new CornerRadii(50, true), null)));
    }

    public void stopHover(){

    }

    public void startHoverDone(){
        startBtn.setBackground(new Background(new BackgroundFill(Color.web("#10403b"), new CornerRadii(50, true), null)));
    }

    public void stopHoverDone(){

    }

    public void selectVoiceHover(){
        btnHover(selectVoiceBtn);
    }

    public void selectVoiceHoverDone(){
        btnHoverDone(selectVoiceBtn);
    }

    public void saveSettingsHover(){
        btnHover(saveSettingsBtn);
    }

    public void saveSettingsHoverDone(){
        btnHoverDone(saveSettingsBtn);
    }
}