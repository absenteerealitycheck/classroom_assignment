import java.rmi.AlreadyBoundException;
import java.util.*;
import java.lang.*;
import java.io.*;
public class Driver{
	
	public HashMap<String,ArrayList<Room>> buildingMap=new HashMap<String,ArrayList<Room>>(40);
	public ArrayList<Course> badCourses;
	public static void main (String args[]) throws IOException{
		new Driver().go();
		
	}
	
	public void go() throws IOException{
		//TODO: bug12: can we abstract this to the actual size of the uploaded file
		String[][] roomSpreadsheet = new String[81][6];
		String[][] professorSpreadsheet = new String[2][3];
		String[][] courseSpreadsheet = new String[642][16];
		Hashtable<String,Course> courseHash=new Hashtable<String,Course>(courseSpreadsheet.length*2);
		Hashtable<String,Professor> professorHash=new Hashtable<String,Professor>(professorSpreadsheet.length*2);
		//String[][] profSpreadsheet=new String[338][3];
		//Read csv file into spreadsheet cell by cell
		//print2D(courseSpreadsheet);
		roomSpreadsheet=makeSpreadsheet(new File("proto-roomslist.csv"),roomSpreadsheet);
		professorSpreadsheet=makeSpreadsheet(new File("proto-profslist.csv"),professorSpreadsheet);
		courseSpreadsheet=makeSpreadsheet(new File("proto-courselist.csv"),courseSpreadsheet);
		//print2D(roomSpreadsheet);
		print2D(professorSpreadsheet);
		
		//NOTE: I stored nulls because I haven't sanitized the data in any meaningful way yet
		ArrayList<Room> rooms= generateRooms(roomSpreadsheet);
		ArrayList<Professor> professors= generateProfessors(professorSpreadsheet,professorHash);
		ArrayList<Course> courses= generateCourses(courseSpreadsheet,courseHash,buildingMap, professorHash);
		
		/*for(Course c: courses){
			System.out.println(c.toString());
			for(Professor p:c.getProfessors()){
				System.out.println("\t"+p.toString());
			}
			for (Room r: c.getPreferredRooms()){
				System.out.print("\t"+r.toString());
			}
			System.out.println();
		}	*/
		
		//linkCoursesToRooms();//this is where the magic happens
		
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
		
	
	}
	public void linkRoomsToCourses(ArrayList<Course> courses, ArrayList<Room> rooms){
		/*courses.shuffle
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
	
	private boolean canMove(Course c){
		for(Room r : c.getPreferredRooms()){
			for(Tuple<Time,Time> t:c.getPreferredTimes()){
				if(r.isNotAssigned(t)) return true;
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
			Room r = new Room(accessible,buildingName, buildingShort, capacity,roomNumber,type);
			//eventually implement the following line
			//r.setTech(...)			
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
			System.out.println(name);
			Professor p=new Professor(name);
			//Don't forget that we're setting the size of profList by hand up in go()
			if (pH.containsKey(name)){//we should "probably" write our own exception. 
				try {
					throw new Exception("This professor name already exists! What are you doing?");
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
	
	     

	public ArrayList<Course> generateCourses(String[][] cl, Hashtable<String,Course> ch, HashMap<String,ArrayList<Room>> rH, Hashtable<String,Professor> pH){
		/*
		 * [Shortname|Longname|Professor|Capacity|Day|Time|Building|RoomNumber|Type|CP|DVD|VCR|Slides|OH|Concurrent|Noncurrent]
		 */
	
		

		System.out.println("Generating Courses");
		ArrayList<Course> courseList=new ArrayList<Course>();
		int allCourses=cl.length;
		int eachCourse=cl[0].length;
		Course temp;
		for(int row=1;row<allCourses;row++){//start at 1 because first row is headings of columns
			
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
			
			
			//Making preferred Times Begins
			String daysOfWeek=cl[row][4];
			String time=cl[row][5];
			String[] times=time.split("-",2);
			char[] dow=daysOfWeek.toCharArray();
			Time start;
			Time end;
			
			
			if(!times[0].isEmpty()){
				for(int i=0;i<dow.length;i++){
					if(dow[i]=='T' && i!=dow.length-1 && dow[i+1]=='H'){
						char[] th={'T','H'};
						start=new Time(th,times[0],true);
						end=new Time(th, times[1],false);
						temp.addPreferredTimes(new Tuple<Time,Time>(start,end));
					}
					else{
						
						start=new Time(dow[i],times[0],true);
						end=new Time(dow[i], times[1],false);
						temp.addPreferredTimes(new Tuple<Time,Time>(start,end));
					}
				}
			}//End preferredTimes
			//Handling preferredRooms Begins
			
			if (!cl[row][6].isEmpty()){
				//Building
				String buildingShort=cl[row][6];// We should probably check for typos or errors in the building names...-LGS
					
				ArrayList<Room> roomsInBuilding;
				if (rH.containsKey(buildingShort)){
					roomsInBuilding=rH.get(buildingShort);
				} else {
					System.out.println("Throw an error for "+buildingShort+"!");
					continue;
				}
				
				
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
		}
		courseList.trimToSize();
		return courseList;
	}
	
//NOTE: Do we still need this method????-LGS
		/*public void linkProfessorsAndCourses(ArrayList<Professor> professors, String[][] cSS, Hashtable<String,Course> cH, Hashtable<String,Professor> pH){


		System.out.println("Matching Professors and Courses");
		
		for (Enumeration<Course> courseList = cH.elements(); courseList.hasMoreElements();){
			Course c =courseList.nextElement();
			ArrayList<String> professorName=c.getProfessors();
			Professor p = pH.
		}
		
		
		//not exactly sure how to implement these loops at this point
		//maybe take a list of <professorName,courseName> pairs and
		
		//assume we have courseSpreadSheet
		
		for (int i=0; i<cSS.length; i++){
			Course c = cH.get(cSS[i][1]);
			Professor p=pH.get(cSS[i][2]);
			c.addProfessor(p);
			p.addCourse(c);
		}
	}
		
	 */
	
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
