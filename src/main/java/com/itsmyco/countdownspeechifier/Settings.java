package com.itsmyco.countdownspeechifier;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.Scanner;

public class Settings {
    private static final File settingsFile = new File(System.getProperty("user.home") + "/Library/Application\sSupport/speechifier-settings.xml");

    public static void saveSteps(Steps steps, File file) throws JAXBException{
        JAXBContext context = JAXBContext.newInstance(Steps.class);
        Marshaller mar= context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//        if (!file.exists()){
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
        mar.marshal(steps, file);
    }

    public static void saveSteps(Steps steps) throws JAXBException {
        Settings.saveSteps(steps, settingsFile);
    }

    public static Steps loadSteps(File file) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Steps.class);
        var mar = context.createUnmarshaller();
        return (Steps) mar.unmarshal(file);
    }

    public static Steps loadSteps() throws JAXBException {
        if (!settingsFile.exists()){
            saveSteps(new Steps());
        }
        return loadSteps(settingsFile);
    }

}
