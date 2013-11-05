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
    private boolean[][] timeTable = new boolean[5][48]; //boolean[day][half-hour]

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
    	this.capacity=capacity;
    	this.building=building;
    	this.buildingShort=buildingShort;
    	this.roomNumber=roomNumber;
    	this.isAccessible=accessible;
    	this.type=type.toLowerCase();
    	this.startTimeTable();
    }
    public Room (String building, String roomNumber){//Secondary constructor for use in generateCourses method
    	this.building=building;
    	this.roomNumber=roomNumber;
    	this.startTimeTable();
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
	
	public String toString(){
		return this.building+this.roomNumber;
	}
	
	public void startTimeTable() {
		for (int i=0;i<5;i++) {
			for (int j=0;j<48;j++) {
				timeTable[i][j]=false;
			}
		}
	}
		
	public void setTimeTable(ArrayList<Tuple<Time,Time>> times){
		int dayStart;
		int hourStart;
		int minStart;
		int hourEnd;
		int minEnd;
		Time start;
		Time end;
		int i; //while start
		int j; //while end
		for (Tuple t: times) {
			start = (Time) t.getFirst();
			end = (Time) t.getSecond();
			dayStart = start.getDay()-2;
			hourStart = start.getHour();
			minStart = start.getMinute();
			hourEnd = end.getHour();
			minEnd = end.getMinute();
			i = hourStart*2;
			j = hourEnd*2;
			if (minStart > 30) i++;
			if (minEnd > 30) j++;
			if (i > j) System.out.println("something's wrong, check for military time"); //debugging
			j=27;
			System.out.println("i: "+i+" j: "+j);
			while (i < j) {
				timeTable[dayStart][i] = true;
				i++;
			}
					
		}
		System.out.println("You made it this far.");
		
	}	
	
	public void printTimeTable() {
		for (int j =0;j<48;j++) {
			for (int i=0;i<5;i++) {
				System.out.print(timeTable[i][j]+" ");
			}
			System.out.println(" ");
		}
	}	
	
	public boolean isNotAssigned(Tuple<Time, Time> t) {
		Time start = t.getFirst();
		Time end = t.getSecond();
		int sh=start.getHour()*2;
		if(start.getMinute()==30)sh++;
		int eh=end.getHour()*2;
		if(end.getMinute()==20)eh++;
		else if(end.getMinute()==50)eh+=2;
		int day=t.getFirst().getEventTime().DAY_OF_WEEK-2;
		for(int k=sh;k<eh;k++){
			if(timeTable[k][day]) return false;
		}
		
		return true;
	}
}
