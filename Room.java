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
		for(Tuple<Time,Time> t:times){
			Time startTime=t.getFirst();
			Time endTime=t.getSecond();
			int dayIndex=startTime.getDay()-2;
			int startHour=startTime.getMilitaryHour()*2;
			if (startTime.getMinute()==30){
				startHour++;
			}
			int endHour=endTime.getMilitaryHour()*2;
			if (endTime.getMinute()==20){
				endHour++;
			} else if (endTime.getMinute()==50){
				endHour+=2;
			}
			for(;startHour<endHour;startHour++){
				timesAssigned[dayIndex][startHour]++;
			}
		}
	}
		
	/*public void setTimeTable(ArrayList<Tuple<Time,Time>> times){
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
			hourStart = start.getMilitaryTime();
			minStart = start.getMinute();
			hourEnd = end.getMilitaryTime();
			minEnd = end.getMinute();
			i = hourStart*2;
			j = hourEnd*2;
			if (i > j) {
				System.out.println("something's wrong, check for military time: "+
						i+" "+j); //debugging
				j=2*(hourEnd+12);
			}
			if (minStart > 29) i++;
			if (minEnd > 29) j++;
			while (i < j) {
				timeTable[i][dayStart] = true;
				i++;
			}		
		}
	}*/
	
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
	
	public boolean isAssigned(Tuple<Time, Time> t) {
		boolean assigned=false;
		Time start = t.getFirst();
		Time end = t.getSecond();
		int sh=start.getMilitaryHour()*2;
		if(start.getMinute()==30)sh++;
		int eh=end.getMilitaryHour()*2;
		if(end.getMinute()==20)eh++;
		else if(end.getMinute()==50)eh+=2;
		System.out.print(start.getDay());
		int day=t.getFirst().getDay()-2;
		System.out.println(day+start.toString());
		//System.out.println("day of week is "+ t.getFirst().getEventTime().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US)+" and the time is "+ t.getFirst().toString()+"-"+t.getSecond().toString());
		for(int k=sh;k<eh;k++){
			//System.out.println("k is "+k+" and day is "+day);
			if(timesAssigned[day][k] > 0) assigned=true;
		}
		return assigned;
	}
}
