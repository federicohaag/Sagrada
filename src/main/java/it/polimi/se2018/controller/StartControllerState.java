package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

public class StartControllerState implements ControllerState {
    Controller controller;

    public StartControllerState(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void draftDiceFromDraftPool(Dice dice, View view) {
        Round currentRound = controller.game.getCurrentRound();

        if (currentRound.getDraftPool().draftDice(dice)) {
            currentRound.getCurrentTurn().setDraftedDice(dice);
        }
        controller.setControllerState(controller.stateManager.getPlaceState());
    }

    @Override
    public void placeDice(int row, int col, View view) {
        view.showMessage("Can't do. You have to choose a dice from the draft pool first.");
    }

    @Override
    public void useToolCard(Player player, ToolCard toolcard, View view) {
        if (controller.canUseSpecificToolCard(toolcard)) {
            controller.setActiveToolCard(toolcard);
            controller.stateManager.getNextState(this);
        } else {
            view.showMessage("Can't use this toolcard.");
        }
    }

    @Override
    public void chooseDiceFromTrack(Dice dice, int slotNumber, View view) {
        view.showMessage("Can't do. Choose a dice from the draft pool or activate a ToolCard");
    }

    @Override
    public void moveDice(int rowFrom, int colFrom, int rowTo, int colTo, View view) {
        view.showMessage("Can't do. Choose a dice from the draft pool or activate a ToolCard");
    }

    @Override
    public void incrementDice(View view) {
        view.showMessage("Can't do. Choose a dice from the draft pool or activate a ToolCard");
    }

    @Override
    public void decrementDice(View view) {
        view.showMessage("Can't do. Choose a dice from the draft pool or activate a ToolCard");
    }

    @Override
    public void chooseDiceValue(int value, View view) {
        view.showMessage("Can't do. Choose a dice from the draft pool or activate a ToolCard");
    }

    @Override
    public void executeImplicitBehaviour() {

    }
}
