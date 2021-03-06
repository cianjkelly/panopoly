package monopoly;

public class GameBot extends Player{

    private int bidsMade = 0;
    private GUI gui;

    public GameBot(String name, int imageIndex, int playerIndex, Panopoly panopoly) {
        super(name, imageIndex, playerIndex, panopoly);
    }

    @Override
    public String getIdentifier() {
        return name+"(BOT)";
    }

    public void makeGameDecision() {
        if(isInJail()){
            gui.answerCorrectlyFunction();
        } else if(gui.auctionTimer.isVisible()){
            gui.bidFunction();
        } else if(gui.buildButton.isVisible()){
            gui.buildHouseFunction();
        } else if(gui.rollButton.isVisible()) {
            gui.rollFunction();
        } else if(gui.exitGame.isVisible()){
            gui.wonGameQuitFunction();
        } else if(gui.buyButton.isVisible()){
            gui.buyPropertyFunction();
        } else if(gui.endButton.isVisible()){
            gui.endTurnFunction();
        } else if(gui.mortgageButton.isVisible() && balance<0){
            gui.mortgagePropertyFunction();
        } else if(balance<400 && gui.redeemButton.isVisible()){
            gui.redeemPropertyFunction();
        } else if(balance<0){
            gui.leaveGameFunction();
        } else{
            gui.leaveGameFunction();
        }
    }
    public void setGUI(GUI gui)
    {
        this.gui=gui;
    }
}
