import java.util.*;
import java.lang.*;
import java.io.*;
public class Room{
	
	private String roomNumber; //this cant be an int because there's a room 17A
	//private String nickname; //should this be a thing for "Blackbox "Buckley Recital Hall" etc?
	private int capacity;
	private String building;
	private boolean isAccessible;
    private String type; //lab,seminar,lecture,small,studio

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

    
    public Room(boolean accessible,String building,int capacity,String roomNumber,String type){
    	this.capacity=capacity;
    	this.building=building;
    	this.roomNumber=roomNumber;
    	this.isAccessible=accessible;
    	this.type=type.toLowerCase();
    }

    public boolean inSameBuilding(Room r){
    	return this.building==r.getBuilding();
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

	public String getBuilding() {
		return building;
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
}
