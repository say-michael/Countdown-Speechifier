package com.itsmyco.countdownspeechifier;

public class Step {
    private String heading;
    private int min;
    private int max;
    private String spokenPhrase;

    public Step(String heading, int min, int max, String spokenPhrase) {
        this.heading = heading;
        this.min = min;
        this.max = max;
        this.spokenPhrase = spokenPhrase;
    }

    public String getHeading() {
        return heading;
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
