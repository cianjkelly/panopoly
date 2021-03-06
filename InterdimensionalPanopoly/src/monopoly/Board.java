package monopoly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.concurrent.ThreadLocalRandom;

import base.PersonOfInterest;
import interfaces.Locatable;

public class Board 
{
	private ArrayList<Locatable> locations = new ArrayList<Locatable>();
	private ArrayList<String> addresses = new ArrayList<String>();
	private ArrayList<String> currentSet = new ArrayList<String>();
	private Group currentGroup;

	LinkedHashMap<String, ArrayList<String>> possibleLocations = new LinkedHashMap<String, ArrayList<String>>();
	private int index = 0;
	
	private PersonOfInterest ps = new PersonOfInterest();
	
	private int minPrice = 50;
	private int maxPrice = 100;
	
	Board(int numLocations)
	{
		int priceIncrease = 44/ numLocations;
		int setIndex = 0;

		generateLocations();
		do 
		{
			currentGroup = getNextSet();
		} while(currentSet.size() < 2);
		
		//Fill in locations for board - hard-coded Named Locations/Tax and Utilities
		for(int i = 0; i < numLocations; i++)
		{
			if(i == 0)
				locations.add(new NamedLocation("GO"));
			else if(i == numLocations/4)
				locations.add(new NamedLocation("Jail/Just Visiting"));
			else if(i == numLocations/4 + 2)
				locations.add(generateUtility());
			else if(i == (numLocations*3)/4)
				locations.add(new NamedLocation("Go to Jail"));
			else if(i == ((numLocations*3)/4)-2)
				locations.add(generateUtility());
			else if(i == numLocations/2)
				locations.add(new NamedLocation("Free Parking"));
			else if(i == 4)
				locations.add(new TaxableProperty("Income Tax", 200));
			else if(i == numLocations-2)
				locations.add(new TaxableProperty("Super Tax", 100));
			//one in 8 chance that property is station, 1 in 4 card or 5 in 8 investment
			else
			{
				int rnd = ThreadLocalRandom.current().nextInt(1, 7 + 1);
				if(rnd == 1) 
					locations.add(generateStation());
				else if(rnd == 2)
					locations.add(new Chance());
				else if(rnd == 3)
					locations.add(new CommunityChest());
				else
				{
					int price = generatePrice();
					
					int buildCost = 0;
					//first side
					if(locations.size() < numLocations/4)
						buildCost = 50;
					else if(locations.size() < numLocations/2)
						buildCost = 100;
					else if(locations.size() < (numLocations*3)/4)
						buildCost = 150;
					else
						buildCost = 200;					
					
					if(setIndex == currentSet.size() || setIndex == 3)
					{
						do
						{
							currentGroup = getNextSet();
						} while(currentSet.size() < 2);
						
						setIndex = 0;
					}
					
					locations.add(new InvestmentProperty(currentSet.get(setIndex), price, generateRentArray(price), (price/2), buildCost, currentGroup));
					
					setIndex++;

					minPrice += priceIncrease * 10;
					maxPrice += priceIncrease * 15;
				}
			}
		}
	}
	
	private void generateLocations()
	{
		ArrayList<String> locs = PersonOfInterest.locations;
		String[] splitLoc = null;
		String[] splitAddress = null;
		
		Collections.shuffle(locs);
		
		for(String loc : locs)
		{
			splitLoc = loc.split(" \\(");
			if(splitLoc[1] != null)
			{
				ArrayList<String> temp = new ArrayList<String>();
				String group = splitLoc[1].replaceAll("\\)", "");
				
				//key = group, value += address
				if(possibleLocations.containsKey(group))
					temp = possibleLocations.get(group);
				
				//split multi-line addresses
				splitAddress = splitLoc[0].split(", ");
				
				for(String a: splitAddress)
				{
					//check for duplicates across groups
					if(!addresses.contains(a))
					{
						temp.add(a);
						addresses.add(a);
					}
				}
				
				possibleLocations.put(group, temp);
			}
		}
	}
	
	private Station generateStation()
	{
		String stationName = ps.TransportLinks();
		
		return new Station(stationName);
	}
	
	private Utility generateUtility()
	{
		return new Utility(ps.utility());
	}
	
	private Group getNextSet()
	{
		String nextKey = (String) possibleLocations.keySet().toArray()[index];
		currentSet = possibleLocations.get(nextKey);
		index++;
		Group group = new Group(nextKey);
		return group;
	}
	
	public Locatable getLocation(int squareLocation)
	{
		return locations.get(squareLocation);
	}

	public ArrayList<Locatable> getLocations()
	{
		return locations;
	}
	
	private int generatePrice()
	{
		return ThreadLocalRandom.current().nextInt(minPrice, maxPrice + 1);
		
	}
	
	//REFERENCE: http://www.jdawiseman.com/papers/trivia/monopoly-rents.html
	private int[] generateRentArray(int price)
	{
		int rent = price/10 - 4;
		int oneHouseRent = (price/2) - 20;

		int[] array = {rent, oneHouseRent, 3*oneHouseRent, 6*oneHouseRent, 210 + 7*oneHouseRent, 300 + 5*oneHouseRent};
		
		if(price >= 150)
		{
			array[5] += 300;
		}
		
		return array;
	}
	
	public int getNumLocations()
	{
		return locations.size();
	}
	
	public int getJailLocation()
	{
		return getNumLocations()/4;
	}
}
