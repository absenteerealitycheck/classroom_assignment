import java.util.*;
import java.lang.*;
import java.io.*;
public class Room{
	
	private int roomNumber;
	private int capacity;
	private String building;
	private boolean isAccessible;
	private String type;

    private class Technology{
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

    
    public Room(int capacity, String building, int roomNumber, boolean accessible, String type){
    	this.capacity=capacity;
    	this.building=building;
    	this.roomNumber=roomNumber;
    	this.isAccessible=accessible;
    	this.type=type.toLowerCase();
    }

}