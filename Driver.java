import java.util.*;
import java.lang.*;
import java.io.*;
public class Driver{
	
	public static void main(String args[]) throws IOException{
		new Driver().go();
		
	}
	
	public void go() throws IOException{
		String[][] roomSpreadsheet = new String[81][5];
		String[][] courseSpreadsheet = new String[642][16];
		//String[][] profSpreadsheet=new String[338][3];
		//Read csv file into spreadsheet cell by cell
		courseSpreadsheet=makeSpreadsheet(new File("proto-courselist.csv"),courseSpreadsheet);
		//print2D(courseSpreadsheet);
		roomSpreadsheet=makeSpreadsheet(new File("proto-roomslist.csv"),roomSpreadsheet);
		//print2D(roomSpreadsheet);
		//profSpreadsheet=makeSpreadsheet(new File("proto-professorlist.csv"),profSpreadsheet);
		//print2D(profSpreadsheet);
		
		//probably need to sanitize data as we read it in, i.e. use String currentBuilding
		//does it make sense to store nulls? 
		//NOTE: I stored nulls because I haven't sanitized the data in any meaningful way yet
		ArrayList<Room> rooms= generateRooms(roomSpreadsheet);
		ArrayList<Course> courses= generateCourses(courseSpreadsheet);
		System.out.println("Success!");
		
		
		//ArrayList<Professor> professors= generateProfessors(profSpreadsheet);
		/*
		 * after generating each room, we need to either generate all the professors or all the courses.
		 * it should be noted that the order we do these in determines greatly what constructors we need for each class
		 */

		/*make a graph of courses - courses that overlap share an edge.
		 *this method will give us clusters of courses based on time
		 *go through each cluster and color separately?
		 *JP 10/29
		 */
	       
		Tuple<Time,Time> courseTimes; 
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
		}
		
		
	}
	
	public ArrayList<Room> generateRooms(String[][] rS){
		System.out.println("Generating Rooms");
		/*However we construct rS, write a visual representation of it below
		 * 
		 * rS:
		 * [BuildingName|RoomNumber|Capacity|RoomType|Accessible]
		 * [BuildingName|...]
		 * ...
		 * current columns are [buildingName|roomNumber|capacity|type|accessible] - jpham14:10/28
		 * 
		*/
		//after this is just pseudocode. change it to make it work.
		int numberOfRooms=rS.length;
		//numberOfRooms=100;
		ArrayList<Room> rooms= new ArrayList<Room>(numberOfRooms);
		for(int row=1; row<rS.length; row++){
			//String type=coalesce(rS[row][2],rS[row][3],rS[row][4]);
			String type=rS[row][3];
			//the above is just a guess, assuming rS is formatted [Building|Room|Seminar|SmallClassrm|Lecture|Seats...] 
			//it might make more sense to sanitize and construct rS as [Building|Room|Type|Capacity|...]
			Boolean accessible=Boolean.valueOf(rS[row][4]);
			String buildingName=rS[row][0];
			Integer capacity= Integer.valueOf(rS[row][2]);
			String roomNumber=rS[row][1];
			//these guys can change too. depends how we constuct rS
			//the next two lines should be ok 
			Room r = new Room(accessible,buildingName,capacity,roomNumber,type);
			//eventually implement the following line
			//r.setTech(...)			
			rooms.add(r);
		}		
		rooms.trimToSize();
		System.out.println("WOOT WOOT");
		return rooms;
	}
	
	public ArrayList<Professor> generateProfessors(String[][] profs){//just some speculative code for now on how we should break stuff up		
		System.out.println("Generating Professors");
		ArrayList<Professor> profList=new ArrayList<Professor>();
		int allProfs=profs.length;
		int eachProf=profs[0].length;
		for(int row=0;row<allProfs;row++){
			for(int col=0;col<eachProf;row++){
				//break things up and send everything out.
				
			}
		}
		profList.trimToSize();
		return profList;
	}
	
	     
	public ArrayList<Course> generateCourses(String[][] cl){
		System.out.println("Generating Courses");
		ArrayList<Course> courseList=new ArrayList<Course>();
		int allProfs=cl.length;
		int eachProf=cl[0].length;
		Course temp;
		for(int row=1;row<allProfs;row++){//start at 1 because first row is headings of columns
			String shortname=cl[row][0];
			String longname=cl[row][1];
			//cl[row][ 2 ]is the professor teaching the course
			//String prof=cl[row][2];
			
			int capacity;
			if(cl[row][3].isEmpty()){
				capacity=10;
			}
			else{
				capacity=Integer.parseInt(cl[row][3]);
			}
			
			//These are the preferred rooms stored in the sheet
			String building=cl[row][6];
			String roomnum=cl[row][7];
			Room pref=new Room(building,roomnum);
			//type of class
			String type=cl[row][8];
			temp= new Course(capacity,longname,type);
		
			temp.addShortName(shortname);
			temp.addPreferredRooms(pref);
			//Making preferred Times
			String daysOfWeek=cl[row][4];
			String time=cl[row][5];
			String[] times=time.split("-",2);
			char[] dow=daysOfWeek.toCharArray();
			Time start;
			Time end;
			if(times[0].isEmpty()){
				times=new String[2];
				times[0]="12:00PM";
				times[1]="12:50PM";
			}
			if(daysOfWeek.isEmpty()){
				daysOfWeek="MWF";
			}
			
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
			
			courseList.add(temp);
			
		}
		courseList.trimToSize();
		return courseList;
	}
	
    /*	public void linkProfessorsAndCourses(ArrayList<Professor> professors, ArrayList<Course> courses, ArrayList<Tuple<Course,Professor>> pairs){
		System.out.println("Matching Professors and Courses");
		//not exactly sure how to implement these loops at this point
		//maybe take a list of <professorName,courseName> pairs and
		for(Tuple t: pairs){
			Course c=courses.getCourseByName(t.getFirst());
			Professor p=professors.getProfessorByName(t.getSecond());
			c.addProfessor(p);
			p.addCourse(c);
		}
	}*/
	
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
					csv[count][i]=temp[i];
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
		for(int row=0;row<s.length;row++){
			for(int col=0;col<s[0].length;col++){
				if(s[row][col]!=null){System.out.print(s[row][col]);}
				else continue;
			}
			System.out.print("\n");
		}
	}
}
