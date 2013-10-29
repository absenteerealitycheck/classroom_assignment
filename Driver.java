import java.util.*;
import java.lang.*;
import java.io.*;
public class Driver{
	
	public static void main(String args[]){
		new Driver().go();
		
	}
	
	public void go(){
		String[][] roomSpreadsheet = new String[100][20];
		String[][] courseSpreadsheet = new String[642][16];
		//Read csv file into spreadsheet cell by cell
		//probably need to sanitize data as we read it in, i.e. use String currentBuilding
		//does it make sense to store nulls?
		ArrayList<Room> rooms= generateRooms(roomSpreadsheet);
		ArrayList<Course> courses= generateCourses();
	
		/*
		 * after generating each room, we need to either generate all the professors or all the courses.
		 * it should be noted that the order we do these in determines greatly what constructors we need for each class
		 */
		
	}
	
	public ArrayList<Room> generateRooms(String[][] rS){
		/*However we construct rS, write a visual representation of it below
		 * 
		 * rS:
		 * [BuildingName|...]
		 * [BuildingName|...]
		 * ...
		 * 
		*/
		//after this is just pseudocode. change it to make it work.
		int numberOfRooms=rS.length;
		numberOfRooms=100;
		ArrayList<Room> rooms= new ArrayList<Room>(numberOfRooms);
		for(int row=0; row<rS.length; row++){
			String type=coalesce(rS[row][2],rS[row][3],rS[row][4]);
			//the above is just a guess, assuming rS is formatted [Building|Room|Seminar|SmallClassrm|Lecture|Seats...] 
			//it might make more sense to sanitize and construct rS as [Building|Room|Type|Capacity|...]
			Boolean accessible=Boolean.valueOf(rS[row][6]);
			String buildingName=rS[row][0];
			Integer capacity= Integer.valueOf(rS[row][5]);
			String roomNumber=rS[row][1];
			//these guys can change too. depends how we constuct rS
			//the next two lines should be ok 
			Room r = new Room(accessible,buildingName,capacity,roomNumber,type);
			//eventually implement the following line
			//r.setTech(...)			
			rooms.add(r);
		}		
		rooms.trimToSize();
		return rooms;
	}
	
	public ArrayList<Professor> generateProfessors(String[][] profs){//just some speculative code for now on how we should break stuff up		
		ArrayList<Professor> profList=new ArrayList<Professor>();
		int allProfs=profs.length;
		int eachProf=profs[0].length;
		for(int i=0;i<allProfs;i++){
			for(int j=0;j<eachProf;j++){
				//break things up and send everything out.
			}
		}
		profList.trimToSize();
		return profList;
	}
	
	public ArrayList<Course> generateCourses(String[][] cl){
		ArrayList<Course> courseList=new ArrayList<Course>();
		int allProfs=cl.length;
		int eachProf=cl[0].length;
		for(int i=0;i<allProfs;i++){
			for(int j=0;j<eachProf;j++){
				//break things up and send everything out.
			}
		}
		courseList.trimToSize();
		return courseList;
	}
	
	public void linkProfessorsAndCourses(ArrayList<Professor> professors, ArrayList<Course> courses, ArrayList<Tuple<Course,Professor>> pairs){
		//not exactly sure how to implement these loops at this point
		//maybe take a list of <professorName,courseName> pairs and
		for(Tuple t: pairs){
			Course c=courses.getCourseByName(t.getFirst());
			Professor p=professors.getProfessorByName(t.getSecond());
			c.addProfessor(p);
			p.addCourse(c);
		}
	}
	
	public <T> T coalesce(T a, T b, T c) {
	    return a != null ? a : (b != null ? b : c);
	}
	public static String[][] makeSpreadsheet(File file, String[][] strings) throws IOException{
		String[][] csv=strings;
		try {
			BufferedReader readIn = new BufferedReader(new FileReader(file));
			String line="";
			String[] row=new String[csv[0].length];
			int count=0;
			while((line=readIn.readLine())!=null){
				String [] temp= line.split(", ",row.length);
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
}
