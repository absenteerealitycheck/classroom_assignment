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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

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
		
		//Merge the duplicates from manually fixing old two digit course numberings
		//fixFirstPass();
		
		
		String[][] roomsAndProfsSpreadsheet=makeSpreadsheet(new File("proto-roomsandprofslist.csv"));
		String[][] roomsAndDeptsSpreadsheet=makeSpreadsheet(new File("proto-roomsanddeptslist.csv"));
		String[][] roomsAndCoursesSpreadsheet=makeSpreadsheet(new File("proto-roomsandcourseslist.csv"));
		//secondPass(roomsAndProfsSpreadsheet, roomsAndCoursesSpreadsheet, roomsAndDeptsSpreadsheet);
		
		// Aggregate spreadsheets to form preferred rooms list for courses.
		
		String[][] courseSpreadsheet=makeSpreadsheet(new File("workingCourseList.csv"));
		
		
		

		// Make a first pass through the historical data to generate more useful spreadsheets
		String[][] courseHistSpreadsheet=makeSpreadsheet(new File("proto-coursehistory.csv"));
		//TODO:bug16 Delete two digit data
		/*ArrayList<Tuple<String,ArrayList<String>>> roomsForDepartment = associateFieldAndRooms(courseHistSpreadsheet,1);
		ArrayList<Tuple<String,ArrayList<String>>> roomsForProfessors = associateFieldAndRooms(courseHistSpreadsheet,4);
		ArrayList<Tuple<String,ArrayList<String>>> roomsForCourses = associateCourseAndRooms(courseHistSpreadsheet,1);
		
		if (!writeFirstPass(roomsForDepartment, roomsForProfessors, roomsForCourses)){
			System.out.println("Writing the first pass failed.");
			return;
		}*/
		
		/*
		 * 	String[][] roomsAndProfsSpreadsheet=makeSpreadsheet(new File("proto-roomsandprofslist.csv"));
		String[][] roomsAndDeptsSpreadsheet=makeSpreadsheet(new File("proto-roomsanddeptslist.csv"));
		String[][] roomsAndCoursesSpreadsheet=makeSpreadsheet(new File("proto-roomsandcourseslist.csv"));
		 */
		
		// Load the csv files
		String[][] roomSpreadsheet=makeSpreadsheet(new File("proto-roomslist.csv"));
		/*String[][] professorSpreadsheet=makeSpreadsheet(new File("proto-profslist.csv"));
		String[][] deptroomSpreadsheet=makeSpreadsheet(new File("proto-deptrooms.csv"));
		String[][] courseSpreadsheet=(testing)?makeSpreadsheet(new File("proto-courselist-easy.csv"))
					:makeSpreadsheet(new File("proto-courselist.csv"));
		*/
		
		// Create Hashtables for quicker lookup
		Hashtable<String,Course> courseHash=new Hashtable<String,Course>(courseSpreadsheet.length*2);
		Hashtable<String,Professor> professorHash=new Hashtable<String,Professor>(roomsAndProfsSpreadsheet.length*2);
		Hashtable<String,Time> timeHash=new Hashtable<String,Time>(100);

		// Make node objects
		
		ArrayList<Room> rooms= generateRooms(roomSpreadsheet);
		HashMap<String,ArrayList<Room>> roomsAndProfsHash=toHash(roomsAndProfsSpreadsheet,rooms);
		HashMap<String,ArrayList<Room>> roomsAndDeptsHash=toHash(roomsAndDeptsSpreadsheet,rooms);		
		HashMap<String,ArrayList<Room>> roomsAndCoursesHash=toHash(roomsAndCoursesSpreadsheet,rooms);
		//checkForUndocumentedRooms(roomsAndDeptsSpreadsheet, rooms);
		
		ArrayList<Professor> professors= generateProfessors(roomsAndProfsSpreadsheet,professorHash, rooms);
		professors.trimToSize();
		ArrayList<Course> courses= generateCourses(courseSpreadsheet,roomsAndDeptsHash,roomsAndCoursesHash,courseHash,rooms, professorHash, timeHash,roomsAndProfsHash);
		courses.trimToSize();
			
		boolean giveUp=true;
		if (giveUp){
			return;
		}
		
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
	public Object secondPass(String[][]rAPL, String[][] rACL, String[][] rADL){ //probably/def need to add parameters.
		
		return null;
	} // secondPass
	// =================================================================================================================================================================================
	
	
	
	// =================================================================================================================================================================================
	public boolean writeSecondPass(){
		//see writeFirstPass below for format
		return false;
	} // writeSecondPass
	// =================================================================================================================================================================================
	
	
	
	// =================================================================================================================================================================================
	
	
	// ===============================================================================
	public ArrayList<Tuple<String,ArrayList<String>>> associateFieldAndRooms(String[][] cHS, int fieldIndex) throws IOException{
		ArrayList<Tuple<String,ArrayList<String>>> alt = new ArrayList<Tuple<String,ArrayList<String>>>();
		BufferedWriter bw=new BufferedWriter(new FileWriter(new File("test-data.csv")));
		for (int i=1; i<cHS.length; i++){
			String field=cHS[i][fieldIndex];
			bw.write(field+"\n");
			if (fieldIndex==1){
				field = field.substring(0, 4);
			}
			
			String room=cHS[i][7];
			int makeNewTuple=-1;
			String[] splitField = field.split("  ");
			bw.write(splitField.length+"\n");
			for (String f:splitField){
				for (int j=0; j<alt.size(); j++){
					if (alt.get(j).getFirst().equals(f)){
						makeNewTuple=j+1;
						bw.write("mnt: "+makeNewTuple+" "+f+"\n");
						break;
					}
				}
				if (makeNewTuple==-1){
					bw.write("Adding: "+f+"\n");
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
				makeNewTuple=-1;
			}
			
		}
		bw.close();
		System.out.println("dun dun DONE");
		return alt;
	}
	// =================================================================================================================================================================================
	// =================================================================================================================================================================================
		public HashMap<String,ArrayList<Room>> toHash(String[][]SS, ArrayList<Room> rooms){
			HashMap<String,ArrayList<Room>> hm=new HashMap<String,ArrayList<Room>>();
			
			for(int i=0;i<SS.length;i++){
				ArrayList<Room> temprooms=new ArrayList<Room>();
				for(Room r:rooms){
					String[] ss= SS[i][1].split(", ");
					for(int j=0;j<ss.length;j++){
						if(r.toString().equals(ss[j])){
							temprooms.add(r);
							
						}
					}
					
					
				}
				hm.put(SS[i][0].toString(),temprooms);
			}
			return hm;
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
	public ArrayList<Course> generateCourses(String[][] cl, HashMap<String,ArrayList<Room>> drS,HashMap<String,ArrayList<Room>> crS,
			Hashtable<String,Course> ch, ArrayList<Room> r, Hashtable<String,Professor> pH, Hashtable<String,Time> tH,HashMap<String,ArrayList<Room>> rapH){

		System.out.println("Generating Courses");
		ArrayList<Course> courseList=new ArrayList<Course>();
		Course temp;
		for(int row=0;row<cl.length;row++){//start at 1 because first row is headings of columns
			
			/*
			 * Create all local variables
			 */
			
			String shortname=cl[row][0];
			String deptname=shortname.substring(0,4);
			String longname=cl[row][1];
			
			
			
		
			
			int capacity = (cl[row][8].isEmpty())?10:Integer.parseInt(cl[row][8]);
			String type=cl[row][4]; //must parse L/D, LAB, LEC, DIS

			temp= new Course(capacity,longname,type);
			temp.addShortName(shortname);
			
			//Check for crosslisting
			if(!cl[row][2].isEmpty()){
				String[] crossList=cl[row][2].split(",");
				for (String s: crossList) {
					if (!s.equals(shortname)){temp.addShortName(s);}
				}
				continue;
			}

			//Check professors
			String prof=(cl[row][3].isEmpty())?"Scott Kaplan":cl[row][3];
			String[] profs = prof.split("  ");
			for (String s:profs){
				Professor p = pH.get(s);
				temp.addProfessor(p);
				if (temp==null) System.out.println("TEMP");
				else if(p==null) System.out.println("PROF");
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
			String[] dayandTime=cl[row][5].split(" ");
			String day = dayandTime[0];
			String time=dayandTime[1];
			String[] times=time.split("-",2);
			String[] dow=day.split("");
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
			//End preferredTimes
			//Making preferredRooms 
			
			ArrayList<Room> dRooms=new ArrayList<Room>();
			dRooms.addAll(drS.get(deptname));
			System.out.println(shortname+"\n");
			System.out.println("dRooms size: "+ dRooms.size());
			//System.out.println(crS.containsKey(shortname.substring(0, shortname.length()-3))+" for course "+shortname);
			ArrayList<Room> cRooms=crS.get(shortname.substring(0, shortname.length()-3));
			ArrayList<Room> pRooms=new ArrayList<Room>();
			System.out.println(temp.getProfessors().size());
			for(Professor p:temp.getProfessors()){
				//System.out.println("prof rooms "+rapH.get(p.getName()));
				pRooms.addAll(rapH.get(p.getName()));
			}
			System.out.println("sizes "+pRooms.size()+" "+cRooms.size());
			ArrayList<Room> tempD2=new ArrayList<Room>();
			ArrayList<Room> tempD=new ArrayList<Room>();
			tempD.addAll(dRooms);
				for(int j=0;j<dRooms.size();j++){
					//System.out.println("conatins c "+cRooms.contains(dRooms.get(j)));
					//System.out.println("conatins p "+pRooms.contains(dRooms.get(j)));
					if(!cRooms.contains(dRooms.get(j))&&!pRooms.contains(dRooms.get(j))){
						tempD2.add(dRooms.get(j));
					}
					
					
				}
				
			
			
			System.out.println("tempD2 "+tempD2.size());
			
			for(Room r2:tempD2){
				dRooms.remove(r2);
			}
			
			if(dRooms.isEmpty()){
				System.out.println("EMPTY");
				dRooms.addAll(tempD);
			}
			temp.addPreferredRoomsList(dRooms);
			//dRooms=drS.get(deptname);
			
			//System.out.println("dRooms size again "+dRooms.size()+"\n");
			
			
			
			
			//End preferredRooms
			
		}
		courseList.trimToSize();
		System.out.println("Done courses");
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
	public ArrayList<Professor> generateProfessors(String[][] profs, Hashtable<String,Professor> pH, ArrayList<Room> rooms){//just some speculative code for now on how we should break stuff up		
		System.out.println("Generating Professors");
		ArrayList<Professor> profList=new ArrayList<Professor>();
		for(int row=0;row<profs.length;row++){
			String name=profs[row][0];
			//System.out.println(name);
			Professor p=new Professor(name);
			String[] r = profs[row][1].split(", ");
			if (r[0].isEmpty()){ continue; }
			System.out.println(p.getName());
			//TODO: might want to check this
			for (String s: r){
				System.out.println(s);
				String ss = s.substring(0,4);
				if(ss.equals("ESNH")) {ss="BEBU";}
				if(ss.equals("MUSI")) {ss="ARMU";}
				ArrayList<Room> bm = buildingMap.get(ss);
				
				for(Iterator<Room> it = bm.iterator(); it.hasNext();){
					Room ru=it.next();
					if(ru.getRoomNumber().equals(s.substring(6))){
						p.addRooms(ru);
					}
				}
								
			}
			System.out.println("Survived");
			
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
	 * @return
	 * @throws IOException
	 */
	public static String[][] makeSpreadsheet(File file) throws IOException{
		System.out.println("Reading "+file.getName());
		String[][] csv = null;
		try {
			BufferedReader b = new BufferedReader(new FileReader(file));
			String next=b.readLine(); 
			int cols=next.split(";").length+1;
			int rows=1;
			while ((next=b.readLine())!=null){
				rows++;
			}
			csv = new String[rows][cols];
		}
		catch (FileNotFoundException e) {
			System.out.println("Error making size of spreadsheet.");
			e.printStackTrace();
		}
		
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
	
	
	public void fixFirstPass() throws IOException{
		System.out.println("Fixing first pass.");
		String[][] courseRoomSpreadsheet=makeSpreadsheet(new File("proto-roomsandcourseslist.csv"));
		String[][] newCourseRoomSpreadsheet = new String[courseRoomSpreadsheet.length][courseRoomSpreadsheet[0].length];
		newCourseRoomSpreadsheet[0]=courseRoomSpreadsheet[0];
		for (int i=1; i<courseRoomSpreadsheet.length; i++){
			if (courseRoomSpreadsheet[i][0].equals(courseRoomSpreadsheet[i-1][0])){
				System.out.println("There was a duplicate!");
				String first = courseRoomSpreadsheet[i-1][1];
				String[] firstList = first .split(",");
				String[] secondList = courseRoomSpreadsheet[i][1].split(",");
				ArrayList<String> fL = new ArrayList<String>(Arrays.asList(firstList));
				for (String s:secondList){
					if (!fL.contains(s)){
						first.concat(", "+s);
					}
				}
				newCourseRoomSpreadsheet[i-1][1]=first;
				continue;
			} else{
				newCourseRoomSpreadsheet[i]=courseRoomSpreadsheet[i];	
			}
			
		}
		File f = new File("mega-roomsandcourseslist.csv");
		BufferedWriter bw=new BufferedWriter(new FileWriter(f));
		for (String[] s:newCourseRoomSpreadsheet){
			if(s[0]==null){
				continue;
			}
			bw.write(s[0]+";"+s[1]);
			bw.newLine();
		}
		bw.close();
		System.out.println("Done writing to file "+f.getName());
		System.out.println("Done fixing first pass.");
	}
	
	
	// =================================================================================================================================================================================
	public boolean writeFirstPass(ArrayList<Tuple<String,ArrayList<String>>> roomsForDepartment, ArrayList<Tuple<String,ArrayList<String>>> roomsForProfessors, ArrayList<Tuple<String,ArrayList<String>>> roomsForCourses){
		File rFC=new File("proto-roomsandcourseslist.csv");
		File rFD=new File("proto-roomsanddeptslist.csv");
		File rFP=new File("proto-roomsandprofslist.csv");
		Comparator<Tuple<String,ArrayList<String>>> c = new Comparator<Tuple<String,ArrayList<String>>>(){
			public int compare(Tuple<String,ArrayList<String>> t1, Tuple<String,ArrayList<String>> t2){
				return t1.getFirst().compareTo(t2.getFirst());
			}
		};
		Collections.sort(roomsForDepartment, c);
		Collections.sort(roomsForProfessors, c);
		Collections.sort(roomsForCourses, c);
		
		/*for (Tuple<String,ArrayList<String>> tas: roomsForDepartment){
			System.out.println(tas);
		}
		for (Tuple<String,ArrayList<String>> tas: roomsForProfessors){
			System.out.println(tas);
		}
		for (Tuple<String,ArrayList<String>> tas: roomsForCourses){
			System.out.println(tas);	
		}*/
		
		try {
			writeToCSV(roomsForDepartment,rFD);
			writeToCSV(roomsForProfessors,rFP);
			writeToCSV(roomsForCourses,rFC);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
		return true;
	} // writeFirstPass
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
		// departmentRooms= new String[600][11];
		String[][] departmentRooms=makeSpreadsheet(new File("Fall.csv"));
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
