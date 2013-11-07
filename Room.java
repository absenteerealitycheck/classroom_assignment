import java.util.*;
import java.lang.*;
import java.io.*;
public class Room{
	
	private String roomNumber; //this cant be an int because there's a room 17A
							   /*
							    *  How about we just make it either an int that turns the letter into a hex form 
							    *  i.e 17A -> 1709 while 17 -> 17/1700 depending on preference
							    *  
							    * Or a double that uses the decimal of the letter value if there is an issue
							    *  i.e. 17A -> 17.1 since 17 will automatically go to 17.00
							    * -cbb
							    */
	//private String nickname; //should this be a thing for "Blackbox "Buckley Recital Hall" etc?
	private int capacity;
	private String building;
	private String buildingShort;
	private boolean isAccessible;
    private String type; //lab,seminar,lecture,small,studio

    //private boolean[][] timeTable = new boolean[48][5]; //boolean[day][half-hour]
    //the code will run faster when we implement time shifting if half hours are in the subarray
    private int[][] timesAssigned = new int[5][48]; //boolean[day][half-hour]

    private ArrayList<Course> courses;
    

    private class Technology{
    	//28-10: I dont think we should touch this yet 
    	private boolean isSmall;
    	private boolean hasProj;
    	private boolean hasCassette;
    	private boolean hasLaser;
    	private boolean hasCD;
    	private boolean regionFree;
    	private boolean hasVCR;
    	private boolean hasPC;
    	private boolean islaptop;
    	private boolean hasLCD;
    	private boolean hasTV;
    	private int screenSize;
    	private boolean isFlatscreen;
    	private boolean isMultiformat;
    	
    	private Technology(){}
    }

    
    public Room(boolean accessible,String building,String buildingShort,int capacity,String roomNumber,String type){
    	this(building,roomNumber);
    	this.capacity=capacity;
    	this.buildingShort=buildingShort;
    	this.isAccessible=accessible;
    	this.type=type.toLowerCase();
    }
    public Room (String building, String roomNumber){//Secondary constructor for use in generateCourses method
    	this.building=building;
    	this.roomNumber=roomNumber;
    	this.initRoomAvailable();
    }
    public boolean inSameBuilding(Room r){
    	return this.building==r.getBuilding();
    }
    
    public boolean isRoomNumber(String rN){
    	return this.roomNumber.equals("rN");
    }

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public boolean inBuilding(String building){
		return this.building.equals(building);
	}

	public String getBuilding() {
		return building;
	}
	
	public boolean inBuildingShort(String buildingShort){
		return this.building.equals(building);
	}
	
	public String getBuildingShort() {
		return buildingShort;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public boolean isAccessible() {
		return isAccessible;
	}

	public void setAccessible(boolean isAccessible) {
		this.isAccessible = isAccessible;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public ArrayList<Course> getCourses() {
		return courses;
	}
	public void addCourse(Course course) {
		this.courses.add(course);
		//TODO:update3:make call to setTimeTable here
	}
	public String toString(){
		return this.building+" "+this.roomNumber;
	}
	
	public void initRoomAvailable() {
		for (int i=0;i<timesAssigned.length;i++) {
			for (int j=0;j<timesAssigned[i].length;j++) {
				timesAssigned[i][j]=0;
			}
		}
	}
	
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
	}
	
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
	}	
	
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
}
