package it.polimi.se2018.model;

import it.polimi.se2018.controller.NoMoreRoundsAvailableException;
import it.polimi.se2018.controller.NoMoreTurnsAvailableException;
import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import it.polimi.se2018.utils.EmptyListException;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.utils.ValueOutOfBoundsException;
import it.polimi.se2018.utils.message.MVMessage;
import it.polimi.se2018.utils.message.Message;

import java.util.*;

/**
 * Main class of the Model.
 *
 * An instance of this class represent a specific real game going on on the server.
 *
 * This class implements the OBSERVER PATTERN in order to notify all the Views connected.
 */
public class Game extends Observable implements Observer{

    /**
     * The number of rounds the game is composed of
     */
    private final int numberOfRounds;

    /**
     * The maximum number of players that the game can have
     */
    private final int maxNumberOfPlayers;

    /**
     * The number of turns that has each round. Round with
     * different number of turns are not allowed.
     */
    private final int numberOfTurnsPerRound;

    /**
     * Reference to the instance of the current {@link Round}
     */
    private Round currentRound;

    /**
     * Reference to the instance of the game's {@link Track}
     */
    private Track track;

    /**
     * List of players playing the game
     */
    private Set<Player> players;

    /**
     * List of ToolCards that were assigned to this game at the beginning of it
     */
    private List<ToolCard> drawnToolCards;

    /**
     * List of Public Objective Cards that were assigned to this game at the beginning of it
     */
    private List<PublicObjectiveCard> drawnPublicObjectiveCards;

    /**
     * Represents the status of the game
     */
    private GameStatus status;

    /**
     * Represents the final players' rankings (during the middle of the game is a null object)
     */
    private Map<Player, Integer> rankings;


    /**
     * Constructor for a new Game instance. Basic configuration is passed as argument.
     * Cards will be assigned later.
     *
     * @param numberOfRounds the number of rounds the game is composed of
     * @param maxNumberOfPlayers the maximum number of players that the game can have
     */
    public Game(int numberOfRounds, int maxNumberOfPlayers) {
        if(numberOfRounds < 0){
            throw new ValueOutOfBoundsException("Can't create a game with negative number of rounds"); }
        if(maxNumberOfPlayers <0 ){
            throw new ValueOutOfBoundsException("Can't create a game with negative number of players"); }
        this.currentRound = null;
        this.track = new Track();
        this.players = new HashSet<>();
        this.status = GameStatus.WAITING_FOR_CARDS;
        this.rankings = null;

        this.numberOfRounds = numberOfRounds;
        this.maxNumberOfPlayers = maxNumberOfPlayers;
        this.numberOfTurnsPerRound = maxNumberOfPlayers * 2;
    }

    /**
     * Returns the current round.
     *
     * @return the current round
     */
    public Round getCurrentRound() {
        return currentRound;
    }

    /**
     * Returns the current track.
     *
     * @return the current track
     */
    public Track getTrack() {
        return track;
    }

    /**
     * Returns the list of players.
     *
     * @return the list of players
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(this.players);
    }

    /**
     * Returns the list of ToolCards that were assigned to this game at the beginning of it.
     *
     * @return the list of ToolCards that were assigned to this game at the beginning of it
     */
    public List<ToolCard> getDrawnToolCards() {
        return drawnToolCards;
    }

    /**
     * Returns the list of Public Objective Cards that were assigned to this game at the beginning of it.
     *
     * @return the list of Public Objective Cards that were assigned to this game at the beginning of it
     */
    public List<PublicObjectiveCard> getDrawnPublicObjectiveCards() {
        return drawnPublicObjectiveCards;
    }

    /**
     * Returns the game's status.
     *
     * @return the game's status
     */
    public GameStatus getStatus() {
        return status;
    }

    /**
     * Assigns to the game the selected toolcards and publicobjectivecards (given randomly by the controller)
     *
     * @param drawnToolCards list of ToolCards that are assigned
     * @param drawnPublicObjectiveCards list of PublicObjectiveCards that are assigne
     */
    public void setCards(List<ToolCard> drawnToolCards, List<PublicObjectiveCard> drawnPublicObjectiveCards){
        if( this.status != GameStatus.WAITING_FOR_CARDS ){
            throw new BadBehaviourRuntimeException("Can't assign cards more than once to the game. Controller should not ask for it. Bad unhandleable behaviour.");
        }

        this.drawnToolCards = drawnToolCards;
        this.drawnPublicObjectiveCards = drawnPublicObjectiveCards;

        this.status = GameStatus.WAITING_FOR_PLAYERS;
    }

    /**
     * Gets the final rankings
     *
     * @return list of ordered players, first is the winner, null if not yet set
     */
    public Map<Player, Integer> getRankings() {
        return rankings;
    }

    /**
     * Sets the final rankings.
     *
     * @param rankings list of ordered players: first is winner
     */
    public void setRankings(Map<Player, Integer> rankings) {
        if(this.status != GameStatus.ENDED){ throw  new BadBehaviourRuntimeException("Can't set rankings if game is not ended. Controller should not ask for it. Bad unhandleable behaviour.");}
        if(rankings == null){ throw new IllegalArgumentException("Can't set rankings to null");}

        this.rankings = rankings;

        //NOTIFYING
        List<Player> rankingsAsList = new ArrayList<>(rankings.keySet());

        Map <String, Object> messageAttributes = new HashMap<>();

        messageAttributes.put("rankings", rankings);
        messageAttributes.put("winner", rankingsAsList.get(0));

        notify(new MVMessage(MVMessage.types.RANKINGS, messageAttributes));


    }

    /**
     * If possible, adds the given player to the game.
     *
     * @param player player to add to the game
     * @return if the action succeeded
     */
    public boolean addPlayer(Player player){
        if(this.status != GameStatus.WAITING_FOR_PLAYERS){ throw new BadBehaviourRuntimeException("Can't add player if game is not waiting for players. Controller should not ask for it. Bad unhandleable behaviour.");}

        return  players.size() < maxNumberOfPlayers &&
                players.add(player);
    }

    //TODO: commenta
    public void assignWindowPatternToPlayer(WindowPattern windowPattern, String playerID){
        for(Player p: players){
            if(p.getID().equals(playerID)){
                windowPattern.register(this);
                p.setWindowPattern(windowPattern);
                return;
            }
        }
    }

    /**
     * Returns if the given player is the current playing one.
     *
     * @param playerID the playerID to be checked
     * @return if the given player is the current playing one
     */
    public boolean isCurrentPlayer(String playerID) {
        return getCurrentRound().getCurrentTurn().isCurrentPlayer(playerID);
    }

    /**
     * Set the given ToolCard as used in the current Turn
     *
     * @param toolCard toolCard used in the current Turn
     */
    public void useToolCard(ToolCard toolCard){
        if(this.status != GameStatus.PLAYING){ throw new BadBehaviourRuntimeException("Can't use a toolcard if not playing");}

        if( !this.drawnToolCards.contains(toolCard) ) {
            throw new BadBehaviourRuntimeException("Asked to use a toolcard that is not in the drawn set");
        }
        this.drawnToolCards.get( this.drawnToolCards.indexOf(toolCard) ).use();
        this.getCurrentRound().getCurrentTurn().setUsedToolCard(toolCard);

        //NOTIFYING
        Map <String, Object> messageAttributes = new HashMap<>();

        //updates the toolcards as their tokens were updated
        messageAttributes.put("toolcards", drawnToolCards);
        //updates the player as their tokens were updated
        messageAttributes.put("currentPlayer", currentRound.getCurrentTurn().getPlayer());

        notify(new MVMessage(MVMessage.types.USED_TOOLCARD, messageAttributes));

    }

    /**
     * Gets the toolCard in the model that corresponds to the passed shallow copy.
     *
     * @param toolCardCopy the toolCard shallow copy.
     * @return the actual toolCard from the drawnToolCards, or null if no such card is found.
     *
     * @author Lorenzo Minto
     */
    public ToolCard getToolCard(ToolCard toolCardCopy) {

        for (ToolCard card: drawnToolCards) {
            if (card.equals(toolCardCopy)) { return card;}
        }
        throw new BadBehaviourRuntimeException("Asked to use a toolcard but it is not in the drawn set");
    }

    public void setStatusAsWaitingForPatternsChoice(){
        if(status==GameStatus.WAITING_FOR_PLAYERS){
            this.status = GameStatus.WAITING_FOR_PATTERNS_CHOICE;
        } else {
            throw new BadBehaviourRuntimeException();
        }
    }

    /**
     * Starts the game and creates the first round with the given dices
     * @param dices list of dices to be used for the first round
     */
    public void startGame(List<Dice> dices){
        if(dices == null){ throw new IllegalArgumentException("ERROR: Can't start game with null dices.");}
        if(dices.isEmpty()){ throw new EmptyListException("Can't start game with no dices.");}
        if(this.status != GameStatus.WAITING_FOR_PLAYERS){ throw new BadBehaviourRuntimeException("ERROR: Can't start game if not waiting for players.");}

        this.status = GameStatus.PLAYING;

        try {
            nextRound(dices);
        } catch (NoMoreRoundsAvailableException e) {
            throw new BadBehaviourRuntimeException();
        }

        //NOTIFYING
        Map <String, Object> messageAttributes = new HashMap<>();

        messageAttributes.put("drawnToolCards", drawnToolCards);
        messageAttributes.put("drawnPublicObjectiveCards", drawnPublicObjectiveCards);
        messageAttributes.put("players", players);
        messageAttributes.put("track", track);
        messageAttributes.put("draftPoolDices", dices);

        notify(new MVMessage(MVMessage.types.SETUP, messageAttributes));
    }

    /**
     * Proceed the game going to the next round (if available).
     *
     * @param dices the dices that are drafted from the dicebag for the new round
     * @throws NoMoreRoundsAvailableException if the method is called but all the rounds
     * that could have been played in this game were actually already played
     */
    public void nextRound(List<Dice> dices) throws NoMoreRoundsAvailableException{
        if(dices == null){ throw new IllegalArgumentException("ERROR: Can't proceed to next round with null dices.");}
        if(dices.isEmpty()){ throw new EmptyListException("Can't proceed to next round with no dices.");}
        if(this.status != GameStatus.PLAYING){ throw new BadBehaviourRuntimeException("Can't proceed to next round if game is not already running"); }

        int nextRoundNumber;
        if( this.currentRound == null ){
            nextRoundNumber = 0;
        } else {
            nextRoundNumber = this.currentRound.getNumber() + 1;
        }

        //get the remaining dices in draftpool and put them in the track
        if(currentRound != null && currentRound.getNumber() != 0) {
            this.track.processDices(currentRound.getDraftPool().getDices());
        }

        if(nextRoundNumber > numberOfRounds - 1){
            this.status = GameStatus.ENDED;
            throw new NoMoreRoundsAvailableException();
        }



        DraftPool draftPool = new DraftPool(dices);
        draftPool.register(this);
        this.currentRound = new Round(nextRoundNumber, numberOfTurnsPerRound, getPlayers(), draftPool);

        //NOTIFYING
        Map <String, Object> messageAttributes = new HashMap<>();

        messageAttributes.put("track", track);
        messageAttributes.put("draftPoolDices", dices);

        notify(new MVMessage(MVMessage.types.NEXT_ROUND, messageAttributes));

    }

    public void nextTurn() throws NoMoreTurnsAvailableException {
        try {
            getCurrentRound().nextTurn();

            //NOTIFYING
            List<Player> nextPlayers = getCurrentRound().getPlayersInOrderFromTurn(getPlayers(), numberOfTurnsPerRound);

            Map <String, Object> messageAttributes = new HashMap<>();

            //to be displayed in view
            messageAttributes.put("nextPlayers", nextPlayers);

            notify(new MVMessage(MVMessage.types.NEXT_TURN, messageAttributes));

        } catch (NoMoreTurnsAvailableException e) {
            throw new NoMoreTurnsAvailableException();
        }
    }

    @Override
    public boolean update(Message m) {
        notify(m);
        return true;
    }
}
