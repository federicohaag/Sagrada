package it.polimi.se2018.utils;


import it.polimi.se2018.model.ChangeMessage;

/**
 * A class can implement the {@link Observer} interface when it
 * wants to be informed of changes in observable objects.
 */
public interface Observer {

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an {@link Observable} object's
     * {@link Observable#notify(ChangeMessage)} method to have all the object's
     * observers notified of the change.
     *
     * @param m an argument passed to the {@link Observable#notify(ChangeMessage)} method
     */
    void update(ChangeMessage m);
}