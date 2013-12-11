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
	
	private String longName;
	private HashSet<String> shortName=new HashSet<String>(); 
	private HashSet<String> departments=new HashSet<String>();
	private String timeString; 
	private int startTime;
	private int endTime;
	private ArrayList<Integer> dow=new ArrayList<Integer>();
	private TreeSet<Time> timeBlocks=new TreeSet<Time>(); 
	private int numOfBlocks;
	private HashSet<String> preferredRooms=new HashSet<String>(); //actually inverse(PreferredRooms) ie the Room edges
	private HashSet<String> professors=new HashSet<String>(); 
	private int capacity;
	private String type;
	private Room assignment;
	private HashSet<Room> roomEdges=new HashSet<Room>(); //only Room edges
	private HashSet<Course> courseEdges=new HashSet<Course>(); //only Course edges
	private boolean[] tech;//list of tech booleans
	//0=needsProjecter
	//1=needsDVD
	//2=needsVCR
	//3=needsSlides
	//4=needsOverhead
	private int numberOfSlides;

	// ====================================================
	// Constructor
	/**
	 * 
	 * @param capacity - course capacity
	 * @param name - full course title
	 * @param type - course type (lab, seminar, discussion, lecture)
	 */
	public Course(String[] parameters){
		this.shortName.add(parameters[0]);
		this.longName=parameters[1];
		this.timeString=parameters[2];
		if (!parameters[2].isEmpty()) {
			String[] crosslisted=parameters[2].split(",");
			for(String s:crosslisted) {
				this.shortName.add(s);
			}
		}
		String[] profs=parameters[3].split("  ");
		for(String s:profs){
			this.professors.add(s);
		}
	
		if (parameters[4].equals("LAB")){
			this.type="lab";
		} else {
			this.type="N/A";
		}
		//L/D, DIS, LAB, LEC?
		
		 //TODO:Do Not Use MakeTimes::: add method to build an ArrayList of Times for edge making.
		
		String[] dayandTime=parameters[5].split(" ");
		//String[] dow=dayandTime[0].split("");
		String[] dayStrings=dayandTime[0].replace("SU","").replace("S","").replace("F","4").replace("TH","3").replace("W","2").replace("T","1").replace("M","0").split("");
		for(int i=1; i<dayStrings.length; i++){
			this.dow.add(Integer.parseInt(dayStrings[i]));
		}
		
		String[] times=dayandTime[1].split("-");
		int start=0;
		int end=0;
		start=Integer.parseInt(times[0].substring(0,times[0].length()-2).replace(":", ""));
		end=Integer.parseInt(times[1].substring(0,times[1].length()-2).replace(":", ""));
		if (!((start-1200)>=0)&&times[0].charAt(times[0].length()-2)=='P'){
			start+=1200;
		}
		if (!((end-1200)>=0)&&times[1].charAt(times[1].length()-2)=='P'){
			end+=1200;
		}
		this.startTime=start;
		this.endTime=end;
		this.capacity=Integer.parseInt(parameters[8]);
		
	}
	// ====================================================

	public void makeTimes(String s) {
		
		String[] dayandTime=s.split(" ");
		String[] dow=dayandTime[0].split("");

		
		ArrayList<Integer> dayOf=new ArrayList<Integer>(dow.length) ;
		
		for (int i=0;i<dow.length;i++){
			if(dow[i].equals("M")){
				dayOf.add(0);
			}
			else if(dow[i].equals("T")){
				if ((i+1)!=dow.length) {
					if (dow[i+1].equals("H")){
						dayOf.add(3);
					}
					else{
						dayOf.add(1);
					}
				}
				else{
					dayOf.add(1);
				}
			}
			else if(dow[i].equals("W")){
				dayOf.add(2);
			}
			else if(dow[i].equals("F")){
				dayOf.add( 4);
			}
			else if(dow[i].equals("H")){
				continue;
			}
		}//end dayOfWeek
		
		String[] times=dayandTime[1].split("-");
		
		int startHr = Integer.parseInt(times[0].substring(0,2))*100;
		int startMin = Integer.parseInt(times[0].substring(3,times[0].length()-2));
		int endHr = Integer.parseInt(times[1].substring(0,2))*100;
		int endMin = Integer.parseInt(times[1].substring(3,times[1].length()-2));
		
		int numOfBlocks=(endHr-startHr)/50;
		
		if (endMin==20) {
			startMin=30;
			numOfBlocks++;
		}
		else if (endMin==30) {
			numOfBlocks++;
		}
		else if (endMin==50) {
			endHr +=100;
			endMin=0;
			numOfBlocks+=2;
		}
		
		int start = startHr+startMin;
		int end = endHr+endMin;
		
		
		if (startHr!=1200&&times[0].substring(times[0].length()-2).equals("PM")) {
			start+=1200;
		}
		if (endHr!=1200&&times[1].substring(times[1].length()-2).equals("PM")) {
			end+=1200;
		}
				
		//TODO: get String keys, get Times from Time data in FileManager - associate Time and Course
		//key will look like "0: 1430"
		//We'll be handling this elsewhere/when
		
		this.numOfBlocks=numOfBlocks;
		this.startTime=start;
		this.endTime=end;
		
		
	}

	// ====================================================
	// Getters and Setters
	
	public TreeSet<Time> getTimes() {
		return this.timeBlocks;
	}
	
	public void setTime(TreeSet<Time> t) {
		this.timeBlocks = t;
	}
	
	public Course addTime(Time t){
		this.timeBlocks.add(t);
		return this;
	}
	
	public ArrayList<Integer> getDow() {
		return dow;
	}

	public void setDow(Integer dow) {
		this.dow.add(dow);
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	/**
	 * 
	 * @return all technology requirements
	 */
	public boolean[] getTech() {
		return tech;
	}

	/**
	 * 
	 * @param tech initialized
	 */
	public void setTech(boolean[] tech) {
		this.tech = tech;
	}

	/**
	 * 
	 * @return number of slideshow requirements
	 */
	public int getNumberOfSlides() {
		return numberOfSlides;
	}

	/**
	 * 
	 * @param numberOfSlides initialized
	 */
	public void setNumberOfSlides(int numberOfSlides) {
		this.numberOfSlides = numberOfSlides;
	}

	/**
	 * 
	 * @return preferredRooms list
	 */
	public HashSet<String> getPreferredRooms() {
		return this.preferredRooms;
	}
	
	/**
	 * 
	 * @return final room assignment
	 */
	public Room getAssignment() {
		return assignment;
	}

	/**
	 * 
	 * @param assignment set to given room assignment
	 */
	public void setAssignment(Room assignment) {
		this.assignment = assignment;
	}
	
	/**
	 * 
	 * @return course capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * 
	 * @param capacity initialized
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	

	public String getType(){
		return this.type;
	}

	// numeric conversion for course type for constant 
	// search/comparison


	// course title/name
	public String getLongName() {
		return longName;
	}

	/**
	 * 
	 * @param longName sets courses full title 
	 */
	public void setLongName(String longName) {
		this.longName = longName;
	}

	/**
	 * 
	 * @return course abbreviation shortName
	 */
	public HashSet<String> getShortName() {
		return shortName;
	}

	/**
	 * 
	 * @param shortName added to list of course abbreviations
	 */
	public void addShortName(String shortName) {
		this.shortName.add(shortName);
	}

	/**
	 * 
	 * @return associated departments list
	 */
	public HashSet<String> getDepartment() {
		return departments;
	}

	/**
	 * 
	 * @param department added to associated departments list
	 */
	public void addDepartment(String department) {
		this.departments.add(department);
	}

	/**
	 * 
	 * @return list of professors teaching this course
	 */
	public HashSet<String> getProfessors() {
		return professors;
	}

	/**
	 * 
	 * @param professor added to list of professors 
	 * teaching this course
	 */
	public void addProfessor(String professor) {
		this.professors.add(professor);
	}

	// ====================================================
	
	public Course addEdge(Node n){
		if (n instanceof Course){
			this.courseEdges.add((Course)n);
		} 
		else if (n instanceof Room){
			this.roomEdges.add((Room)n);
		}
		return this;
	}
	
	public int getNeighborCount(){
		return this.courseEdges.size()+this.roomEdges.size();
	}
	
	public Set<Node> getNeighbors(){
		Set<Node> tmp= new HashSet<Node>(this.courseEdges);
		tmp.addAll(this.roomEdges);
		return tmp;
		
	}
	
	public boolean isNeighbor(Node n){
		return getNeighbors().contains(n);
	}
	
	// ====================================================
	// techFilterRooms
	// Eliminates rooms from preferredRooms is 
	// not compatible based on technology requirements.
	/**
	 * 
	 * @param pR - preferred rooms placeholder
	 * @return list of rooms compatible for technology requirements
	 */
	public ArrayList<Room> techFilterRooms(ArrayList<Room> pR){//checks the room's Tech against the courses Tech needs
		
		// for each room in preferredRooms
		for(Iterator<Room> it = pR.iterator(); it.hasNext();){
			Room r=it.next();
			if(allTrue(this.getTech())&&(!compareArrays(this.getTech(), r.getTechnology()))){
				it.remove();
			}
		}
		
		if(!pR.isEmpty()){
			System.out.println("There are no rooms in that buiding that work");
		}
		return pR;
	}
	
	// ====================================================
	// general boolean array comparison
	// everything true in a1 must be true in a2
	// used by techFilterRooms
	
	public boolean compareArrays(boolean[] a1, boolean[] a2){
		for (int j = 0; j<a1.length; j++) {
		
		   if (a1[j] && !a2[j]) {
		      return false;
		   }
		}
		return true;
	}
	// ====================================================
	
	
	// ====================================================
	// general array checking?
	// checks whether or not every boolean is true
	// used by techFilterRooms

	public  boolean allTrue(boolean[] array) {
	    for(boolean b : array) if(b) return true;
	    return false;
	}
	// ====================================================
	
	
	
	// ====================================================
	// add individual room to preferredRooms
	public Course addPreferredRoom(String preferredRoom) {
		this.preferredRooms.add(preferredRoom);
		return this;
	}

	// add a list of rooms to preferredRooms
	public Course addPreferredRoomsList(ArrayList<String> pR){
		for (String r:pR){
			addPreferredRoom(r);
		}
		return this;
	}
	// ====================================================
	
	
	
	// ====================================================
	// print course name, full course title with abbrev.
	public String toString(){
		return this.longName+" ("+this.shortName+")";
	}
	// ====================================================
	
	
	// ====================================================
	// checks rooms in preferredRooms list for compatibility
	// based on accessibility of course to room 
	
	// ====================================================

	
	// ====================================================
	// checks rooms in preferredRooms for compatibility 
	// based on course capacity to room capacity
	public void checkCapacity() { //split into separate methods?
		Room r;
		for (Iterator i = preferredRooms.iterator();i.hasNext();){
			r=(Room) i.next();
			// if room capacity is too small, remove room 
			// from preferredRooms
			if (r.getCapacity() < this.capacity) {
				i.remove();
			}
		}
	}
	// ====================================================
	
	
	// ====================================================
	// check rooms in preferredRooms for compatibility 
	// based on course type = lab, under the premise that
	// labs must be in labs (hard constraint), but other
	// course types are a soft constraint (a seminar can
	// be put in a lecture hall, for example) - 11/18
	
	public void checkLabs() {
		Room r;
		// if this course is a lab, check its preferredRooms
		if (this.getType().toLowerCase().equals("lab")){
			for (Iterator i = preferredRooms.iterator();i.hasNext();){
				r=(Room) i.next();
				// if room in preferred list is a "lab"
				// remove it
				if(!r.getType().toLowerCase().equals("lab")) {
					i.remove();
				}
			}
		}
	}
	// ====================================================
} // class Course
// ========================================================
