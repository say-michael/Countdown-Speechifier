package com.itsmyco.countdownspeechifier;

import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Settings {
    private final File settingsFile = new File("speechifier-settings.txt");

    public void save(Step[] steps){
        try (var output = new PrintWriter(settingsFile)){
            for (int i = 0; i < 4; i++) {
                output.println(steps[i].getHeading() + "§" + steps[i].getMin() + "§" +  steps[i].getMax()+ "§" + steps[i].getSpokenPhrase());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Step[] load(){
        if (settingsFile.exists()){
            Step[] steps = new Step[4];
            try {
                var input = new Scanner(settingsFile);
                int index = 0;
                while (input.hasNextLine()){
                    var arr = input.nextLine().split("§");
                    var a = arr[0].equals("~") ? "" : arr[0];
                    var d = arr[3].equals("~") ? "" : arr[3];
                    steps[index++] = new Step(index, a, Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), d);
                }
                input.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            return steps;
        }
        return null;
    }
}
