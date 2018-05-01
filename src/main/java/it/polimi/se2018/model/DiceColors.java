package it.polimi.se2018.model;

import java.util.Random;

public enum DiceColors {
    NOCOLOR,
    RED,
    YELLOW,
    GREEN,
    BLUE,
    PURPLE;

    public static DiceColors getRandomColor(){
        Random random = new Random();
        DiceColors randomColor;

        do{
            randomColor = values()[random.nextInt(values().length)];
        } while (randomColor.equals(DiceColors.NOCOLOR));

        return randomColor;
    }

    public String toOneLetter(){
        switch(this) {
            case NOCOLOR: return "_";
            case RED: return "R";
            case YELLOW: return "Y";
            case GREEN: return "G";
            case BLUE: return "B";
            case PURPLE: return "P";
            default: throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

}
