package it.polimi.se2018.view;

import it.polimi.se2018.networking.message.Message;

public class GUIView extends View{

    public GUIView() {
        super();
    }


    @Override
    public boolean update(Message m) {
        return true;
    }
}
