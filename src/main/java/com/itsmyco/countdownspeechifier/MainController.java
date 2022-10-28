package com.itsmyco.countdownspeechifier;

import io.github.jonelo.jAdapterForNativeTTS.engines.SpeechEngine;
import io.github.jonelo.jAdapterForNativeTTS.engines.SpeechEngineNative;
import io.github.jonelo.jAdapterForNativeTTS.engines.Voice;
import io.github.jonelo.jAdapterForNativeTTS.engines.VoicePreferences;
import io.github.jonelo.jAdapterForNativeTTS.engines.exceptions.NotSupportedOperatingSystemException;
import jakarta.xml.bind.JAXBException;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
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
    HBox startBtn;

    @FXML
    BorderPane displayScreen;
    @FXML
    VBox stepsView;

    @FXML StackPane timeView;

    @FXML BorderPane appScreen;

    @FXML
    private void initialize() throws JAXBException {

        speechEngine.setVoice(voiceList.get(0).getName());

        step1Flds = new TextField[]{step1heading, step1Min, step1Max, step1SpokenPhrase};
        step2Flds = new TextField[]{step2heading, step2Min, step2Max, step2SpokenPhrase};
        step3Flds = new TextField[]{step3heading, step3Min, step3Max, step3SpokenPhrase};
        step4Flds = new TextField[]{step4heading, step4Min, step4Max, step4SpokenPhrase};

        allFlds = new TextField[][]{step1Flds, step2Flds, step3Flds, step4Flds};

        allMinMaxFields = new TextField[]{step1Min, step1Max, step2Min, step2Max, step3Min, step3Max, step4Min, step4Max};

        var stepsObj = Settings.loadSteps();

        if (stepsObj != null){
            this.steps = new Steps();
            loadStepsIntoFields(stepsObj);
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
        var text = new Text("Select Voice");
        text.setFont(new Font(15));
        var st = new StackPane(text);
        st.setPadding(new Insets(20, 0,10, 0));

        choiceDialog.getDialogPane().setHeader(st);
        choiceDialog.setTitle("Voice Settings");

//        for (TextField field : allMinMaxFields) {
//          field.setText("0");
//        }

        int fldIndex = 0;
        for (TextField field : allMinMaxFields) {
            final int index = fldIndex;
            field.focusedProperty().addListener((ob, oldValue, n) -> {if (oldValue) validateNumberTextField(field, index);});
            fldIndex++;
        }
//        for (TextField field : allMinMaxFields) {
//            field.setText("");
//        }

    }

    private void loadStepsIntoFields(Steps stepsObj){
        var steps = stepsObj.steps.toArray(Step[]::new);

        step1Flds[0].setText(steps[0].getHeading());
        step1Flds[1].setText(String.valueOf(steps[0].getMin() < 0 ? "" : steps[0].getMin()));
        step1Flds[2].setText(String.valueOf(steps[0].getMax() < 0 ? "" : steps[0].getMax()));
        step1Flds[3].setText(steps[0].getSpokenPhrase());

        step2Flds[0].setText(steps[1].getHeading());
        step2Flds[1].setText(String.valueOf(steps[1].getMin() < 0 ? "" : steps[1].getMin()));
        step2Flds[2].setText(String.valueOf(steps[1].getMax() < 0 ? "" : steps[1].getMax()));
        step2Flds[3].setText(steps[1].getSpokenPhrase());

        step3Flds[0].setText(steps[2].getHeading());
        step3Flds[1].setText(String.valueOf(steps[2].getMin() < 0 ? "" : steps[2].getMin()));
        step3Flds[2].setText(String.valueOf(steps[2].getMax() < 0 ? "" : steps[2].getMax()));
        step3Flds[3].setText(steps[2].getSpokenPhrase());

        step4Flds[0].setText(steps[3].getHeading());
        step4Flds[1].setText(String.valueOf(steps[3].getMin() < 0 ? "" : steps[3].getMin()));
        step4Flds[2].setText(String.valueOf(steps[3].getMax() < 0 ? "" : steps[3].getMax()));
        step4Flds[3].setText(steps[3].getSpokenPhrase());
    }

    private Steps steps = new Steps();

    private boolean checkValidationOfMinMax(int fldIndex){
        var fld = allMinMaxFields[fldIndex];
        try{
            var number = Integer.parseInt(fld.getText());

            if (fld.getPromptText().equalsIgnoreCase("min")) {
                var maxNumber = Integer.parseInt(allMinMaxFields[fldIndex + 1].getText());
                return number <= maxNumber;
            } else {
                var minNumber = Integer.parseInt(allMinMaxFields[fldIndex - 1].getText());
                return number >= minNumber;
            }
        } catch (NumberFormatException e){
            return false;
        }
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
//            fld.setText("");
            System.err.println("Empty String");
        }
    }

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
    public void onSelectVoice(){
        choiceDialog.showAndWait();
    }

    public void onSaveSettings() throws JAXBException {
        this.steps.getSteps().clear();
        this.steps.getSteps().addAll(Arrays.asList(makeSteps()));
        Settings.saveSteps(steps);
    }

    private Step[] makeSteps(){
        Step[] steps = new Step[4];

        steps[0] = new Step(1,
                step1Flds[0].getText(),
                Integer.parseInt(step1Flds[1].getText().isBlank() ? "-1" : step1Flds[1].getText()),
                Integer.parseInt(step1Flds[2].getText().isBlank() ? "-1" : step1Flds[2].getText()),
                step1Flds[3].getText()
        );
        steps[1] = new Step(2,
                step2Flds[0].getText(),
                Integer.parseInt(step2Flds[1].getText().isBlank() ? "-1" : step2Flds[1].getText()),
                Integer.parseInt(step2Flds[2].getText().isBlank() ? "-1" : step2Flds[2].getText()),
                step2Flds[3].getText()
        );
        steps[2] = new Step(3,
                step3Flds[0].getText(),
                Integer.parseInt(step3Flds[1].getText().isBlank() ? "-1" : step3Flds[1].getText()),
                Integer.parseInt(step3Flds[2].getText().isBlank() ? "-1" : step3Flds[2].getText()),
                step3Flds[3].getText()
        );
        steps[3] = new Step(4,
                step4Flds[0].getText(),
                Integer.parseInt(step4Flds[1].getText().isBlank() ? "-1" : step4Flds[1].getText()),
                Integer.parseInt(step4Flds[2].getText().isBlank() ? "-1" : step4Flds[2].getText()),
                step4Flds[3].getText()
        );
        return steps;
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

//        if (stepsFin)

        stopProcessingSteps.set(false);

        // hide views
        stepsView.setVisible(false);

        startText.setText("STOP");

        // show views
        timeView.setVisible(true);
//        heading.setText("Step");

        stepIndex = 0;
        executor.execute(()-> startTheSteps(makeSteps()));
    }
    static final ExecutorService executor = Executors.newCachedThreadPool();
    public void onStop(){
//        executor.submit(()->{}).
//        f.cancel(true);
        stopProcessingSteps.set(true);
        // hide views
        timeView.setVisible(false);

        // show views
        stepsView.setVisible(true);

        startText.setText("START");
        heading.setText("SETTINGS");

    }
    @FXML
    Circle timeDot, timeCircle;

    public void onAppScreen(){
        appScreen.requestFocus();
    }
    private boolean verifyAllFieldsEntered(){
        boolean containsSomething = false;

        for (TextField fld : allMinMaxFields) {
            if (fld.getText() != null || !fld.getText().isBlank()){
                containsSomething = true;
                break;
            }
        }

        if (!containsSomething) return false;

        for (int i = 0; i < allMinMaxFields.length; i+=2) {
            if (checkValidationOfMinMax(i)){
                return true;
            }
        }

        return false;
    }

    final AtomicBoolean stopProcessingSteps = new AtomicBoolean(false);
    private int stepIndex = 0;
    private synchronized void startTheSteps(Step[] steps)  {
        while (!stopProcessingSteps.get()){
            if (nextStep.get()){
                nextStep.set(false);

                var step = steps[stepIndex++ % 4];


                if (step.getMin() < 0 || step.getMax() <= 0) {
                    nextStep.set(true);
                    continue; //skip
                }

                if (step.getMin() > step.getMax()) {
                    nextStep.set(true);
                    continue; //skip
                }

                int seconds;

                if (step.getMin() == step.getMax()) {
                    seconds = step.getMin();
                } else {
                    seconds = new Random().nextInt(step.getMin(), step.getMax() + 1);
                }

                if (seconds == 0) {
                    nextStep.set(true);
                    continue;
                }

                heading.setText(step.getHeading().isBlank() ? step.getStepNumber() : step.getHeading());

                try {
                    speechEngine.say(step.getSpokenPhrase());
                    timeText.setText(String.valueOf(seconds));

                    var words = step.getSpokenPhrase().split(" ").length;
                    System.out.println("Sleeping");
                    Thread.sleep(285 * words);
                    System.out.println("I'm Awake");

                    countDownStep(seconds);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    @FXML private Text heading;
    @FXML Text timeText;

    private final BooleanProperty nextStep = new SimpleBooleanProperty(true);

    private void countDownStep(int seconds){
        var time = LocalTime.now();
        var timeDone = time.plusSeconds(seconds);

        AtomicInteger currentSec = new AtomicInteger(seconds);
        timeText.setText(String.valueOf(currentSec.get()));
//        try {Thread.sleep(1000);} catch (InterruptedException ignored) {}

        while (true){
            if (stopProcessingSteps.get()){
                break;
            }
            var tmp = LocalTime.now();
            // Platform.runLater(()-> );
            // seconds are finished counting
            if (tmp.isAfter(timeDone)){
                Platform.runLater(()-> timeText.setText(String.valueOf(currentSec.get())));
                break;
            }
            Platform.runLater(()-> timeText.setText(String.valueOf(currentSec.getAndDecrement())));
            // countdown in seconds
            try {Thread.sleep(1000);} catch (InterruptedException ignored) {}
        }
        // wait 1/2 second before moving to next step
        try {Thread.sleep(500);} catch (InterruptedException ignored) {}
        nextStep.set(true); // start next step
    }

    public void startHover(){
        startBtn.setBackground(new Background(new BackgroundFill(Color.web("#127369"), new CornerRadii(50, true), null)));
    }


    public void startHoverDone(){
        startBtn.setBackground(new Background(new BackgroundFill(Color.web("#10403b"), new CornerRadii(50, true), null)));
    }

    public void onClose(ActionEvent actionEvent) throws JAXBException {
        onSaveSettings();
        onStop();
        executor.shutdownNow();
        Platform.exit();
    }

    public void onSaveAs(ActionEvent actionEvent) throws JAXBException {
        var chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.xml"));
        chooser.setTitle("Save Settings");
        chooser.setInitialFileName("settings");

        var file = chooser.showSaveDialog(step1Min.getScene().getWindow());
        Settings.saveSteps(steps,file);
    }

    public void onImport(ActionEvent actionEvent) {
        var chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.xml"));
        chooser.setTitle("Import Settings");

        var file = chooser.showOpenDialog(step1Min.getScene().getWindow());
        Steps steps = null;
        try {
            steps = Settings.loadSteps(file);
        } catch (JAXBException e) {
           // steps error
            steps = new Steps();
        }
        this.steps = steps;
        loadStepsIntoFields(steps);
    }

    public void onClearFields(ActionEvent actionEvent) {
        this.steps = new Steps();
        for (TextField[] allFld : allFlds) {
            for (TextField textField : allFld) {
                textField.clear();
            }
        }
    }
}