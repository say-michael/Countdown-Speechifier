package com.itsmyco.countdownspeechifier;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Step {
    private String heading;
    private int min;
    private int max;
    @XmlElement(name = "spoken-phrase")
    private String spokenPhrase;
    @XmlElement(name = "step-number")
    private int stepNumber;


    public Step(int stepNumber, String heading, int min, int max, String spokenPhrase) {
        this.heading = heading;
        this.min = min;
        this.max = max;
        this.spokenPhrase = spokenPhrase;
        this.stepNumber = stepNumber;
    }

    public static Step blank(int number) {
        return new Step(number, "", -1, -1, "");
    }

    public String getHeading() {
        return heading;
    }

    public String getStepNumber() {
        return "STEP " + stepNumber;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getSpokenPhrase() {
        return spokenPhrase;
    }

    public void setSpokenPhrase(String spokenPhrase) {
        this.spokenPhrase = spokenPhrase;
    }

    @Override
    public String toString() {
        return "Step{" +
                "heading='" + heading + '\'' +
                ", min=" + min +
                ", max=" + max +
                ", spokenPhrase='" + spokenPhrase + '\'' +
                '}';
    }
}
