package com.itsmyco.countdownspeechifier;

import io.github.jonelo.jAdapterForNativeTTS.engines.SpeechEngine;
import io.github.jonelo.jAdapterForNativeTTS.engines.SpeechEngineNative;
import io.github.jonelo.jAdapterForNativeTTS.engines.Voice;
import io.github.jonelo.jAdapterForNativeTTS.engines.VoicePreferences;
import io.github.jonelo.jAdapterForNativeTTS.engines.exceptions.NotSupportedOperatingSystemException;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

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

    TextField[] allMinMaxFields;

    TextField[][] allFlds;
    @FXML
    HBox selectVoiceBtn, saveSettingsBtn, startBtn;

    @FXML
    BorderPane displayScreen;
    @FXML
    VBox stepsView;

    @FXML StackPane timeView;

    @FXML BorderPane appScreen;

    @FXML
    private void initialize(){

        speechEngine.setVoice(voiceList.get(0).getName());

        step1Flds = new TextField[]{step1heading, step1Min, step1Max, step1SpokenPhrase};
        step2Flds = new TextField[]{step2heading, step2Min, step2Max, step2SpokenPhrase};
        step3Flds = new TextField[]{step3heading, step3Min, step3Max, step3SpokenPhrase};
        step4Flds = new TextField[]{step4heading, step4Min, step4Max, step4SpokenPhrase};

        allFlds = new TextField[][]{step1Flds, step2Flds, step3Flds, step4Flds};
        allMinMaxFields = new TextField[]{step1Min, step1Max, step2Min, step2Max, step3Min, step3Max, step4Min, step4Max};

        int fldIndex = 0;
        for (TextField field : allMinMaxFields) {
            final int index = fldIndex;
            field.focusedProperty().addListener((ob, oldValue, n) -> {if (oldValue) validateNumberTextField(field, index);});
            fldIndex++;
        }
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

        choiceDialog.selectedItemProperty().addListener((o, oldValue, newValue) -> {
            selectedVoice = newValue;
            speechEngine.setVoice(voiceList.get(voiceDescriptions.indexOf(newValue)).getName());
            try {
                speechEngine.say("This is my voice.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void validateNumberTextField(TextField fld, int fldIndex){
        try {
            var number = Integer.parseInt(fld.getText());
            if (number < 0) fld.setText("0");
            if (number > 999) fld.setText("999");

            fld.setBorder(null);
            fld.setEffect(null);

            TextField siblingFld;

            if (fld.getPromptText().equalsIgnoreCase("min")) {
                siblingFld = allMinMaxFields[fldIndex + 1];
            } else {
                siblingFld = allMinMaxFields[fldIndex - 1];
            }
            siblingFld.setBorder(null);
            siblingFld.setEffect(null);

            if (fld.getPromptText().equalsIgnoreCase("min")) {
                var maxNumber = Integer.parseInt(allMinMaxFields[fldIndex + 1].getText());
                if (number > maxNumber){
                    fld.setBorder(new Border(new BorderStroke(
                            Color.RED,
                            BorderStrokeStyle.SOLID,
                            new CornerRadii(20),
                            new BorderWidths(0.5)
                    )));
                    siblingFld.setBorder(new Border(new BorderStroke(
                            Color.RED,
                            BorderStrokeStyle.SOLID,
                            new CornerRadii(20),
                            new BorderWidths(0.5)
                    )));
                    var ds = new DropShadow();
                    ds.setRadius(5.0);
                    ds.setColor(Color.rgb(255,0,0, 0.2));
                    fld.setEffect(ds);
                    siblingFld.setEffect(ds);
                }
                System.out.println("Min Clicked");
            } else {
                System.out.println("Max Clicked");
                var minNumber = Integer.parseInt(allMinMaxFields[fldIndex - 1].getText());
                if (number < minNumber){
                    fld.setBorder(new Border(new BorderStroke(
                            Color.RED,
                            BorderStrokeStyle.SOLID,
                            new CornerRadii(20),
                            new BorderWidths(0.5)
                    )));
                    siblingFld.setBorder(new Border(new BorderStroke(
                            Color.RED,
                            BorderStrokeStyle.SOLID,
                            new CornerRadii(20),
                            new BorderWidths(0.5)
                    )));
                    var ds = new DropShadow();
                    ds.setRadius(5.0);
                    ds.setColor(Color.rgb(255,0,0, 0.2));
                    fld.setEffect(ds);
                    siblingFld.setEffect(ds);
                }
            }
        } catch (NumberFormatException e) {
            fld.setText("");
        }
    }

    private void toggleInValidField(boolean on, TextField fld){
        if (on){
            fld.setBorder(new Border(new BorderStroke(
                    Color.RED,
                    BorderStrokeStyle.SOLID,
                    new CornerRadii(20),
                    new BorderWidths(3)
            )));
        } else {
            fld.setBorder(null);
        }
    }

    private final Settings settings = new Settings();

    private boolean isVoiceOptionsShown = false;

    private final VoicePreferences voicePreferences = new VoicePreferences();
    private SpeechEngine speechEngine;

    {
        try {
            speechEngine = SpeechEngineNative.getInstance();
        } catch (NotSupportedOperatingSystemException e) {
            System.err.println(e.getMessage());
        }
    }

    private final List<Voice> voiceList = speechEngine.getAvailableVoices();


    private final List<String> voiceDescriptions = voiceList.stream().map(Voice::getDescription).toList();

    private String selectedVoice = voiceDescriptions.get(0);
    private final ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(selectedVoice, voiceDescriptions);
    @FXML HBox voiceOptions;
    public void onSelectVoice(){

        choiceDialog.showAndWait();


//        var sc = new Timeline();
//        if (isVoiceOptionsShown){
//            sc.stop();
//            sc.getKeyFrames().clear();
//            sc.getKeyFrames().add(
//                    new KeyFrame(Duration.millis(1000),
//                            new KeyValue(voiceOptions.prefHeightProperty(),0),
//                            new KeyValue(voiceOptions.translateYProperty(),-5)
//                    )
//            );
//
//            isVoiceOptionsShown = false;
//        } else {
//            // show voice options
//            sc.stop();
//            sc.getKeyFrames().clear();
//            sc.getKeyFrames().add(
//                    new KeyFrame(Duration.millis(1000),
//                            new KeyValue(voiceOptions.prefHeightProperty(),60),
//                            new KeyValue(voiceOptions.translateYProperty(),-14)
//                    )
//            );
//            isVoiceOptionsShown = true;
//        }
//        sc.play();
    }
    @FXML Text saveSettingsText;

    public void onSaveSettings(){
        if (verifyAllFieldsEntered()) saveSettingsText.setText("Saved");
        settings.save(makeSteps());
    }

    private Step[] makeSteps(){
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
        return steps;
    }

    public void onMaleVoice(){
        voicePreferences.setGender(VoicePreferences.Gender.MALE);
        maleText.setFont(new Font("Arial Rounded MT Bold", 13));
        femaleText.setFont(new Font("System", 13));
        maleText.setFill(Color.BLACK);
        femaleText.setFill(new Color(0.5529, 0.5529, 0.5529, 1.0));

    }
    public void onFemaleVoice(){
        voicePreferences.setGender(VoicePreferences.Gender.FEMALE);
        femaleText.setFont(new Font("Arial Rounded MT Bold", 13));
        maleText.setFont(new Font("System", 13));
        femaleText.setFill(Color.BLACK);
        maleText.setFill(new Color(0.5529, 0.5529, 0.5529, 1.0));
    }

    private Thread runningStepsThread;
    @FXML private Text startText, maleText, femaleText;

    @FXML
    ChoiceBox<String> voiceChoices;

    public void onStart(){

        if (startText.getText().contentEquals("STOP")){
            onStop();
            return;
        }

//        if(!verifyAllFieldsEntered()){
//            return;
//        }

//        if (stepsFin)

        stopProcessingSteps = false;

        // hide views
        stepsView.setVisible(false);
        selectVoiceBtn.setVisible(false);
        saveSettingsBtn.setVisible(false);
        voiceOptions.setVisible(false);
        startText.setText("STOP");

        // show views
        timeView.setVisible(true);
        heading.setText("Heading");

        stepIndex = 0;
//        runningStepsThread.start();
        runningStepsThread = new Thread(() -> startTheSteps(makeSteps()));
       runningStepsThread.start();

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
    private synchronized void startTheSteps(Step[] steps){
        while (!stopProcessingSteps){
            if (nextStep.get()){
                nextStep.set(false);

                var step = steps[stepIndex++ % 4];
                System.out.println(stepIndex + " = " + step);

                heading.setText(step.getHeading());

                try {
                    speechEngine.say(step.getSpokenPhrase());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (step.getMin() < 0 || step.getMax() < 0) {
                    continue; //skip
                }

                if (step.getMax() == 0) continue;

                var seconds = new Random().nextInt(step.getMin(), step.getMax());

                if (seconds == 0) continue;

                countDownStep(seconds);
            }
        }
    }





    private void speakStep(String text){
        try {
            speechEngine.say(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        runningStepsThread.interrupt();
        runningStepsThread = null;
        // hide views
        timeView.setVisible(false);

        // show views
        stepsView.setVisible(true);
        selectVoiceBtn.setVisible(true);
        saveSettingsBtn.setVisible(true);
        voiceOptions.setVisible(true);

        startText.setText("START");
        heading.setText("SETTINGS");

        stopProcessingSteps = true;

    }
    private final BooleanProperty nextStep = new SimpleBooleanProperty(true);


    private void countDownStep(int seconds){
        var time = LocalTime.now();
        var timeDone = time.plusSeconds(seconds);

        var currentSec = seconds;
        timeText.setText(String.valueOf(currentSec));


        try {
            if (seconds < 4){
                Thread.sleep(1000);
            } else {
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        while (true){
            if (stopProcessingSteps){
                break;
            }
            var tmp = LocalTime.now();
            if (tmp.isAfter(timeDone)){
                timeText.setText(String.valueOf(currentSec));
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


    public void startHoverDone(){
        startBtn.setBackground(new Background(new BackgroundFill(Color.web("#10403b"), new CornerRadii(50, true), null)));
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