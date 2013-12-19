// ========================================================
// Course.java 
// againessmith13, jpham14, mmillian15, cbottum15
// Fall 2013
// ========================================================

import java.util.*;
import java.lang.reflect.Array;
import java.io.*;

// ========================================================
// Contains information for courses for room assignment 
// problem.
public class Course extends Node{
// ========================================================
	private Set<String> shortName=new TreeSet<String>(); 
	private int startTime;
	private int endTime;
	private ArrayList<Integer> dow=new ArrayList<Integer>();
	private Set<Time> timeBlocks=new TreeSet<Time>(); 
	private Set<String> preferredRooms=new HashSet<String>();
	// ====================================================
	/**
	 * Constructs a Course from the specified list of parameters. The list of parameters is expected to 
	 * have the following structure: 
	 * 		Course Number|Course Title|Crosslisted Courses|Professors|Course Type|Course Times|Course Capacity
	 * 
	 *  @param parameters - the list of parameters from which to construct the Course
	 */
	public Course(String[] parameters){
		this.shortName.add(parameters[0]);
		setName(parameters[1]);
		if (!parameters[2].isEmpty()) {
			String[] crosslisted=parameters[2].split(",");
			for(String s:crosslisted) {
				this.shortName.add(s);
			}
		}
		if (parameters[4].equals("LAB")){
			setType("lab");
		} else {
			setType("N/A");
		}
		// Parsing the time String into int is hard
		String[] dayAndTime=parameters[5].split(" ");
		// Replace day letters with int equivalent
		String[] dayStrings=dayAndTime[0].replace("SU","").replace("S","").replace("F","4").replace("TH","3").replace("W","2").replace("T","1").replace("M","0").split("");
		for(int i=1; i<dayStrings.length; i++){ // the 0 element is an empty String
			this.dow.add(Integer.parseInt(dayStrings[i]));
		}
		String[] times=dayAndTime[1].split("-");
		int start=Integer.parseInt(times[0].substring(0,times[0].length()-2).replace(":", ""));
		int end=Integer.parseInt(times[1].substring(0,times[1].length()-2).replace(":", ""));
		// turn PM times into military time
		if (!((start-1200)>=0)&&times[0].charAt(times[0].length()-2)=='P'){
			start+=1200;
		}
		if (!((end-1200)>=0)&&times[1].charAt(times[1].length()-2)=='P'){
			end+=1200;
		}
		this.startTime=start;
		this.endTime=end;
		setCapacity(Integer.parseInt(parameters[6]));//Changing Working Course List so that it has the correct number of Columns for this
	}
	// ====================================================


	// ====================================================
	/**
	 * 
	 * @param t - the Time to added to the list of Times this Course covers
	 * @return the Course 
	 */
	public Course addTime(Time t){
		this.timeBlocks.add(t);
		return this;
	}
	/**
	 * 
	 * @return a Set containing the Time objects assigned to this Course
	 */
	public Set<Time> getTimes() {return this.timeBlocks;}
	/**
	 * 
	 * @return an ArrayList of the Integer forms for the Days of the Week this Course occurs
	 */
	public ArrayList<Integer> getDow() {return this.dow;}
	/**
	 * 
	 * @return the Time at which this Course begins
	 */
	public int getStartTime() {return this.startTime;}
	/**
	 * 
	 * @return the Time at which this Course ends
	 */
	public int getEndTime() {return this.endTime;}
	/**
	 * 
	 * @param preferredRoom - the Room to be added the Courses preferredRooms
	 * @return the Course
	 */
	public Course addPreferredRoom(String preferredRoom) {
		this.preferredRooms.add(preferredRoom);
		return this;
	}
	/**
	 * 
	 * @return preferredRooms list
	 */
	public Set<String> getPreferredRooms(){return this.preferredRooms;}
	/**
	 * 
	 * @param shortName added to list of course abbreviations
	 */
	public void addShortName(String shortName) {this.shortName.add(shortName);}
	
	/**
	 * 
	 * @return course abbreviation shortName
	 */
	public Set<String> getShortName() {return this.shortName;}
	/**
	 * @return String version of the Course object which is in the format "Course Title (Course Numbers)"
	 */
	public String toString(){return this.getName()+" ("+this.shortName+")";}
	// ====================================================
} // class Course
// ========================================================
