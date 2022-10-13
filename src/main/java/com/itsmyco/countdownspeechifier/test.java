package com.itsmyco.countdownspeechifier;

import io.github.jonelo.jAdapterForNativeTTS.engines.SpeechEngine;
import io.github.jonelo.jAdapterForNativeTTS.engines.SpeechEngineNative;
import io.github.jonelo.jAdapterForNativeTTS.engines.Voice;
import io.github.jonelo.jAdapterForNativeTTS.engines.exceptions.NotSupportedOperatingSystemException;

public class test {
    public static void main(String[] args) throws NotSupportedOperatingSystemException {
        var se = SpeechEngineNative.getInstance();
        var voices = se.getAvailableVoices();
        for (Voice voice : voices) {
            System.out.println(voice);
        }
    }
}
