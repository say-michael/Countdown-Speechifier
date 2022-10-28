package com.itsmyco.countdownspeechifier;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "steps")
@XmlAccessorType(XmlAccessType.FIELD)
public class Steps {
    @XmlElement(name = "step")
    List<Step> steps = new ArrayList<>();

    public Steps(){
        steps.add(new Step(1));
        steps.add(new Step(2));
        steps.add(new Step(3));
        steps.add(new Step(4));
    }

    public List<Step> getSteps() {
        return steps;
    }

}
