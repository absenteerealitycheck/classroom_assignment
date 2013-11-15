import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;

public class Driver{
	
	public HashMap<String,ArrayList<Room>> buildingMap=new HashMap<String,ArrayList<Room>>(40);
	public ArrayList<Course> badCourses= new ArrayList<Course>(15);
	public static void main (String args[]) throws IOException{
		new Driver().go(1);
		
	}
	
	public void go(int overloadingThisMethod) throws IOException{
		String[][] departmentRooms= new String[600][11];
		departmentRooms=makeSpreadsheet(new File("Fall.csv"),departmentRooms);
		ArrayList<String> r=new ArrayList<String>(30);
		String dept=departmentRooms[0][0].substring(0, 4);
		BufferedWriter bw=new BufferedWriter(new FileWriter(new File("proto-deptrooms.csv")));
		//This is really ugly code
		for(int i=0;i<departmentRooms.length;i++){
			if (!(departmentRooms[i][0]==null) && departmentRooms[i][0].substring(0, 4).equals(dept)){
				if(!r.contains(departmentRooms[i][10])){
					r.add(departmentRooms[i][10]);
				}
				continue;
			}
			for(String s:r){
				//System.out.println(dept+";"+s);
				bw.write(dept+";"+s+"\n");
			}
			
			r.clear();
			if (departmentRooms[i][0]==null){
				break;
			}
			dept=departmentRooms[i][0].substring(0, 4);
			
		}
		bw.close();
		System.out.println("Done writing");
	}
	
	public void go() throws IOException{
		boolean lexie=true;
		boolean testing=false;
		//TODO: bug12: can we abstract this to the actual size of the uploaded file
		String[][] roomSpreadsheet = new String[81][11];
		String[][] professorSpreadsheet = new String[2][3];
		String[][] courseSpreadsheet;
		if (testing){
			courseSpreadsheet = new String[10][16];
		} else {
			courseSpreadsheet = new String[642][16];
		}
		Hashtable<String,Course> courseHash=new Hashtable<String,Course>(courseSpreadsheet.length*2);
		Hashtable<String,Professor> professorHash=new Hashtable<String,Professor>(professorSpreadsheet.length*2);
		Hashtable<String,Time> timeHash=new Hashtable<String,Time>(100);
		//String[][] profSpreadsheet=new String[338][3];
		//Read csv file into spreadsheet cell by cell
		//print2D(courseSpreadsheet);
		roomSpreadsheet=makeSpreadsheet(new File("proto-roomslist.csv"),roomSpreadsheet);
		professorSpreadsheet=makeSpreadsheet(new File("proto-profslist.csv"),professorSpreadsheet);
		if (testing){
			courseSpreadsheet=makeSpreadsheet(new File("proto-courselist-easy.csv"),courseSpreadsheet);
		}else{
			courseSpreadsheet=makeSpreadsheet(new File("proto-courselist.csv"),courseSpreadsheet);
		}
		//print2D(roomSpreadsheet);
		print2D(professorSpreadsheet);
		
		//NOTE: I stored nulls because I haven't sanitized the data in any meaningful way yet
		ArrayList<Room> rooms= generateRooms(roomSpreadsheet);
		rooms.trimToSize();
		ArrayList<Professor> professors= generateProfessors(professorSpreadsheet,professorHash);
		professors.trimToSize();
		ArrayList<Course> courses= generateCourses(courseSpreadsheet,courseHash,buildingMap, professorHash, timeHash);
		courses.trimToSize();
		
		for (Course c: courses) {
			System.out.println("==="+c.getLongName()+"==="+c.getPreferredRooms().size());
			c.checkCapacity();
			c.checkLabs();
			//for (Room r : c.getPreferredRooms()){
				//System.out.println(r.getBuildingShort()+"-"+r.getRoomNumber());
			//}
		}
		
		ArrayList<Course> setCourses= bruteForce(courses);
/*

		for(Course c:courses){
			System.out.println(c.getLongName()+" Preferred: "+c.getPreferredRooms());

			if(c.getAssignment()!=null){
			System.out.println(c.getLongName()+" is in room "+ c.getAssignment().toString());} //"at "+c.getPreferredTimes().get(0));
		}*/
		
		/*for(Course c: courses){
			System.out.println(c.toString());

		//shuffle because maybe it will help
		Collections.shuffle(courses);
		
		linkRoomsToCourses(courses,rooms);//this is where the magic happens
		for(Course c: courses){
			System.out.printf("%-3s %-3s %-14s %-15s %n", c.getPreferredTimes().size(), c.getCapacity(),c.getType(), c.toString());
			
			//System.out.print(c.getPreferredTimes().size()+": "+c.getCapacity()+": "+c.getType());
			//System.out.println(c.toString()+": "+c.getCapacity()+": "+c.getPreferredTimes().size());
			for(Professor p:c.getProfessors()){
				//System.out.printf("%-20s %-20s %n", "",p.toString());
				System.out.println("\t\t\t"+p.toString());
			}
			for (Room r: c.getPreferredRooms()){
				//System.out.printf("%-20s %-20s", "",r.toString());
				System.out.print("\t\t\t"+r.toString());
			}
			System.out.println();
		}	
		
		
		System.out.println("Success!");
		
		/*
		 * after generating each room, we need to either generate all the professors or all the courses.
		 * it should be noted that the order we do these in determines greatly what constructors we need for each class
		 */

		/*make a graph of courses - courses that overlap share an edge.
		 *this method will give us clusters of courses based on time
		 *go through each cluster and color separately?
		 *JP 10/29
		 */
		
		boolean allTimesPresent = true;
		
		for (Course c: courses){
			//System.out.println("Time for "+ c.getShortName()+ " is "+c.getPreferredTimes().get(0).first.toString()+"-"+c.getPreferredTimes().get(0).second.toString());
			if (c.getPreferredTimes().size()==0) allTimesPresent=false; 
		
		}
	       
		/*Tuple<Time,Time> courseTimes; 
		Course c;
		System.out.println(courses.size());
		for(int i=0;i<courses.size();i++) {
		    c = courses.get(i);
		    System.out.println("Course: "+c.getLongName());
		    if (c.getPreferredTimes().size()==0) System.out.println("TBA");
		    else {
		    courseTimes = c.getPreferredTimes().get(0);
		    System.out.println("Start: "+courseTimes.getFirst().getHour()+":"+courseTimes.getFirst().getMinute());
		    System.out.println("End: "+courseTimes.getSecond().getHour()+":"+courseTimes.getSecond().getMinute());
		    
		    
		    }
		} *///for general debugging - jpham14:10/31
		
		System.out.println("No missing times?: "+allTimesPresent);
	

		
	} //END GO===================================================================
	
	
	public ArrayList<Course> bruteForce(ArrayList<Course> courses){
		double counter=0;
		for(Course c:courses){
			
			if(c.getPreferredRooms().isEmpty()){
				//System.out.println("in course "+c.getLongName()+" there are "+c.getPreferredRooms().isEmpty());
				badCourses.add(c);
				continue;
			}
			for(Room r:c.getPreferredRooms()){
				for(Time t: c.getPreferredTimes()){

					//System.out.println(c.toString());

					if(r.isAssigned(t)){//if the room is assigned at that time
						//System.out.println("ASSIGNED");
						if(r.equals(c.getPreferredRooms().get(c.getPreferredRooms().size()-1))){
							//System.out.println("PIGEONS");
							c.setAssignment(r);
							r.scheduleRoomForTimes(c.getPreferredTimes());
							counter++;
						}
						else{ 
							//System.out.println("elsing");
							break;
						}//if the room is assigned at that time move on to next room
					}
					else if(t.equals(c.getPreferredTimes().get(c.getPreferredTimes().size()-1))){
						//System.out.println("POSTMAN");
						c.setAssignment(r);
						r.scheduleRoomForTimes(c.getPreferredTimes());
					}
				
				}
			}
		}
		System.out.println("Conflicts is "+counter);
		System.out.println("Solution Quality is "+((courses.size()-counter)/courses.size()*100)+"%");
		return courses;
			
	}
	public void linkRoomsToCourses(ArrayList<Course> courses, ArrayList<Room> rooms){

		//Collections.shuffle(courses);
		
		/*courses.shuffle

		
		//OH GOD DONT TOUCH IT
		sort(courses,"getTypeCode",-1);
		sort(courses,"getCapacity",1);
		sort(courses,"getNumberOfPreferredTimes",1);
		
		/*
		 * 
		 * 
		 * 

		 * courses.sort(courses.getPrefRoom.length)
		 * courses.sort(courses.getType)
		 * courses.sort(courses.getCapacity)
		 * for(Course c: courses){
		 * 	for(Room r:c.getPreferredRooms){
		 * 		if(r.isNotAssigned()){
		 * 			c.setRoom(r);
		 * 			break;
		 * 		}
		 * 		
		 * 	}
		 * 	if(c.getAssignment==null){
		 * 		for(Room r:c.getPreferredRooms){
		 * 			if(r.canMove(r.getCourse()){
		 * 			r.getCourse.move;
		 * 			r.setCourse(c);
		 * 			break;}
		 * 			
		 * 		}
		 * 	}
			 * if(c.getAssignment==null){
			 * badClasses.put(c);
			 * }
		 * }
		 * 
		 * Assign classes we can't handle to badClasses
		 * }*/
	}
	
	public <T> void sort(ArrayList<Course> list, String getterName, final int order){//help order makes no sense. please document how it works.
		
		try {
			//String getterName="get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
			final Method getter=Course.class.getMethod(getterName, (Class<?>[]) null);
			if (getter == null){
				//Do Nothing
			} else{
				Collections.sort(list, new Comparator<Course>(){
					public int compare(Course c1, Course c2){
						try {
							if((Integer)getter.invoke(c1, (Object[]) null) == (Integer)getter.invoke(c2, (Object[]) null)){
								return 0;
							}
							return ((Integer) getter.invoke(c1, (Object[]) null))<((Integer) getter.invoke(c2, (Object[]) null))?(-1*order):(1*order);
							
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return -2;
					}
				});
				
				
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	private boolean canMove(Course c){
		for(Room r : c.getPreferredRooms()){
			for(Time t:c.getPreferredTimes()){
				if(!r.isAssigned(t)) return true;
			}
		}
		return false;
	}
	
	public ArrayList<Room> generateRooms(String[][] rS){
		System.out.println("Generating Rooms");
		/*However we construct rS, write a visual representation of it below
		 * 
		 * rS:
		 * [BuildingName|RoomNumber|Capacity|RoomType|Accessible|BuildingShort]
		 * [BuildingName|...]
		 * ...
		 * current columns are [buildingName|roomNumber|capacity|type|accessible] - jpham14:10/28
		 * 
		*/
		int numberOfRooms=rS.length;
		//numberOfRooms=100;
		ArrayList<Room> rooms= new ArrayList<Room>(numberOfRooms);
		for(int row=1; row<rS.length; row++){
			//String type=coalesce(rS[row][2],rS[row][3],rS[row][4]);
			String type=rS[row][3];
			Boolean accessible=Boolean.valueOf(rS[row][4]);
			String buildingName=rS[row][0];
			String buildingShort=rS[row][5];
			Integer capacity= Integer.valueOf(rS[row][2]);
			String roomNumber=rS[row][1];
			boolean[]tech=new boolean[5];
			int slidesNeeded=0;
			for(int i=0;i<tech.length;i++){
				//System.out.println("i is "+i+" row length at 0 "+ rS[0].length);
				tech[i]= rS[row][i+6].isEmpty();
				if(i==3&&!(rS[row][i+6].isEmpty())){
					 slidesNeeded=Integer.parseInt(rS[row][9]);
				}
			}
			
			
			
			Room r = new Room(accessible,buildingName, buildingShort, capacity,roomNumber,type);
			r.setTechnology(tech,slidesNeeded);
					
			rooms.add(r);
			//Changed the buildingNames in the csv to the shortNames as that seems to be how the course scheduler stores them so there is no reason to have a building long name.
			addToBuilding(r);
		}		
		rooms.trimToSize();
		System.out.println("WOOT WOOT");
		return rooms;
	}
	
	public void addToBuilding(Room r) {
		String b=r.getBuildingShort();
		if(buildingMap.containsKey(b))
			buildingMap.get(b).add(r);
		else{
			ArrayList<Room> a=new ArrayList<Room>();
			a.add(r);
			buildingMap.put(b,a);
		}
	}

	public ArrayList<Professor> generateProfessors(String[][] profs, Hashtable<String,Professor> pH){//just some speculative code for now on how we should break stuff up		
		System.out.println("Generating Professors");
		ArrayList<Professor> profList=new ArrayList<Professor>();
		for(int row=1;row<profs.length;row++){
			String name=profs[row][0];
			//System.out.println(name);
			Professor p=new Professor(name);
			//Don't forget that we're setting the size of profList by hand up in go()
			if (pH.containsKey(name)){//we should "probably" write our own exception. 
				try {
					throw new AlreadyExistingException("This professor name already exists! What are you doing?");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			profList.add(p);
			pH.put(name,p);
		}
		System.out.println("Help");
		profList.trimToSize();
		return profList;
	}
	
	     

	public ArrayList<Course> generateCourses(String[][] cl, Hashtable<String,Course> ch, HashMap<String,ArrayList<Room>> rH, Hashtable<String,Professor> pH, Hashtable<String,Time> tH){
		/*
		 * [Shortname|Longname|Professor|Capacity|Day|Time|Building|RoomNumber|Type|CP|DVD|VCR|Slides|OH|Concurrent|Noncurrent]
		 */
	
		System.out.println("Generating Courses");
		ArrayList<Course> courseList=new ArrayList<Course>();
		Course temp;
		for(int row=1;row<cl.length;row++){//start at 1 because first row is headings of columns
			
			/*
			 * Create all local variables
			 */
			
			String shortname=cl[row][0];
			String longname=cl[row][1];
			//Check for crosslisting
			if(ch.containsKey(longname)){
				ch.get(longname).addShortName(shortname);
				continue;
			}
			
			int capacity = (cl[row][3].isEmpty())?10:Integer.parseInt(cl[row][3]);
			String type=cl[row][8];

			temp= new Course(capacity,longname,type);
			temp.addShortName(shortname);

			String prof=(cl[row][2].isEmpty())?"Scott Kaplan":cl[row][2];
			Professor p = pH.get(prof);
			temp.addProfessor(p);
			p.addCourse(temp);
			
			courseList.add(temp);
			ch.put(longname, temp);
			boolean[]tech=new boolean[5];
			int slidesNeeded=0;
			for(int i=0;i<tech.length;i++){
				tech[i]=cl[row][i+9].isEmpty();
				if(i==3&&!(cl[row][i+9].isEmpty())){
					 slidesNeeded=Integer.parseInt(cl[row][12]);
				}
			}
			temp.setTech(tech);
			temp.setNumberOfSlides(slidesNeeded);
			
			//Making preferred Times Begins
			String daysOfWeek=cl[row][4];
			String time=cl[row][5];
			String[] times=time.split("-",2);
			String[] dow=daysOfWeek.split("");
			Time t;
			
			if(!times[0].isEmpty()){
				for(int i=0;i<dow.length;i++){
					//System.out.println(daysOfWeek);
					if (i<dow.length-1&&dow[i+1].equals("H")){i++;}
					if (tH.containsKey(dow[i]+times[0]+times[1])){
						t=tH.get(dow[i]+times[0]+times[1]);
					} else {						
						t=new Time(dow[i],
									times[0].substring(0,times[0].length()-2),times[0].substring(times[0].length()-2),
									times[1].substring(0,times[1].length()-2),times[1].substring(times[1].length()-2));
						tH.put(dow[i]+times[0]+times[1],t);
					}
					temp.addPreferredTime(t);
				}
			}//End preferredTimes
			
			//Handling preferredRooms Begins
			if (!cl[row][6].isEmpty()){
				//Building
				String buildingShort=cl[row][6];
												
				ArrayList<Room> roomsInBuilding;
				if (rH.containsKey(buildingShort)){
					roomsInBuilding=rH.get(buildingShort);
				} else {
					System.out.println("Throw an error for "+buildingShort+"!");
					continue;
				}
				//System.out.println("for course: "+temp.getShortName()+" "+roomsInBuilding.toString() +" before");
				temp.techFilterRooms(roomsInBuilding);
				//System.out.println("for course: "+temp.getShortName()+" "+roomsInBuilding.toString() +" after");
				//Room Number
				
				/* TODO:bug13: modify this code so roomnum can be a comma seperated list of rooms
				 * 
				 * 
				 String roomnum=cl[row][7];
					 if (!roomnum.equals("")){
						 for (Room r:roomsInBuilding){
							 if(!r.isRoomNumber(roomnum){
							 roomsInBuilding.remove(r);
						 }
					 }
				 }
				 *
				 * 
				*/
				
				//ok to add here because we're generating rooms
				//after this will will only remove rooms from preferredRooms
				temp.addPreferredRoomsList(roomsInBuilding);
				
			}//Making preferredRooms Ends
			//remove rooms based on access if need be
			boolean work=false;
			for(Professor pr: temp.getProfessors()){
				if(pr.isNeedsAccess())
					work=true;
			}
			if(work)
				temp.cleanse();
			//prefRooms are now cleansed if the professor needs accessible rooms
		}
		courseList.trimToSize();
		return courseList;
	}
	
	public <T> T coalesce(T a, T b, T c) {
	    return a != null ? a : (b != null ? b : c);
	}

	public static String[][] makeSpreadsheet(File file, String[][] strings) throws IOException{
		System.out.println("Reading Files");
		String[][] csv=strings;
		try {
			BufferedReader readIn = new BufferedReader(new FileReader(file));
			String line="";
			String[] row=new String[csv[0].length];
			int count=0;
			while((line=readIn.readLine())!=null){
				String [] temp= line.split(";",row.length);
				int n = Math.min(temp.length, row.length);
				for(int i=0;i<n;i++){
					//System.out.println("count is "+count+" i is "+i+" temp. length "+temp.length+ "  th"+csv.length);
					if(count<csv.length){
					csv[count][i]=temp[i];
					}
					else break;
				}
				count++;
				
			}
			readIn.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("You Done Goofed");
			e.printStackTrace();
		}
		return csv;
	}
	public static void print2D(String[][] s){
		for(int row=1;row<s.length;row++){
			for(int col=0;col<s[0].length;col++){
				if(s[row][col]!=null){System.out.print(s[row][col]);}
				else continue;
			}
			System.out.print("\n");
		}
	}
}
