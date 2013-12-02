// ====================================================================================================================================================================================
// Driver
// Us.
// Fall 2013 
// ====================================================================================================================================================================================

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

// =====================================================================================================================================================================================
// The driver that loads various .csv files, parses them to construct objects, and runs a heuristic solution
// to the classroom assignment problem.
public class Driver{
// =====================================================================================================================================================================================
	
	public HashMap<String,ArrayList<Room>> buildingMap=new HashMap<String,ArrayList<Room>>(40);
	public ArrayList<Course> badCourses= new ArrayList<Course>(15);
	
	// =================================================================================================================================================================================
	// The entry point. Switch into non-static methods to start the program.
	public static void main (String args[]) throws IOException{
		new Driver().go();
		
	} // main
	// =================================================================================================================================================================================
	
	
	
	// ================================================================================================================================================================================
	// The real non-static main method
	public void go() throws IOException{
		
		boolean testing=false;
		//TODO: bug12: can we abstract this to the actual size of the uploaded file
		// Create objects for the csv files
		String[][] roomSpreadsheet = new String[81][11];
		String[][] professorSpreadsheet = new String[2][3];
		String[][] courseSpreadsheet = testing?new String[10][16]:new String[642][16];
		String[][] deptroomSpreadsheet = new String[252][2];
		String[][] courseHistSpreadsheet = new String [5765][10];
		
		
		courseHistSpreadsheet=makeSpreadsheet(new File("proto-coursehistory.csv"),courseHistSpreadsheet);
		ArrayList<Tuple<String,ArrayList<String>>> roomsForDepartment = associateFieldAndRooms(courseHistSpreadsheet,1);
		ArrayList<Tuple<String,ArrayList<String>>> roomsForProfessors = associateFieldAndRooms(courseHistSpreadsheet,4);
		ArrayList<Tuple<String,ArrayList<String>>> roomsForCourses = associateCourseAndRooms(courseHistSpreadsheet,1);
		File rFC=new File("proto-roomsandcourseslist.csv");
		File rFD=new File("proto-roomsanddeptslist.csv");
		File rFP=new File("proto-roomsandprofslist.csv");
		Collections.sort(roomsForDepartment, new Comparator<Tuple<String,ArrayList<String>>>(){
			public int compare(Tuple<String,ArrayList<String>> t1, Tuple<String,ArrayList<String>> t2){
				return t1.getFirst().compareTo(t2.getFirst());
			}
		});
		writeToCSV(roomsForDepartment,rFD);
		for (Tuple<String,ArrayList<String>> tas: roomsForDepartment){
			System.out.println(tas);
		}
		System.out.println("===============================================================================================");
		System.out.println("===============================================================================================");
		System.out.println("===============================================================================================");
		System.out.println("===============================================================================================");
		System.out.println("===============================================================================================");
		
		Collections.sort(roomsForProfessors, new Comparator<Tuple<String,ArrayList<String>>>(){
			public int compare(Tuple<String,ArrayList<String>> t1, Tuple<String,ArrayList<String>> t2){
				return t1.getFirst().compareTo(t2.getFirst());
			}
		});
		writeToCSV(roomsForProfessors,rFP);
		for (Tuple<String,ArrayList<String>> tas: roomsForProfessors){
			System.out.println(tas);
		}
		System.out.println("===============================================================================================");
		System.out.println("===============================================================================================");
		System.out.println("===============================================================================================");
		System.out.println("===============================================================================================");
		System.out.println("===============================================================================================");
		
		Collections.sort(roomsForCourses, new Comparator<Tuple<String,ArrayList<String>>>(){
			public int compare(Tuple<String,ArrayList<String>> t1, Tuple<String,ArrayList<String>> t2){
				return t1.getFirst().compareTo(t2.getFirst());
			}
		});
		writeToCSV(roomsForCourses,rFC);
		for (Tuple<String,ArrayList<String>> tas: roomsForCourses){
			System.out.println(tas);
			
		}
		boolean giveUp=true;
		if (giveUp){
			return;
		}
		
		// Create Hashtables for quicker lookup
		Hashtable<String,Course> courseHash=new Hashtable<String,Course>(courseSpreadsheet.length*2);
		Hashtable<String,Professor> professorHash=new Hashtable<String,Professor>(professorSpreadsheet.length*2);
		Hashtable<String,Time> timeHash=new Hashtable<String,Time>(100);
		
		// Load the csv files
		roomSpreadsheet=makeSpreadsheet(new File("proto-roomslist.csv"),roomSpreadsheet);
		professorSpreadsheet=makeSpreadsheet(new File("proto-profslist.csv"),professorSpreadsheet);
		deptroomSpreadsheet=makeSpreadsheet(new File("proto-deptrooms.csv"),deptroomSpreadsheet);
		courseSpreadsheet=(testing)?makeSpreadsheet(new File("proto-courselist-easy.csv"),courseSpreadsheet)
					:makeSpreadsheet(new File("proto-courselist.csv"),courseSpreadsheet);

		// Make node objects
		
		ArrayList<Room> rooms= generateRooms(roomSpreadsheet);
		checkForUndocumentedRooms(deptroomSpreadsheet, rooms);
		
		ArrayList<Professor> professors= generateProfessors(professorSpreadsheet,professorHash);
		professors.trimToSize();
		ArrayList<Course> courses= generateCourses(courseSpreadsheet,deptroomSpreadsheet,courseHash,rooms, professorHash, timeHash);
		courses.trimToSize();
			
		boolean print=false;
		for (Course c: courses) {
			int startSize=c.getPreferredRooms().size();
			c.checkCapacity();//rename to ensureCapacity
			c.checkLabs();
			if (c.getPreferredRooms().size()!=startSize){
				print=true;
			}
			
			if (print){
				System.out.println(c.toString());
	
				for (Room r : c.getPreferredRooms()){
					System.out.print("\t"+r.toString());
				}
				System.out.println();
			}
			print=false;
		}
		
		
		//ArrayList<Course> setCourses= bruteForce(courses);
		/*		
		//shuffle to get different solutions
		Collections.shuffle(courses);
		linkRoomsToCourses(courses,rooms);//this is where the magic happens
		/*make a graph of courses - courses that overlap share an edge.
		 *this method will give us clusters of courses based on time
		 *go through each cluster and color separately?
		 *JP 10/29
		 */
	} //go
	// =================================================================================================================================================================================
	// =================================================================================================================================================================================
	
	public ArrayList<Tuple<String,ArrayList<String>>> associateFieldAndRooms(String[][] cHS, int fieldIndex){
		ArrayList<Tuple<String,ArrayList<String>>> alt = new ArrayList<Tuple<String,ArrayList<String>>>();
		
		for (int i=1; i<cHS.length; i++){
			String field=cHS[i][fieldIndex];
			if (fieldIndex==1){
				field = field.substring(0, 4);
			}
			String room=cHS[i][7];
			int makeNewTuple=-1;
			String[] splitField = field.split("  ");
			
			for (String f:splitField){
				for (int j=0; j<alt.size(); j++){
					if (alt.get(j).getFirst().equals(f)){
						makeNewTuple=j+1;
						break;
					}
				}
				if (makeNewTuple==-1){
					alt.add(new Tuple<String,ArrayList<String>>(f, new ArrayList<String>()));
					makeNewTuple=alt.size();
				}
				ArrayList<String> a = alt.get(makeNewTuple-1).getSecond();
				boolean shouldAddRoom=true;
				for (String s:a){
					if (s.equals(room)){
						shouldAddRoom=false;
						break;
					}
				}
				if (shouldAddRoom){
					a.add(room);
				}
			}
		}
		return alt;
	}
	// =================================================================================================================================================================================
	
	// =================================================================================================================================================================================
	
	public ArrayList<Tuple<String,ArrayList<String>>> associateCourseAndRooms(String[][] cHS, int fieldIndex){
		ArrayList<Tuple<String,ArrayList<String>>> alt = new ArrayList<Tuple<String,ArrayList<String>>>();
		
		for (int i=1; i<cHS.length; i++){
			String field=cHS[i][fieldIndex].substring(0, cHS[i][fieldIndex].length()-3);
			
			String room=cHS[i][7];
			int makeNewTuple=-1;
			String[] splitField = field.split("  ");
			
			for (String f:splitField){
				for (int j=0; j<alt.size(); j++){
					if (alt.get(j).getFirst().equals(f)){
						makeNewTuple=j+1;
						break;
					}
				}
				if (makeNewTuple==-1){
					alt.add(new Tuple<String,ArrayList<String>>(f, new ArrayList<String>()));
					makeNewTuple=alt.size();
				}
				ArrayList<String> a = alt.get(makeNewTuple-1).getSecond();
				boolean shouldAddRoom=true;
				for (String s:a){
					if (s.equals(room)){
						shouldAddRoom=false;
						break;
					}
				}
				if (shouldAddRoom){
					a.add(room);
				}
			}
		}
		return alt;
	}
	
	// =================================================================================================================================================================================
	
	// =================================================================================================================================================================================
	
	/**
	 * 
	 * @param cl courseList
	 * @param ch courseHash
	 * @param rH roomHash
	 * @param pH professorHash
	 * @param tH timeHash
	 * @return
	 */
	public ArrayList<Course> generateCourses(String[][] cl, String[][] drS,
			Hashtable<String,Course> ch, ArrayList<Room> r, Hashtable<String,Professor> pH, Hashtable<String,Time> tH){

		System.out.println("Generating Courses");
		ArrayList<Course> courseList=new ArrayList<Course>();
		Course temp;
		for(int row=1;row<cl.length;row++){//start at 1 because first row is headings of columns
			
			/*
			 * Create all local variables
			 */
			
			String shortname=cl[row][0];
			String deptname=shortname.substring(0,4);
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
			}
			
			for (int i=0; i<drS.length; i++){
				if (deptname.equals(drS[i][0])){
					for (Room room: r){
						if (room.toString().equals(drS[i][1])){
							temp.addPreferredRoom(room);
						}
					}
				}
			}
			
			//End preferredTimes
			
			//Handling preferredRooms Begins
			//if (!cl[row][6].isEmpty()){
				//Building
				//String buildingShort=cl[row][6];
												
				//ArrayList<Room> roomsInBuilding;
				//if (rH.containsKey(buildingShort)){
				//	roomsInBuilding=rH.get(buildingShort);
				//} else {
				//	System.out.println("Throw an error for "+buildingShort+"!");
				//	continue;
				//}

				//temp.techFilterRooms(roomsInBuilding);
				
				
				//Make generic method that takes a field and removes based on it

				//Room Number
				 /*String[] roomnum=cl[row][7].split(",");
				 for (String rn : roomnum){
					 if (!rn.equals("")){
						 for (Room r:roomsInBuilding){//change to c.getRooms
							 if(!r.isRoomNumber(rn)){
								 roomsInBuilding.remove(r);
						 }
					 }
				 }*/
				 
				
				//ok to add here because we're generating rooms
				//after this will will only remove rooms from preferredRooms
				//temp.addPreferredRoomsList(roomsInBuilding);
				
			//}//Making preferredRooms Ends
			//remove rooms based on access if need be
			boolean needsAccessible=false;
			for(Professor pr: temp.getProfessors()){
				if(pr.isNeedsAccess())
					needsAccessible=true;
			}
			
			
			if(needsAccessible){ //where the fuck do we put this shit omg wtf >_<
				temp.cleanse();
			}
				
			//prefRooms are now cleansed if the professor needs accessible rooms
		}
		courseList.trimToSize();
		return courseList;
	}// generateCourses
	// =================================================================================================================================================================================
	
	
	
	// =================================================================================================================================================================================
	/**
	 * bruteForce is a solver	
	 * @param courses The list of all course objects for this semester
	 * @return The list of courses, all assigned to rooms
	 **/
	public ArrayList<Course> bruteForce(ArrayList<Course> courses){
		double counter=0;
		for(Course c:courses){
			if(c.getPreferredRooms().isEmpty()){
				badCourses.add(c);
				continue;
			}
			for(Room r:c.getPreferredRooms()){
				for(Time t: c.getPreferredTimes()){
					if(r.isAssigned(t)){//if the room is assigned at that time
						if(r.equals(c.getPreferredRooms().get(c.getPreferredRooms().size()-1))){
							c.setAssignment(r);
							r.scheduleRoomForTimes(c.getPreferredTimes());
							counter++;
						}
						else{ 
							break;
						}//if the room is assigned at that time move on to next room
					}
					else if(t.equals(c.getPreferredTimes().get(c.getPreferredTimes().size()-1))){
						c.setAssignment(r);
						r.scheduleRoomForTimes(c.getPreferredTimes());
					}
				}
			}
		}
		System.out.println("Conflicts is "+counter);
		System.out.println("Solution Quality is "+((courses.size()-counter)/courses.size()*100)+"%");
		return courses;
	} //bruteForce
	// =================================================================================================================================================================================
	
	
	
	// =================================================================================================================================================================================
	/**
	 * Create a list of room objects from the room data spreadsheet
	 * 
	 * @param rS Takes in the roomSpreadsheet that has the form
	 * [BuildingName|RoomNumber|Capacity|RoomType|Accessible|BuildingShort|CP|DVD|VCR|Slides|OH]
	 * @return A list of Room objects
	 */
	public ArrayList<Room> generateRooms(String[][] rS){
		System.out.println("Generating Rooms");
		int numberOfRooms=rS.length;
		ArrayList<Room> rooms= new ArrayList<Room>(numberOfRooms);
		for(int row=1; row<rS.length; row++){
			String type=rS[row][3];
			Boolean accessible=Boolean.valueOf(rS[row][4]);
			String buildingName=rS[row][0];
			String buildingShort=rS[row][5];
			Integer capacity= Integer.valueOf(rS[row][2]);
			String roomNumber=rS[row][1];
			boolean[]tech=new boolean[5];
			int slidesNeeded=0;
			for(int i=0;i<tech.length;i++){
				tech[i]= rS[row][i+6].isEmpty();
				if(i==3&&!(rS[row][i+6].isEmpty())){
					 slidesNeeded=Integer.parseInt(rS[row][9]);
				}
			}			
			Room r = new Room(accessible,buildingName, buildingShort, capacity,roomNumber,type);
			r.setTechnology(tech,slidesNeeded);
			rooms.add(r);
			addToBuilding(r);
		}		
		rooms.trimToSize();
		System.out.println("Done Generating Rooms");
		return rooms;
	} //generateRooms
	// =================================================================================================================================================================================
	
	
	
	// =================================================================================================================================================================================
	//TODO:Depricate this?
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
	} // generateProfessors
	// =================================================================================================================================================================================
	

	
	// =================================================================================================================================================================================
	/**
	 * Use reflection to sort the list of courses based on some field in courses in asc or desc order.
	 * @param list The list of courses to sort
	 * @param getter Name of the getter method for the field name to sort by
	 * @param order Whether to sort in asc or desc order
	 */
	public <T> void sort(ArrayList<Course> list, String getterName, final int order){//help order makes no sense. please document how it works.
		try {
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
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
						return -2;
					}
				});
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	} // sort
	// =================================================================================================================================================================================
	/**
	 * Debugging method that prints the names of rooms we need data for.
	 * 
	 * @param deptroomSpreadsheet
	 * @param rooms
	 */
	public void checkForUndocumentedRooms(String[][] deptroomSpreadsheet, ArrayList<Room> rooms){
		boolean badDeptRoomExists=false;
		for (int i=0; i<deptroomSpreadsheet.length; i++){
			if (deptroomSpreadsheet[i][1].substring(0,4).equals("ESNH")){
				deptroomSpreadsheet[i][1]="BEBU"+deptroomSpreadsheet[i][1].substring(4);
			}
			int count=0;
			for (int j=0; j<rooms.size(); j++){				
//				System.out.println(rooms.get(j).toString());
				if (deptroomSpreadsheet[i][1].equals(rooms.get(j).toString())){
//					System.out.println(rooms.get(j).toString()+" | "+deptroomSpreadsheet[i][1]);
				}
				
				if (!deptroomSpreadsheet[i][1].equals(rooms.get(j).toString())){
					count++;
				}				
			}
//			System.out.println(count+","+rooms.size());
			if (count==rooms.size()){
				System.out.println(deptroomSpreadsheet[i][1]);
				deptroomSpreadsheet[i][1]="NULL 000";
				badDeptRoomExists=true;
			}
		}
		System.out.println("There are "+((badDeptRoomExists)?"some":"no")+" bad rooms.");
	}
	
	// =================================================================================================================================================================================
	//TODO:Depricate this?
	public void addToBuilding(Room r) {
		String b=r.getBuildingShort();
		if(buildingMap.containsKey(b))
			buildingMap.get(b).add(r);
		else{
			ArrayList<Room> a=new ArrayList<Room>();
			a.add(r);
			buildingMap.put(b,a);
		}
	} //addToBuilding
	// =================================================================================================================================================================================
	

	
	// =================================================================================================================================================================================
	/**
	 * linkRoomsToCourses was an early solver
	 * @param courses The list of all course objects for this semester
	 * @param rooms The list of all room objects for this semester
	 */
	public void linkRoomsToCourses(ArrayList<Course> courses, ArrayList<Room> rooms){
		//Collections.shuffle(courses);
		/*courses.shuffle
		//OH GOD DONT TOUCH IT
		sort(courses,"getTypeCode",-1);
		sort(courses,"getCapacity",1);
		sort(courses,"getNumberOfPreferredTimes",1);
		//OK THANK YOU
		 */
	}// linkRoomsToCourses
	// =================================================================================================================================================================================
	
	
	
	// =================================================================================================================================================================================
	/**
	 * Takes a file and reads it into a spreadsheet for us to use
	 * 
	 * @param file
	 * @param strings
	 * @return
	 * @throws IOException
	 */
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
	}// makeSpreadsheet
	// =================================================================================================================================================================================
	
	
	
	// =================================================================================================================================================================================
	/**
	 * toString method for String[][]
	 * @param s
	 */
	public static void print2D(String[][] s){
		for(int row=1;row<s.length;row++){
			for(int col=0;col<s[0].length;col++){
				if(s[row][col]!=null){System.out.print(s[row][col]);}
				else continue;
			}
			System.out.print("\n");
		}
	} //print2D
	// =================================================================================================================================================================================
	
	// =================================================================================================================================================================================
	public void writeToCSV(ArrayList<Tuple<String,ArrayList<String>>> SS, File name) throws IOException{
		BufferedWriter bw=new BufferedWriter(new FileWriter(name));
		for (Tuple<String,ArrayList<String>> ts :SS){
			String ts2=ts.getSecond().toString();
			ts2=ts2.substring(1,ts2.length()-1);
			//System.out.println(ts.getFirst()+";"+ts2+"\n");
			bw.write(ts.getFirst()+";"+ts2+"\n");
		}
		bw.close();
		System.out.println("Done writing to file "+name.getName());
	}
	
	
	// =================================================================================================================================================================================

	
	// =================================================================================================================================================================================
	/**
	 * This is a data-builder and an alternate method to go. 
	 * Takes a spreadsheet of courses and their actual room assignments 
	 * for a semester and creates a spreadsheet of rooms that departments use.
	 * 
	 * @throws IOException
	 */
	public void createDeptRoomsCSV() throws IOException{
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
	}//createDeptRoomsCSV
	
	// =================================================================================================================================================================================
// =====================================================================================================================================================================================
} // class Driver
// =====================================================================================================================================================================================
