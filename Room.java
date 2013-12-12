// =====================================================================================================
// Room.java
// againessmith13, jpham14, mmillian15, cbottum15
// Fall 2013
// =====================================================================================================
/**
 * An object that holds information about rooms on Amherst College campus.
 */
public class Room extends Node{
	// =================================================================================================	
	
	// =================================================================================================
	/**
	 * Constructs a Room from the specified list of parameters. The list of parameters is expected to 
	 * have the following structure:
	 * building;number;capacity;type;accessible;buildingShort;CP;DVD;VCR;Slides;OH
	 * @param parameters - the list of parameters from which to construct the Room
	 */
	public Room (String[] parameters){
		String building=parameters[0];
		String roomNumber=parameters[1];
		int capacity=Integer.parseInt(parameters[2]);
		String type=parameters[3];
		String buildingShort=parameters[5];
		setName(buildingShort+" "+roomNumber);
		setCapacity(capacity);
		setType(type);
	}
	// =================================================================================================
	
	// =================================================================================================
	/**
	 * Returns a string representation of this Room. The string representation consists of the string 
	 * representation of the abbreviation for the building and the room number separated by a space.
	 * @return the String representation of this Room 
	 */
	public String toString(){
		return this.getName();
	}	
	// =================================================================================================
}//class Room
// =====================================================================================================

