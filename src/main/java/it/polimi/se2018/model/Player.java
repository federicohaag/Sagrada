package it.polimi.se2018.model;

public class Player {

    private int favorTokens;

    private int score;

    private String nickname;

    private WindowPattern windowPattern;

    private User user;

    private PrivateObjectiveCard privateObjectiveCard;

    public Player(User user, String nickname) {
        this.user = user;

        this.nickname = nickname;
        this.score = 0;

        this.windowPattern = null;
        this.favorTokens = 0;

        this.privateObjectiveCard = null;
    }

    public Player(User user, String nickname, WindowPattern windowPattern, PrivateObjectiveCard card) {
        this.user = user;

        this.nickname = nickname;
        this.score = 0;

        this.windowPattern = windowPattern;
        this.favorTokens = windowPattern.getDifficulty();

        this.privateObjectiveCard = card;
    }

    //Can be assigned only one time at all
    public void setWindowPattern(WindowPattern windowPattern) {
        if(this.windowPattern==null){
            this.windowPattern = windowPattern;
            this.favorTokens = windowPattern.getDifficulty();
        }
    }

    //Can be assigned only one time at all
    public void setPrivateObjectiveCard(PrivateObjectiveCard card) {
        if(this.privateObjectiveCard==null){
            this.privateObjectiveCard = card;
        }
    }

    //Decrease favorTokens of quantity. Return false if not enough tokens left.
    public boolean decreaseTokens(int quantity) {
        if(favorTokens<quantity) return false;
        favorTokens -= quantity;
        return true;
    }

    //Compares favorTokens with toolCard.getNeededTokens()
    public boolean canUseToolCard(ToolCard toolCard) {
        return toolCard.getNeededTokens() <= favorTokens;
    }
}