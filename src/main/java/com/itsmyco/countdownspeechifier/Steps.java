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
        steps.add(Step.blank(1));
        steps.add(Step.blank(2));
        steps.add(Step.blank(3));
        steps.add(Step.blank(4));
    }

    public List<Step> getSteps() {
        return steps;
    }

}
