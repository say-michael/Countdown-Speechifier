package com.itsmyco.countdownspeechifier;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.LocalTime;
import java.util.ArrayList;

public class MainController {
    @FXML
    TextField step1heading, step1Min, step1Max, step1SpokenPhrase;

    @FXML
    TextField step2heading, step2Min, step2Max, step2SpokenPhrase;

    @FXML
    TextField step3heading, step3Min, step3Max, step3SpokenPhrase;

    @FXML
    TextField step4heading, step4Min, step4Max, step4SpokenPhrase;

    TextField[] step1Flds, step2Flds, step3Flds, step4Flds;

    TextField[][] allFlds;
    @FXML
    HBox selectVoiceBtn, saveSettingsBtn, startBtn, stopBtn;

    @FXML
    BorderPane displayScreen;
    @FXML
    VBox stepsView;

    @FXML StackPane timeView;

    @FXML
    private void initialize(){

        step1Flds = new TextField[]{step1heading, step1Min, step1Max, step1SpokenPhrase};
        step2Flds = new TextField[]{step2heading, step2Min, step2Max, step2SpokenPhrase};
        step3Flds = new TextField[]{step3heading, step3Min, step3Max, step3SpokenPhrase};
        step4Flds = new TextField[]{step4heading, step4Min, step4Max, step4SpokenPhrase};

        allFlds = new TextField[][]{step1Flds, step2Flds, step3Flds, step4Flds};

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

    @FXML private Text startText;
    public void onStart(){

        if (startText.getText().contentEquals("STOP")){
            onStop();
            return;
        }

        if(!verifyAllFieldsEntered()){
            return;
        }

        stopProcessingSteps = false;

        // hide views
        stepsView.setVisible(false);
        selectVoiceBtn.setVisible(false);
        saveSettingsBtn.setVisible(false);
        startText.setText("STOP");

        // show views
        timeView.setVisible(true);

//        var pane = (Pane) timeDot.getParent();
//        var c = pane.getChildren();
//        timeDot.translateXProperty().addListener(observable -> {
////            var ci = new Circle(6);
//            var ci = new Rectangle(6,6, timeDot.getTranslateX(),timeDot.getTranslateY());
////            ci.setCenterX(timeDot.getTranslateX());
////            ci.setCenterY(timeDot.getTranslateY());
//            c.add(ci);
//        });

        heading.setText("Heading");

        int[] minMax1 = {Integer.parseInt(step1Min.getText()), Integer.parseInt(step1Max.getText())};
        int[] minMax2 = {Integer.parseInt(step2Min.getText()), Integer.parseInt(step2Max.getText())};
        int[] minMax3 = {Integer.parseInt(step3Min.getText()), Integer.parseInt(step3Max.getText())};
        int[] minMax4 = {Integer.parseInt(step4Min.getText()), Integer.parseInt(step4Max.getText())};

        var countdown = new Countdown(minMax1, minMax2, minMax3, minMax4);

        var step1 = new StepTime(step1heading.getText(), countdown.getStep1Seconds(), step1SpokenPhrase.getText());
        var step2 = new StepTime(step1heading.getText(), countdown.getStep1Seconds(), step1SpokenPhrase.getText());
        var step3 = new StepTime(step1heading.getText(), countdown.getStep1Seconds(), step1SpokenPhrase.getText());
        var step4 = new StepTime(step1heading.getText(), countdown.getStep1Seconds(), step1SpokenPhrase.getText());

        new Thread(() -> startTheSteps(new StepTime[]{step1, step2, step3, step4})).start();

    }
    @FXML
    Circle timeDot, timeCircle;

    private boolean verifyAllFieldsEntered(){
        for (TextField[] fld : allFlds) {
            for (TextField textField : fld) {
                if (textField.getText() == null || textField.getText().isBlank()){
                    return false;
                }
            }
        }
        return true;
    }

    boolean stopProcessingSteps = false;
    private int stepIndex = 0;
    private void startTheSteps(StepTime[] stepTimes){
        while (!stopProcessingSteps){
            if (nextStep.get()){
                nextStep.set(false);
                var step = stepTimes[stepIndex++ % 4];
                System.out.println(stepIndex + " = " + step);
                heading.setText(step.getHeading());
                speakStep(step.getPhrase());
                countDownStep(step.getSeconds());
            }
        }
    }

    private void speakStep(String phrase){

    }

    private void animateTimeDot(int seconds){
        var pt = new PathTransition();
        pt.setNode(timeDot);
        pt.setPath(timeCircle);
        pt.setDuration(Duration.seconds(seconds));
        pt.setInterpolator(Interpolator.LINEAR);
        pt.play();
    }

    @FXML private Text heading;
    @FXML Text timeText;
    public void onStop(){
        // hide views
        timeView.setVisible(false);

        // show views
        stepsView.setVisible(true);
        selectVoiceBtn.setVisible(true);
        saveSettingsBtn.setVisible(true);

        startText.setText("START");
        heading.setText("SETTINGS");

        stopProcessingSteps = true;
    }
    private final BooleanProperty nextStep = new SimpleBooleanProperty(true);

    private void countDownStep(int seconds){
        var time = LocalTime.now();
        var timeDone = time.plusSeconds(seconds);

        var currentSec = seconds;

        while (true){
            var tmp = LocalTime.now();
            if (tmp.isAfter(timeDone)){
                timeText.setText(String.valueOf(currentSec--));
                break;
            }
            timeText.setText(String.valueOf(currentSec--));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        nextStep.set(true);
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