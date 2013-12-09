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
public class Course{
// ========================================================
	
	private String longName;
	private ArrayList<String> shortName; 
	private ArrayList<String> departments;
	private String timeString; 
	private ArrayList<Time> times; 
	private ArrayList<Room> preferredRooms;
	private ArrayList<String> professors; 
	private int capacity;
	private boolean discussionCourse;
	private boolean labCourse;
	private boolean lectureCourse;
	private boolean seminarCourse;
	private Room assignment;
	private ArrayList<Course> edges;
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
	
		//L/D, DIS, LAB, LEC?
		
		this.times=makeTimes(parameters[5]); //TODO: BUILD LATER
		
		this.capacity=Integer.parseInt(parameters[8]);
		
	}
	// ====================================================

	public ArrayList<Time> makeTimes(String s) {
		ArrayList<Time> setTimes = null;
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
		
		String startHour = times[0].substring(0,2);
		String endHour = times[1].substring(0,2);
		
		if(times[0].substring(times[0].length()-3,times[0].length()).equals("PM")) startHour+=12;
		if(times[1].substring(times[1].length()-3,times[1].length()).equals("PM")) endHour+=12;
				
		//TODO: get String keys, get Times from Time data in FileManager - associate Time and Course
		//key will look like "0: 1430"
		return setTimes;
	}

	//PULL FROM THIS METHOD TO CREATE NEW METHODS TO CONSTRUCT COURSE
	public ArrayList<Course> generateCourses(String[][] cl, HashMap<String,ArrayList<Room>> drS,HashMap<String,ArrayList<Room>> crS,
			Hashtable<String,Course> ch, ArrayList<Room> r, Hashtable<String,Professor> pH, Hashtable<String,Time> tH,HashMap<String,ArrayList<Room>> rapH) throws IOException{
	
		System.out.println("Generating Courses");
		ArrayList<Course> courseList=new ArrayList<Course>();
		Course temp;

		for(int row=0;row<cl.length;row++){

			/*
			 * Create all local variables
			 */

			String shortname=cl[row][0];
			if(row+1<cl.length){//ignore repeated sections
				if (cl[row+1][0].equals(shortname)) {
					System.out.println("SKIPPING REPEAT "+shortname);
					continue;
				}
			}
			String deptname=shortname.substring(0,4);
			String longname=cl[row][1];
			int capacity = (cl[row][8].isEmpty())?10:Integer.parseInt(cl[row][8]);
			String type=cl[row][4]; //must parse L/D, LAB, LEC, DIS
	
			temp= new Course(capacity,longname,type);
			temp.addShortName(shortname);
			//Check for crosslisting
			if(!cl[row][2].isEmpty()){
				System.out.println("CROSSLISTED: "+cl[row][2]);
				String[] crossList=cl[row][2].split(",");
				for (String s: crossList) { //add each cross-listed shortname
					if (!temp.getShortName().contains(s)) temp.addShortName(s);
				}
				//a course of this name already exists, do not make a duplicate Object
				//TODO: FIX FOR CROSSLISTS - CHECK MAP FOR LONG(?) KEY; IF EXISTS, CONTINUE
			/*	if (!!!) {
					System.out.println("ALREADY CONTAINS "+temp.toString());
					continue;
				}*/
			}
	
			//Check professors
			String prof=(cl[row][3].isEmpty())?"Scott Kaplan":cl[row][3];
			String[] profs = prof.split("  ");
			for (String s:profs){
				Professor p = pH.get(s);
				temp.addProfessor(p);
				p.addCourse(temp);
			}

			courseList.add(temp);
			ch.put(longname, temp);
			// TODO: handle tech please - must find out where getting info from
			/*boolean[]tech=new boolean[5];
			int slidesNeeded=0;
			for(int i=0;i<tech.length;i++){
				tech[i]=cl[row][i+9].isEmpty();
				if(i==3&&!(cl[row][i+9].isEmpty())){
					 slidesNeeded=Integer.parseInt(cl[row][12]);
				}
			}
			temp.setTech(tech);
			temp.setNumberOfSlides(slidesNeeded);
			 */

			//Making preferred Times Begins
			
			//Making preferredRooms 
			ArrayList<Room> dRooms=new ArrayList<Room>();
			dRooms.addAll(drS.get(deptname));
			//System.out.println(shortname+"\n");
			//System.out.println("dRooms size: "+ dRooms.size());
			//System.out.println(crS.containsKey(shortname.substring(0, shortname.length()-3))+" for course "+shortname);
			ArrayList<Room> cRooms=crS.get(shortname.substring(0, shortname.length()-3));
			ArrayList<Room> pRooms=new ArrayList<Room>();
			//System.out.println(temp.getProfessors().size());
			for(Professor p:temp.getProfessors()){
				//System.out.println("prof rooms "+rapH.get(p.getName()));
				pRooms.addAll(rapH.get(p.getName()));
			}
			//System.out.println("sizes "+pRooms.size()+" "+cRooms.size());
			ArrayList<Room> tempD2=new ArrayList<Room>();
			ArrayList<Room> tempD=new ArrayList<Room>();
			tempD.addAll(dRooms);
				for(int j=0;j<dRooms.size();j++){
					//if room in dRooms is not in Courses rooms or Professors rooms, add to a deletion list
					if(!cRooms.contains(dRooms.get(j))&&!pRooms.contains(dRooms.get(j))){
						tempD2.add(dRooms.get(j));
					}
				}
			//System.out.println("tempD2 "+tempD2.size());
			//delete rooms from department rooms that are not suitable for the course
			for(Room r2:tempD2){
				dRooms.remove(r2);
			}
			//if the list of preferred rooms for a course is empty, give it the department room list
			if(dRooms.isEmpty()){
				dRooms.addAll(tempD);
			}
			temp.addPreferredRoomsList(dRooms);
			//End preferredRooms
		}
		courseList.trimToSize();
	
		System.out.println("Done courses");
		return courseList;
	}// generateCourses

	// ====================================================
	// Getters and Setters
	
	public Time getTime() {
		return time;
	}


	public void setTime(Time time) {
		this.time = time;
	}
	
	public ArrayList<Time> getTimes() {
		return times;
	}
	
	public void setTime(ArrayList<Time> t) {
		this.times = t;
	}
	
	public void addTime(Time t){
		this.times.add(t);
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
	public ArrayList<Room> getPreferredRooms() {
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
	
	
	/**
	 * 
	 * @return course type (lab, lecture, discussion, seminar)
	 */
	public boolean isLabCourse() {
		return labCourse;
	}

	/**
	 * 
	 * @param labCourse determines whether course is lab 
	 */
	public void setLabCourse(boolean labCourse) {
		this.labCourse = labCourse;
	}

	/**
	 * 
	 * @return true if course is seminar
	 */
	public boolean isSeminarCourse() {
		return seminarCourse;
	}

	/**
	 * 
	 * @param seminarCourse determines whether course is seminar
	 */
	public void setSeminarCourse(boolean seminarCourse) {
		this.seminarCourse = seminarCourse;
	}

	/**
	 * 
	 * @return true if course is lecture
	 */
	public boolean isLectureCourse() {
		return lectureCourse;
	}

	/**
	 * 
	 * @param lectureCourse determines whether course is lecture
	 */
	public void setLectureCourse(boolean lectureCourse) {
		this.lectureCourse = lectureCourse;
	}

	/**
	 * 
	 * @return true if course is discussion
	 */
	public boolean isDiscussionCourse() {
		return discussionCourse;
	}

	/**
	 * 
	 * @param discussionCourse determines whether course is discussion
	 */
	public void setDiscussionCourse(boolean discussionCourse) {
		this.discussionCourse = discussionCourse;
	}
	
	/**
	 * 
	 * @return courses type (discussion, lab, lecture, seminar) or Unlisted
	 */
	public String getType(){
		if (isDiscussionCourse()){return "Discussion";}
		if (isLabCourse()){return "Lab";}
		if (isLectureCourse()){return "Lecture";}
		if (isSeminarCourse()){return "Seminar";}
		return "Unlisted";
	}

	// numeric conversion for course type for constant 
	// search/comparison
	public int getTypeCode(){
		if (isDiscussionCourse()){return -4;}
		if (isLabCourse()){return -1;}
		if (isLectureCourse()){return -2;}
		if (isSeminarCourse()){return -3;}
		return -5;
	}

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
	public ArrayList<String> getShortName() {
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
	public ArrayList<String> getDepartment() {
		return department;
	}

	/**
	 * 
	 * @param department added to associated departments list
	 */
	public void addDepartment(String department) {
		this.department.add(department);
	}

	/**
	 * 
	 * @return list of professors teaching this course
	 */
	public ArrayList<Professor> getProfessors() {
		return professors;
	}

	/**
	 * 
	 * @param professor added to list of professors 
	 * teaching this course
	 */
	public void addProfessor(Professor professor) {
		this.professors.add(professor);
	}

	// ====================================================
	
	
	
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
	public void addPreferredRoom(Room preferredRoom) {
		this.preferredRooms.add(preferredRoom);
	}

	// add a list of rooms to preferredRooms
	public void addPreferredRoomsList(ArrayList<Room> pR){
		for (Room r:pR){
			addPreferredRoom(r);
		}
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
	public void cleanse(){
		for(Iterator<Room> it=this.getPreferredRooms().iterator();it.hasNext();){
			Room r=it.next();
			if(!r.isAccessible()){
				it.remove();			
			}
		}
	}
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
