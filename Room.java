// =========================================================================================================================================================================
// Room.java
// Connor Bottum, Justyn Pham, Lexie Gaines-Smith and Michael Millian
// Fall 2013
// =========================================================================================================================================================================


import java.util.*;
import java.lang.*;
import java.io.*;

// =========================================================================================================================================================================
/**
 * An object that holds information about rooms on Amherst College Campus
 */

public class Room{
// =========================================================================================================================================================================	
	private String roomNumber; 
	private int capacity;
	private String building;
	private String buildingShort;
	private boolean isAccessible;
    private String type; //lab,seminar,lecture,small,studio
   	boolean[] tech;
	private int numberOfSlides;
    private int[][] timesAssigned = new int[5][48]; //boolean[day][half-hour]
    private ArrayList<Course> courses;
    

// =========================================================================================================================================================================
	/** The constructor.
	* @param accessible boolean to indicate if the room is near an elevator
	* @param building full building name
	* @param buildingShort abbreviation for building name
	* @param capacity capacity of the room
	* @param roomNumber number of the room in the building
	* @param type kind of room such as lab or lecture
	*/  
    public Room(boolean accessible,String building,String buildingShort,int capacity,String roomNumber,String type){
    	//Initialize the globals
    	this(building,roomNumber);
    	this.capacity=capacity;
    	this.buildingShort=buildingShort;
    	this.isAccessible=accessible;
    	this.type=type.toLowerCase();

    } // Room
// =========================================================================================================================================================================
// =========================================================================================================================================================================
	/**
	 *  Secondary constructor for testing 
	 * @param building name of building
	 * @param roomNumber room number
	 */
    public Room (String building, String roomNumber){//Secondary constructor for use in generateCourses method
    	//Initializes with all fields available in dummy data
    	this.building=building;
    	this.roomNumber=roomNumber;
    	this.initRoomAvailable();
    }// Room
// =========================================================================================================================================================================   
// =========================================================================================================================================================================
    /**
     * setTechnology sets booleans to indicate what technology a room has available
     * @param t the array of technology booleans for this Room
     * @param numberOfSlides int to indicates the number of slides this Room has
     */
    public void setTechnology(boolean[] t, int numberOfSlides){
    		this.tech=t;
    		this.numberOfSlides=numberOfSlides;
    	}//setTechnology
// =========================================================================================================================================================================    

// ========================================================================================================================================================================= 
   /**
    * getTechnology returns the booleans indicating the Room's technological capabilities
    * @return array of booleans indicating the Room's technological capabilities
    */
   public boolean[] getTechnology(){
	  return this.tech;
  }//getTechnology
// =========================================================================================================================================================================   

// ========================================================================================================================================================================= 
   /**
    * getNumberOfSlides
    * @return int of number of slides in that Room
    */
	public int getNumberOfSlides() {
		return numberOfSlides;
	}//getNumberOfSlides
// ========================================================================================================================================================================= 
// ========================================================================================================================================================================= 
	/**
	 * getTimesAssigned
	 * @return int [][] of times the Room is occupied
	 */
	public int[][] getTimesAssigned() {
		return timesAssigned;
	}//getTimesAssigned
// ========================================================================================================================================================================= 
// ========================================================================================================================================================================= 
	/**
	 * inSameBuilding
	 * @param r another Room
	 * @return boolean indicating if r is in the same building as this Room
	 */
	public boolean inSameBuilding(Room r){
    	return this.building==r.getBuilding();
    }//inSameBuilding
// ========================================================================================================================================================================= 
// ========================================================================================================================================================================= 
	/**
	 * getRoomNumber
	 * @return String of this Room's room number
	 */
	public String getRoomNumber() {
		return roomNumber;
	}//getRoomNumber
// ========================================================================================================================================================================= 
// ========================================================================================================================================================================= 
	/**
	 * setRoomNumber
	 * @param roomNumber a String representation of a room number
	 */
	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}//setRoomNumber(String roomNumber)
// ========================================================================================================================================================================= 
// ========================================================================================================================================================================= 
	/**
	 * getCapacity
	 * @return capacity of Room
	 */
	public int getCapacity() {
		return capacity;
	}//getCapacity
// =========================================================================================================================================================================
// =========================================================================================================================================================================
	/**
	 *  setCapacity
	 * @param capacity is the capacity we are setting for this Room
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}//setCapacity(int capacity)
// =========================================================================================================================================================================	
// =========================================================================================================================================================================
	/**
	 * inBuilding
	 * @param building String representation of a building name
	 * @return whether this Room is in building
	 */
	public boolean inBuilding(String building){
		return this.building.equals(building);
	}//inBuilding
// =========================================================================================================================================================================
// =========================================================================================================================================================================
	/**
	 * getBuilding
	 * @return building of Room
	 */
	public String getBuilding() {
		return building;
	}//getBuilding
// =========================================================================================================================================================================	
// =========================================================================================================================================================================
	/**
	 * inBuildingShort
	 * @param buildingShort a building abbreviation
	 * @return whether the Room is in the building represented by buildingShort
	 */
	public boolean inBuildingShort(String buildingShort){
		return this.building.equals(building);
	}//inBuildignShort
// =========================================================================================================================================================================
// =========================================================================================================================================================================
	/**
	 * getBuildingShort
	 * @return the short form of a Room's building
	 */
	public String getBuildingShort() {
		return buildingShort;
	}//getBuildingShort
// =========================================================================================================================================================================
// =========================================================================================================================================================================
	/**
	 * setBuilding sets this Room's building to building
	 * @param building the building this Room belongs to
	 */
	public void setBuilding(String building) {
		this.building = building;
	}//setBuilding
// =========================================================================================================================================================================
// =========================================================================================================================================================================
	/**
	 * isAccessible
	 * @return whether the room is accessible
	 */
	public boolean isAccessible() {
		return isAccessible;
	}//isAccessible
// =========================================================================================================================================================================
// =========================================================================================================================================================================
	/**
	 * setAccessible
	 * @param isAccessible the accessibility we are setting for the Room
	 */
	public void setAccessible(boolean isAccessible) {
		this.isAccessible = isAccessible;
	}//setAccessible
// =========================================================================================================================================================================
// =========================================================================================================================================================================
	/**
	 * getType
	 * @return the type of Room
	 */
	public String getType() {
		return type;
	}//getType
// =========================================================================================================================================================================
// =========================================================================================================================================================================
	/**
	 * setType
	 * @param type String representation to set the type of this Room
	 */
	public void setType(String type) {
		this.type = type;
	}//setType
// =========================================================================================================================================================================
// =========================================================================================================================================================================
	/**
	 * getCourses
	 * @return list of courses in this Room
	 */
	public ArrayList<Course> getCourses() {
		return courses;
	}
// =========================================================================================================================================================================
// =========================================================================================================================================================================
	/**
	 * addCourse
	 * @param course a course to put in this Room
	 */
	public void addCourse(Course course) {
		this.courses.add(course);
		//TODO:update3:make call to setTimeTable here
	}//addCourse
// =========================================================================================================================================================================
// =========================================================================================================================================================================
	/**
	 * toString no seriously it's a toString
	 */
	public String toString(){
		return this.building+" "+this.roomNumber;
	}//toString
// =========================================================================================================================================================================
// =========================================================================================================================================================================
	/**
	 * initRoomAvailable sets up the original blank timetable for this Room
	 * 
	 */
	public void initRoomAvailable() {
		for (int i=0;i<timesAssigned.length;i++) {
			for (int j=0;j<timesAssigned[i].length;j++) {
				timesAssigned[i][j]=0;
			}
		}
	}//initRoomAvailable
// =========================================================================================================================================================================
// =========================================================================================================================================================================
	/**
	 * scheduleRoomForTimes adds 1 to the half an hour period to which a course is assigned for this Room in timesAssigned[][]
	 * @param times a list of Times to increment for this Room
	 */
	public void scheduleRoomForTimes(ArrayList<Time> times){

		for(Time t:times){
			int dayIndex=t.getDayOfWeek();
			int startHalfHour=t.getStartHour()*2;
			if (t.getStartMinute()==30){startHalfHour++;}
			int blocks=t.getBlocks();
			for(int i=0;i<blocks;startHalfHour++){
				timesAssigned[dayIndex][startHalfHour]++;

			}
		}
	}//scheduleRoomForTimes
// =========================================================================================================================================================================
// =========================================================================================================================================================================
	/**
	 * printTimeTable prints the current timesAssigned for this Room
	 */
	public void printTimeTable() {
		int k = 0;
		for (int j =0;j<timesAssigned.length;j++) {
			k=j/2;
			if(j%2==0){
				System.out.print(k+":00 ");
			}
			else System.out.print(k+":30 ");
			for (int i=0;i<timesAssigned[i].length;i++) {
				System.out.print("	"+timesAssigned[i][j]+"	");
			}
			System.out.println(" ");
		}
	}//printTimeTable	
// =========================================================================================================================================================================
// =========================================================================================================================================================================
	/**
	 * isAssigned checks if a Time has been assigned for this Room
	 * @param t a Time to check
	 * @return the assignment status of the Time
	 */
	public boolean isAssigned(Time t) {
		boolean assigned=false;
		int day=t.getDayOfWeek();
		int startHalfHour=t.getStartHour()*2;
		if (t.getStartMinute()==30){startHalfHour++;}
		int blocks=t.getBlocks();
		for(int i=0;i<blocks;startHalfHour++){
			if (timesAssigned[day][startHalfHour]>0){
				assigned=true;
				break;
			}
		}
		return assigned;
	}//isAssigned
// =========================================================================================================================================================================
}//class Room
//==========================================================================================================================================================================

