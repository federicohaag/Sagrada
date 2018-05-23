package it.polimi.se2018.utils;

/**
 * The class {@code BadDiceReferenceException} is a form of {@link Exception}
 * that is thrown when is request to perform an action on a dice that does not
 * exists or is not contained in the array / group where it is looked for.
 */
public class BadDiceReferenceException extends Exception{
    /**
     * Constructor for BadDiceReference Exception
     */
    public BadDiceReferenceException(String message) {
        super(message);
        //do nothing else
    }
}
