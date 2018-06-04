package it.polimi.se2018.view;

import it.polimi.se2018.controller.ObjectiveCardManager;
import it.polimi.se2018.controller.ToolCardManager;
import it.polimi.se2018.model.EmptyPlacementRule;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.networking.Client;
import it.polimi.se2018.utils.Move;
import it.polimi.se2018.utils.message.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;

import java.io.File;
import java.net.URL;
import java.util.*;

public class SagradaSceneController extends View implements Initializable {
    private Client client;
    private List<Image> cards = new ArrayList<>();
    private static final int numberOfToolCards = 3;
    private static final int numberOfPublicObjectiveCards = 3;
    private int cardCarouselCurrentIndex;

    private ToolCardManager toolCardManager = new ToolCardManager(new EmptyPlacementRule());
    private ObjectiveCardManager objectiveCardManager = new ObjectiveCardManager();

    @FXML private TextArea playerTerminal;
    @FXML private HBox dynamicChoicesPane;
    @FXML private HBox cardsCarouselCardHBox;
    @FXML private ImageView cardsCarouselCardImageView;
    @FXML private StackPane cardsCarouselFavorTokensStackPane;

    @FXML private ImageView cardsCarouselFavorTokensImageView;
    @FXML private Label cardsCarouselFavorTokensValue;

    @FXML private HBox cardsCarouselPreviousHBox;
    @FXML private ImageView cardsCarouselPreviousImageView;

    @FXML private HBox cardsCarouselNextHBox;
    @FXML private ImageView cardsCarouselNextImageView;
    @FXML private GridPane cardsCarouselGridPane;


// DO NOT DELETE THIS COMMENT
//
// File file = new File("src/main/resources/images/toolcard1.png");
// Image toolcard = new Image(file.toURI().toString());


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setDrawnToolCards(toolCardManager.getRandomToolCards(3));
        setDrawnPublicObjectiveCards(objectiveCardManager.getPublicObjectiveCards(3));

        //getting the cards images
            drawnToolCards.forEach(card
                    -> cards.add(new Image((new File(card.getImageURL())).toURI().toString())));
        drawnPublicObjectiveCards.forEach(card
                -> cards.add(new Image((new File(card.getImageURL())).toURI().toString())));
        cards.add(new Image((new File(getPrivateObjectiveCard().getImageURL())).toURI().toString()));

        cardCarouselCurrentIndex = 0;
        updateCardCarousel();

        //setting favor tokens image and next and previous buttons
        setImageWithHeightAndWidth(cardsCarouselFavorTokensImageView,
                new Image((new File("src/main/resources/images/FavorToken.jpg")).toURI().toString()),
                cardsCarouselFavorTokensStackPane);

        setImageWithHeightAndWidth(cardsCarouselPreviousImageView,
                new Image((new File("src/main/resources/images/Previous.jpg")).toURI().toString()),
                cardsCarouselPreviousHBox);

        setImageWithHeightAndWidth(cardsCarouselNextImageView,
                new Image((new File("src/main/resources/images/Next.jpg")).toURI().toString()),
                cardsCarouselNextHBox);


        //setting a nice background
        Image backgroundImage = new Image((new File("src/main/resources/images/SagradaBackground.jpg")).toURI().toString());
        cardsCarouselGridPane.setBackground(new Background(new BackgroundFill(new ImagePattern(backgroundImage), CornerRadii.EMPTY, Insets.EMPTY)));

    }

    @FXML
    public void handleCardCarouselNext(){
        if(cardCarouselCurrentIndex == cards.size()-1){
            cardCarouselCurrentIndex = 0;
        }else {
            cardCarouselCurrentIndex ++;
        }
        updateCardCarousel();
    }

    @FXML
    public void handleCardCarouselPrevious(){
        if(cardCarouselCurrentIndex == 0){
            cardCarouselCurrentIndex = cards.size()-1;
        }else {
            cardCarouselCurrentIndex --;
        }
        updateCardCarousel();
    }

    private void updateCardCarousel() {
        setImageWithHeightAndWidth(
                cardsCarouselCardImageView,
                cards.get(cardCarouselCurrentIndex),
                cardsCarouselCardHBox);

        if(cardCarouselCurrentIndex<numberOfToolCards) {
            cardsCarouselFavorTokensValue.setText(String.valueOf(drawnToolCards.get(cardCarouselCurrentIndex).getUsedTokens()));
        }else{
            cardsCarouselFavorTokensValue.setText("");
        }
    }

    private void setImageWithHeightAndWidth(ImageView imageView, Image image, Pane pane) {
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(pane.widthProperty());
        imageView.fitHeightProperty().bind(pane.heightProperty());
    }

    public void handleCardCarouselToolCardsButtonPressed(){
        cardCarouselCurrentIndex = 0;
        updateCardCarousel();
    }

    public void handleCardCarouselPublicsButtonPressed(){
        cardCarouselCurrentIndex = numberOfToolCards;
        updateCardCarousel();
    }

    public void handleCardCarouselPrivateButtonPressed(){
        cardCarouselCurrentIndex = numberOfToolCards + numberOfPublicObjectiveCards;
        updateCardCarousel();
    }

    @Override
    protected void updatePermissions(Message m) {
        super.updatePermissions(m);
        new Thread(new Runnable() {
            @Override public void run() {
                if (getPermissions().isEmpty()) {
                    //add message? No move available at the moment
                } else {
                    Set<Move> permissions = getPermissions();
                    //TODO: each move should have a literal representation, not hardcoded here
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            dynamicChoicesPane.getChildren().clear();
                            for (Move m : permissions) {
                                Button button = new Button(m.toString());
                                button.setId(m.toString());
                                button.setOnAction(event -> checkID(button));
                                dynamicChoicesPane.getChildren().add(button);
                            }
                        }
                    });
                }
            }
        }).start();

    }

    @Override
    void handleAddedEvent() {

    }

    @Override
    void handleRemovedEvent() {

    }

    private void checkID(Button button){
        //TODO: Button action handling here -> will correspond to the start of a move.
        switch (button.getId()) {
            case "DRAFT_DICE_FROM_DRAFTPOOL":

                break;
            default:
                break;
        }
    }

    @Override
    void handleLeaveWaitingRoomMove() {

    }

    @Override
    Message handleEndTurnMove() {
        return null;
    }

    @Override
    Message handleDraftDiceFromDraftPoolMove() {
        return null;
    }

    @Override
    Message handlePlaceDiceOnWindowPatternMove() {
        return null;
    }

    @Override
    Message handleUseToolCardMove() {
        return null;
    }

    @Override
    Message handleIncrementDraftedDiceMove() {
        return null;
    }

    @Override
    Message handleDecrementDraftedDiceMove() {
        return null;
    }

    @Override
    Message handleChangeDraftedDiceValueMove() {
        return null;
    }

    @Override
    Message handleChooseDiceFromTrackMove() {
        return null;
    }

    @Override
    Message handleMoveDiceMove() {
        return null;
    }

    @Override
    Message handleJoinGameMove() {
        return null;
    }

    @Override
    void handleGameEndedEvent(LinkedHashMap<String, Integer> rankings) {

    }

    @Override
    void handleGiveWindowPatternsEvent(List<WindowPattern> patterns) {

    }

    @Override
    void notifyHandlingOfMessageEnded() {

    }

    @Override
    void notifyHandlingOfMessageStarted() {

    }

    @Override
    void showMessage(String message) {
        printOnConsole(message);
    }

    @Override
    void errorMessage(String message) {

    }

    @Override
    void notifyGameVariablesChanged() {

    }

    @Override
    void notifyGameVariablesChanged(boolean forceClean) {

    }

    @Override
    void notifyGameStarted() {

    }

    @Override
    void notifyPermissionsChanged() {

    }

    public void setClient(Client c) {
        this.client = c;
    }

    protected void printOnConsole(String s) {
        String ss = "\n"+s;
        playerTerminal.appendText(ss);
    }

}
