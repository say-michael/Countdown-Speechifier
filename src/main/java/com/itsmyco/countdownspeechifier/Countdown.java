package com.itsmyco.countdownspeechifier;

import java.util.Random;

public class Countdown {
    private int step1Seconds;
    private int step2Seconds;
    private int step3Seconds;
    private int step4Seconds;

    public Countdown(int[] step1MinMax, int[] step2MinMax, int[] step3MinMax, int[] step4MinMax) {
        this.step1Seconds = new Random().nextInt(step1MinMax[0], step1MinMax[1]);
        this.step2Seconds = new Random().nextInt(step2MinMax[0], step2MinMax[1]);
        this.step3Seconds = new Random().nextInt(step3MinMax[0], step3MinMax[1]);
        this.step4Seconds = new Random().nextInt(step4MinMax[0], step4MinMax[1]);
    }

    public void startCountDown(){

    }
}
