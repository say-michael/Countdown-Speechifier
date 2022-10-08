package com.itsmyco.countdownspeechifier;

public class StepTime {
    private String heading;
    private int seconds;
    private String phrase;

    public StepTime(String heading, int seconds, String phrase) {
        this.heading = heading;
        this.seconds = seconds;
        this.phrase = phrase;
    }

    public String getHeading() {
        return heading;
    }

    public int getSeconds() {
        return seconds;
    }

    public String getPhrase() {
        return phrase;
    }


}
