package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;


/*
Public Objective Card that counts the number of specific sets of dice in a window pattern.
Each set is formed by the properties (colors or values) specified in a set passed in the constructor
The Function, which gets the property of the dice, is passed in the constructor
This set is stored in the attribute 'items'

Attributes:
    items: the set of colors or values that form a set
    multiplier: the score multiplier that is specific for each different set of colors or values

Methods:
    calculateScore()
    getProperty()
    updateSets()
    countCompletedSets()
*/

public class SetPublicObjectiveCard extends PublicObjectiveCard {

    private Set<Object> items;

    private int multiplier;


    public SetPublicObjectiveCard(String title, String description, String imageURL, Set<Object> items,
                                  Function<Dice, Object> propertyFunction, int multiplier) {
        super(title, description, imageURL, propertyFunction);
        this.items = items;
        this.multiplier = multiplier;
    }

    //Returns a new SetPublicObjectiveCard instance with same properties of this one
    @Override
    public PublicObjectiveCard copy() {
        return new SetPublicObjectiveCard(super.getTitle(), super.getDescription(), super.getImageURL(),
                this.items, super.getPropertyFunction(), this.multiplier);
    }

    /*
    Calculates a player's score relative to the specific SetPublicObjectiveCard, given their window pattern

    Variables:
        numberOfCompletedSets: number of sets that contain the same elements (colors or values) as the 'items' set
        listOfSets: list of sets that can be formed with the dice of a WindowPattern. Not necessarily all sets of
                    the list will be completed sets
    */
    @Override
    public int calculateScore(WindowPattern windowPattern) {

        if(windowPattern==null){ throw new IllegalArgumentException("ERROR: Cannot calculate score of" +
                " a null window pattern."); }

        Cell[][] pattern = windowPattern.getPattern();
        if(pattern==null){ throw new IllegalArgumentException("ERROR: Pattern is null"); }

        int numberOfCompletedSets;
        List<HashSet<Object>> listOfSets = new ArrayList<>();
        Object currentProperty;

        listOfSets.add(new HashSet<>());

        for(int i=0; i<windowPattern.getNumberOfRows(); i++){
            for(int j=0; j < windowPattern.getNumberOfColumns(); j++){

                currentProperty = getProperty(pattern[i][j]);

                updateSets(listOfSets, currentProperty);
            }
        }

        numberOfCompletedSets = countCompletedSets(listOfSets);

        return this.multiplier*numberOfCompletedSets;
    }

    /*
    Gets the dice property (color or value) if there is a dice on the cell
    Otherwise returns null
    */
    private Object getProperty(Cell cell) {
        if(cell==null){ throw new IllegalArgumentException("ERROR: Cannot get the property of" +
                " a null cell."); }
        if(cell.hasDice()) {
            return super.getPropertyFunction().apply(cell.getDice());
        }else{
            return null;
        }
    }

    /*
    Updates the sets adding the current property if it is different from null and it is contained in 'items'
    */
    private void updateSets(List<HashSet<Object>> listOfSets, Object currentProperty) {

        if(listOfSets==null){ throw new IllegalArgumentException("ERROR: List of Sets cannot be null.");}

        /*
        If there is not a dice on the current cell or if the property to be evaluated is not contained in 'items',
        no set can become a completed set
        */
        if(currentProperty == null || !items.contains(currentProperty)){
            return;
        }

        /*
        Otherwise, the property is necessarily contained in the 'items' set
        For each set, if the set does not already contain the current property, then add it to it
        If all sets already have the current property, then create a new set containing the current property
        */
        for (HashSet<Object> set: listOfSets) {

            if (!set.contains(currentProperty)){
                set.add(currentProperty);
                return;
            }

            else if(listOfSets.indexOf(set) == listOfSets.size()-1){
                set = new HashSet<>();
                set.add(currentProperty);
                listOfSets.add(set);
                return;
            }
        }
    }

    /*
    Counts the number of sets that are equal to the requested set ('items') specified in the constructor of the card
    */
    private int countCompletedSets(List<HashSet<Object>> listOfSets) {
        if(listOfSets==null){ throw new IllegalArgumentException("ERROR: List of Sets cannot be null.");}

        int numberOfCompletedSets = 0;

        for (HashSet set: listOfSets) {
            if (set.equals(items)) {
                numberOfCompletedSets++;
            }
        }
        return numberOfCompletedSets;
    }

    @Override
    public String toString(){
        String s = super.toString();
        s = s.concat("Multiplier: " + multiplier);
        s = s.concat(System.lineSeparator());
        return s;
    }

}