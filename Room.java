// =====================================================================================================
// Room.java
// againessmith13, jpham14, mmillian15, cbottum15
// Fall 2013
// =====================================================================================================

import java.util.*;
import java.lang.*;
import java.io.*;

// =====================================================================================================
/**
 * An object that holds information about rooms on Amherst College campus.
 */
public class Room{
	// =================================================================================================	
	private String roomNumber; 
	private int capacity;
	private String building;
	private String buildingShort;
	private boolean isAccessible;
	//TODO:What if a Room has more than one possible type?
	private String type; //lab,seminar,lecture,small,studio
	private boolean[] tech;
	private int numberOfSlides;
	private int[][] timesAssigned = new int[5][48]; //boolean[day][half-hour]
	private HashSet<Course> courses=new HashSet<Course>();
	private HashSet<Room> rooms;
	// =================================================================================================

	/**
	 * Construct a simple Room object with the specified parameters.
	 * @param buildingShort - the short name of building
	 * @param roomNumber - the room number
	 */
	//Secondary constructor for use in generateCourses method
	public Room (String buildingShort, String roomNumber){
		this.buildingShort=buildingShort;
		this.roomNumber=roomNumber;
		this.initRoomAvailable();
	}
	// =================================================================================================

	/**
	 * Constructs a Room from the specified list of parameters. The list of parameters is expected to 
	 * have the following structure:
	 * building;number;capacity;type;accessible;buildingShort;CP;DVD;VCR;Slides;OH
	 * @param parameters - the list of parameters from which to construct the Room
	 */
	public Room (String[] parameters){
		this.building=parameters[0];
		this.roomNumber=parameters[1];
		this.capacity=Integer.parseInt(parameters[2]);
		this.type=parameters[3];
		this.isAccessible=Boolean.parseBoolean(parameters[4]);
		this.buildingShort=parameters[5];
		boolean[] tech=new boolean[5];
		int slidesNeeded=0;
		for(int i=0;i<tech.length;i++){
			tech[i]= parameters[i+6].isEmpty();
			if(i==3&&!(parameters[i+6].isEmpty())){
				slidesNeeded=Integer.parseInt(parameters[9]);
			}
		}
		this.tech=tech;
		this.numberOfSlides=slidesNeeded;
	}
	
	// =================================================================================================

	/**
	 * Initializes the Room's list of occupied times to 0, marking it as empty.
	 */
	private void initRoomAvailable() {
		for (int i=0;i<timesAssigned.length;i++) {
			for (int j=0;j<timesAssigned[i].length;j++) {
				timesAssigned[i][j]=0;
			}
		}
	}
	// =================================================================================================

	// =================================================================================================
	/**
	 * Returns true if this Room is occupied by at least one other course during the specified Time.
	 * @param t - the Time to check
	 * @return true if this Room is occupied
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
	}
	// =================================================================================================

	// =================================================================================================
	/**
	 * Marks this Room as being occupied by one more course for each Time in the specified list of Times.
	 * @param times - the list of Times
	 */
	private void scheduleRoomForTimes(ArrayList<Time> times){
		for(Time t:times){
			int dayIndex=t.getDayOfWeek();
			int startHalfHour=t.getStartHour()*2;
			if (t.getStartMinute()==30){startHalfHour++;}
			int blocks=t.getBlocks();
			for(int i=0;i<blocks;startHalfHour++){
				timesAssigned[dayIndex][startHalfHour]++;

			}
		}
	}
	// =================================================================================================

	// =================================================================================================
	/**
	 * Marks this Room as being occupied by one less course for each Time in the specified list of Times.
	 * @param times - the list of Times
	 */
	private void unscheduleRoomForTimes(ArrayList<Time> times){
		for(Time t:times){
			int dayIndex=t.getDayOfWeek();
			int startHalfHour=t.getStartHour()*2;
			if (t.getStartMinute()==30){startHalfHour++;}
			int blocks=t.getBlocks();
			for(int i=0;i<blocks;startHalfHour++){
				if (--timesAssigned[dayIndex][startHalfHour]<0){
					throw new IllegalStateException(this.toString()+" not occupied at "+t.toString());
				}
			}
		}
	}
	// =================================================================================================

	public void addRooms(Set<Room> rooms){
		this.rooms.addAll(rooms);
	}
	
	public void removeRooms(Set<Room> rooms){
		this.rooms.removeAll(rooms);
	}
	
	// =================================================================================================
	/**
	 * Ensures that this Room contains the specified Course.
	 * @param course - the Course whose presence in this Room is to be ensured
	 */
	public void addCourse(Course course) {
		this.courses.add(course);
		//TODO:update3:make call to setTimeTable here
	}
	// =================================================================================================

	// =================================================================================================
	/**
	 * Removes the specified Course from this Room, if it is present. Returns true if this Room contained 
	 * the specified Course (or equivalently, if this collection changed as a result of the call).
	 * @param course - the Course to be removed from this Room, if present
	 * @return true if an Course was removed as a result of this call
	 */
	public boolean removeCourse(Course course){
		return this.courses.remove(course);
		//TODO:Call unscheduleRoomForTimes()
	}
	// =================================================================================================

	// =================================================================================================
	/**
	 * Returns the list of Courses scheduled to be in this room.
	 * @return the list of Courses
	 */
	public HashSet<Course> getCourses() {
		return this.courses;
	}
	// =================================================================================================

	// ================================================================================================= 
	/**
	 * Returns a two-dimensional int array view of the availability of this Room. Each cell in the two-
	 * dimensional array represents the occupancy of the room for a day and time, and reflects the number 
	 * of requests being made for that day and time. The array is backed by the Room, so changes made to 
	 * the Room are reflected in the array, and vice-versa.
	 * @return a two-dimensional array view of the availability of this Room
	 */
	public int[][] getTimesAssigned() {
		return timesAssigned;
	}
	// =================================================================================================

	// =================================================================================================
	/**
	 * Returns the four-letter abbreviation of this Room's building.
	 * @return the four-letter abbreviation of this Room's building
	 */
	public String getBuildingShort() {
		return buildingShort;
	}
	// =================================================================================================

	// =================================================================================================
	/**
	 * Changes the four-letter abbreviation for this Room's building.
	 * @param buildingShort - the four-letter abbreviation for this Room's building
	 */
	public void setBuildingShort(String buildingShort) {
		this.buildingShort=buildingShort;
	}
	// =================================================================================================

	// =================================================================================================
	/**
	 * Returns the full name of this Room's building.
	 * @return the name of this Room's building
	 */
	public String getBuilding() {
		return building;
	}
	// =================================================================================================

	// =================================================================================================
	/**
	 * Changes the full name of this Room's building.
	 * @param building - the full name of this Room's building
	 */
	public void setBuilding(String building) {
		this.building = building;
	}
	// =================================================================================================

	// =================================================================================================
	/**
	 * Returns true if this Room's building or buildingShort matches the specified String, i.e. if this 
	 * Room is in the specified building.  
	 * @param building - the name of the building to test
	 * @return true if this Room is in the specified building
	 */
	public boolean inBuilding(String building){
		return this.building.equals(building) || this.buildingShort.equals(building);
	}
	// =================================================================================================

	// ================================================================================================= 
	/**
	 * Tests if the specified Room is in the same building as this Room.
	 * @param r - the Room to test against this Room
	 * @return true if the specified Room is in the same building as this Room, false otherwise
	 */
	public boolean inSameBuilding(Room r){
		return this.building==r.getBuilding();
	}
	// =================================================================================================

	// ================================================================================================= 
	/**
	 * Returns the room number for this Room.
	 * @return the room number for this Room
	 */
	public String getRoomNumber() {
		return roomNumber;
	}
	// =================================================================================================

	// ================================================================================================= 
	/**
	 * Changes the room number for this Room.
	 * @param roomNumber the value to which the room number should be changed
	 */
	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}
	// =================================================================================================

	// =================================================================================================
	/**
	 * Sets the boolean values for what technology the Room has available
	 * @param t - the array of technology booleans for this Room
	 * @param numberOfSlides - an int indicating the number of slides this Room has
	 */
	public void setTechnology(boolean[] t, int numberOfSlides){
		this.tech=t;
		this.numberOfSlides=numberOfSlides;
	}
	// =================================================================================================    

	// ================================================================================================= 
	/**
	 * Returns the array of booleans describing the Room's technological capabilities.
	 * @return the array of booleans describing the Room's technological capabilities
	 */
	public boolean[] getTechnology(){
		return this.tech;
	}
	// =================================================================================================   

	// ================================================================================================= 
	/**
	 * Returns the number of slides in this Room.
	 * @return the number of slides in this Room.
	 */
	public int getNumberOfSlides() {
		return numberOfSlides;
	}
	// =================================================================================================

	// ================================================================================================= 
	/**
	 * Returns the capacity for this Room.
	 * @return the capacity for this Room.
	 */
	public int getCapacity() {
		return capacity;
	}
	// =================================================================================================

	// =================================================================================================
	/**
	 * Changes the capacity for this Room to the specified value.
	 * @param capacity - the value to which the capacity should be changed.
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	// =================================================================================================

	// =================================================================================================
	/**
	 * Returns true if this Room is handicap accessible.
	 * @return true if this Room is handicap accessible.
	 */
	public boolean isAccessible() {
		return isAccessible;
	}
	// =================================================================================================

	// =================================================================================================
	/**
	 * Changes the value indicating if this Room is handicap accessible.
	 * @param isAccessible - the boolean indicating if this Room is handicap accessible.
	 */
	public void setAccessible(boolean isAccessible) {
		this.isAccessible = isAccessible;
	}
	// =================================================================================================

	// =================================================================================================
	/**
	 * Returns the type of this Room.
	 * @return the type of this Room
	 */
	public String getType() {
		return type;
	}
	// =================================================================================================

	// =================================================================================================
	/**
	 * Changes the type of this Room
	 * @param type - the type to which the type of this Room should be changed
	 */
	public void setType(String type) {
		this.type = type;
	}
	// =================================================================================================

	// =================================================================================================
	/**
	 * Returns a string representation of this Room. The string representation consists of the string 
	 * representation of the abbreviation for the building and the room number separated by a space. 
	 */
	public String toString(){
		return this.buildingShort+" "+this.roomNumber;
	}
	// =================================================================================================

	// =================================================================================================
	/**
	 * Writes a representation of the occupancy of this Room to the console. The representation consists 
	 * of every half-hour block of time and the number of courses scheduled to use this Room during that 
	 * time.
	 */
	private void printTimeTable() {
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
	}
	// =================================================================================================
}//class Room
// =====================================================================================================

