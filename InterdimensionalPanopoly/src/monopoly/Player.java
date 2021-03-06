package monopoly;

import java.util.ArrayList;

import interfaces.Groupable;
import interfaces.Locatable;
import interfaces.Ownable;
import interfaces.Playable;
import interfaces.Rentable;

public class Player extends Thread implements Playable, Locatable {
	
	private static final int STARTING_BALANCE = 1500;
	private int playerIndex;
	private GUI gui;

	protected String name;
	protected int balance;
	private ArrayList<Rentable> properties = new ArrayList<>();
	private int position;
	private int imageIndex=0;
	public Panopoly panopoly;
	boolean canRoll = true;
	boolean rollComplete = false;
	private boolean inJail = false;
	private int GOOJFree;
	int doubles;
	
	public Player(String name, int imageIndex, int playerIndex,Panopoly panopoly)
	{
		this.panopoly=panopoly;
		this.playerIndex = playerIndex;
		this.imageIndex=imageIndex;
		this.name = name;
		balance = STARTING_BALANCE;
		position = 0;
		doubles = 0;
		GOOJFree = 0;
	}

	@Override
	public String getIdentifier() 
	{
		return name;
	}

	@Override
	public int getPosition()
	{
		return position;
	}

	 int getImageIndex()
	{
		return imageIndex;
	}
	 
	@Override
	public int getBalance() 
	{
		return balance;
	}
	
	public int getPlayerIndex() {
		return playerIndex;
	}

	public ArrayList<Rentable> getProperties() {
		return properties;
	}
	
	//If game ends early - get winner by net worth = balance + value of properties
	@Override
	public int getNetWorth() 
	{
		int netWorth = balance;
		
		for(Ownable p: properties)
		{
			netWorth += p.getValue();
		}
		
		return netWorth;
	}
	
	public String move(int squares, boolean clockwise)
	{
		String ret = "";
		
		if(clockwise)
		{
			position += squares;
			//if they pass GO
			if(position>=panopoly.getBoard().getNumLocations()) {
				position = position - panopoly.getBoard().getNumLocations();
				ret += "\n" + getIdentifier() + " has passed GO and earned 200.";
				earn(200);
			}
		}
		
		else
		{
			position -= squares;
			//if they pass GO
			if(position<0) {
				position = position + panopoly.getBoard().getNumLocations();
				ret += getIdentifier() + " has passed GO and earned 200.";
				earn(200);
			}

		}
		
		return ret;
	}
	
	 void pay(int payment)
	{
		balance -= payment;
	}
	
	 void earn(int earnings)
	{
		balance += earnings;
	}
	
	//PROPERTY METHODS
	public void buyProperty(Rentable property, int price)
	{
		pay(price);
		property.setOwner(this);
		properties.add(property);
	}
	
	//sell property to other player
	public void sellProperty(Rentable property, int sellPrice, Player newOwner)
	{
		balance += sellPrice;
		properties.remove(property);
		
		newOwner.buyProperty(property, sellPrice);
	}

	public boolean ownsGroup(Group group)
	{
		boolean isGroupOwner = true;
		
		for(Groupable property: group.getMembers())
		{
			if(!properties.contains(property))
			{
				isGroupOwner = false;
			}
		}
		
		return isGroupOwner;
	}
	
	 boolean hasProperty()
	{
		return !properties.isEmpty();
	}
	
	 int ownedStations()
	{
		int stations = 0;
		
		for(Rentable p: properties)
		{
			if(p instanceof Station)
			{
				stations++;
			}
		}
		
		return stations;
	}
	
	public int ownedUtilities()
	{
		int utilities = 0;
		
		for(Rentable p: properties)
		{
			if(p instanceof Utility)
			{
				utilities++;
			}
		}
		
		return utilities;
	}
	
	//JAIL METHODS
	public void addGOOJFree()
	{
		GOOJFree += 1;
	}
	
	public void useGOOJFree()
	{
		GOOJFree -= 1;
		releaseFromJail();
	}
	
	public boolean hasGOOJFree()
	{
		if(GOOJFree == 0)
			return false;
		else
			return true;
	}
	
	public boolean isInJail()
	{
		return inJail;
	}
	
	//On send to jail, end current player's turn
	public void sendToJail()
	{
		position = panopoly.getBoard().getJailLocation();
		inJail = true;
		panopoly.startPlayerTurn(panopoly.getNextPlayer());
	}
	
	//On release, move to next player's turn
	public void releaseFromJail()
	{
		inJail = false;
		panopoly.startPlayerTurn(panopoly.getNextPlayer());
	}
	public GUI getGUI()
	{
		return gui;
	}
	public void setGUI(GUI gui)
	{
		this.gui=gui;
	}
}