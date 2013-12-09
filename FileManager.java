import java.util.*;
import java.util.Map.Entry;
import java.io.*;

public class FileManager {

	private HashMap<String,File> files;
	private HashMap<String,String[][]> spreadsheets;
	private HashMap<String,Map<String,?>> data;

	public FileManager(){
		files=new HashMap<String,File>();
		spreadsheets=new HashMap<String,String[][]>();
		data=new HashMap<String,Map<String,?>>();
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * Adds a File f to the FileManager and associate it with the specified name.
	 * @param f - the File to add
	 * @param name - the name to associate with the File
	 */
	public String addFile(File f, String name){
		files.put(name,f);
		return name;
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * Adds a .csv File f to the FileManager and associate it with the specified name.
	 * @param f - the File to add
	 * @param name - the name to associate with the File
	 * @throws IllegalArgumentException - if the specified File is not a .csv file
	 */
	public String addCSVFile(File f, String name){
		System.out.println("Adding "+f.getName());
		if (!f.getName().endsWith(".csv")){
			throw new IllegalArgumentException(f.getName()+" is the incorrect file type");
		}
		return addFile(f,name);
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * TODO:Implement this?
	 */
	public void addAllCSVFiles(File f, String name){
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * Returns the set of all the File names in FileManager.
	 * @return the set of all the File names in FileManager
	 */
	public Set<String> getFileNames(){
		return this.files.keySet();
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * Returns the File associated with the specified name, or null if no such File exists.
	 * @param name - the name whose associated File is to be returned
	 * @return - the File associated with the specified name
	 */
	public File getFile(String name){
		return files.get(name);
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * Returns the set of all the spreadsheet names in FileManager.
	 * @return the set of all the spreadsheet names in FileManager
	 */
	public Set<String> getSpreadsheetNames(){
		return this.spreadsheets.keySet();
	}
	// ================================================================================================	

	// ================================================================================================
	/**
	 * Returns the spreadsheet representation associated with the specified name, or null if no such 
	 * spreadsheet exists.
	 * @param name - the name whose associated spreadsheet is to be returned
	 * @return - the two-dimensional array of Strings representation of the spreadsheet
	 */
	public String[][] getSpreadsheet(String name){
		return spreadsheets.get(name);
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * Returns the set of all the data names in FileManager.
	 * @return the set of all the data names in FileManager
	 */
	public Set<String> getDataNames(){
		return this.data.keySet();
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * Returns the data map associated with the specified name, or null if no such data exists.
	 * @param name - the name whose associated data map is to be returned
	 * @return - the data map, which uses String keys to access its values
	 */
	public Map<String,?> getData(String name){
		return data.get(name);
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * Loads the File associated with the specified name into a two-dimensional array of Strings and 
	 * returns the two-dimensional array. 
	 * @param name - the name associated with the File to load
	 * @return true if the File is loaded successfully, false otherwise 
	 */
	public boolean loadFile(String name) {
		try{
			String[][] newSpreadsheet = makeSpreadsheet(getFile(name));
			this.spreadsheets.put(name, newSpreadsheet);
			return true;
		} catch(IOException e){
			return false;
		}
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * Takes a file and reads it into a spreadsheet for us to use
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static String[][] makeSpreadsheet(File file) throws IOException{
		System.out.println("Reading "+file.getName());
		String[][] csv = null;
		try {
			//Determine the dimensions of the two-dimensional array
			BufferedReader b = new BufferedReader(new FileReader(file));
			String next=b.readLine(); 
			int cols=next.split(";").length+1;
			int rows=1;
			while ((next=b.readLine())!=null){
				rows++;
			}
			b.close();
			csv = new String[rows][cols];

			//Load the file into the two-dimensional array
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
	} // makeSpreadsheet
	// ================================================================================================

	// ================================================================================================
	/**
	 * Adds the specified custom data map to the File Manager. 
	 * @param name - the name to associate with the specified data map
	 * @param d - the custom data map
	 */
	public void addData(String name, Map<String,?> d){
		data.put(name, d);
	}
	// ================================================================================================

	// ================================================================================================
	private enum Pill {
		historical, room, course, professor, department;
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * Transforms the spreadsheet associated with the specified name into some number of data maps, and
	 * returns the set of keys needed to access the newly created data maps.
	 * @param name - the name associated with the spreadsheet to process
	 * @return the set of keys needed to access the newly created data maps.
	 * @respect calls to data.put() are made directly inside process, not from the methods it calls
	 */
	public Set<String> process(int phase){
		//TODO:Use reflection on name
		String name="";
		switch(phase){
		case 1:
			name="historical";
			System.out.println("Processing Hisorical data");
			data.put("roomsandcourses", associateFieldAndRooms(getSpreadsheet(name),1));
			data.put("roomsanddepartments", associateFieldAndRooms(getSpreadsheet(name),2));
			data.put("roomsandprofessors", associateFieldAndRooms(getSpreadsheet(name),3));
			return data.keySet();
		case 2:
			String spreadsheetNames="roomsandprofessors,roomsandcourses,roomsanddepartments,workingcourselist,workingroomlist";
			data.put("recommendedroomslist",generateRecommendedRooms(spreadsheetNames));
			return data.keySet();
		case 3:
			//data has the "times" key already, see Driver
			data.put("workingroomlist", generateRooms(getSpreadsheet("workingroomlist")));
			data.put("workingcourselist",generateCourses(getSpreadsheet("workingcourselist")));
			data.put("recommendedroomslist", readRecs(getSpreadsheet("recommendedroomslist")));
			
			drawEdges("workingcourselist", "workingroomlist", "recommendedroomslist", "times");

			return data.keySet();
		default:
			break;
		}
		return null;
	}
	// ================================================================================================
	
	// ================================================================================================
	/**
	 * Finds every room that a unique value in the historical spreadsheet has used. The field to search 
	 * search for unique values is indicated by control.
	 * @param cHS - the courseHistorySpreadsheet
	 * @param control - indicates which field to associate with rooms
	 * @return a map of every room this <?> has used
	 */
	private TreeMap<String,ArrayList<String>> associateFieldAndRooms(String[][] cHS, int control){
		TreeMap<String,ArrayList<String>> h = new TreeMap<String,ArrayList<String>>();
		ArrayList<String> forbiddenRooms = new ArrayList<String>();
		forbiddenRooms.add("NEWP 100");
		forbiddenRooms.add("CPRD 101A");
		forbiddenRooms.add("FROS BARKER");
		forbiddenRooms.add("GROS 11");
		forbiddenRooms.add("GROS 12");
		forbiddenRooms.add("PRDT LR");
		forbiddenRooms.add("MERR");
		for (int i=1; i<cHS.length; i++){
			String field="";
			String room=cHS[i][7];
			if (forbiddenRooms.contains(room)){
				continue;
			}
			switch(control){
			case 1://course
				field=cHS[i][1];
				break;
			case 2://department
				field=cHS[i][1].substring(0, 4);
				break;
			case 3://professor
				field=cHS[i][4];
				break;
			}
			String[] splitField = field.split("  ");
			for (String f:splitField){
				if (!h.containsKey(f)){
					h.put(f, new ArrayList<String>());
				}
				if (!h.get(f).contains(room)){
					h.get(f).add(room);
				}
			}
		}
		return h;
	} // associateFieldAndRooms
	// ================================================================================================

	/**
	 * 
	 * @param spreadsheetNames
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private TreeMap<String,?> generateRecommendedRooms(String spreadsheetNames){
		String[] names = spreadsheetNames.split(",");
		String[][] roomsAndProfessors=getSpreadsheet(names[0]);
		String[][] roomsAndCourses=getSpreadsheet(names[1]);
		String[][] roomsAndDepartments=getSpreadsheet(names[2]);
		String[][] workingCourseList=getSpreadsheet(names[3]);
		String[][] workingRoomList=getSpreadsheet(names[4]);

		String[][] recommendedRoomList=new String[workingCourseList.length][8];

		TreeMap<String,ArrayList<String>> t=new TreeMap<String,ArrayList<String>>(); 
		ArrayList<String> info=new ArrayList<String>();

		for(int i=0; i<workingCourseList.length; i++){
			info.add(workingCourseList[i][1]+";");
			info.add(workingCourseList[i][2]+";");
			info.add(workingCourseList[i][3]+";");

			TreeSet<String> pList=findRooms(workingCourseList[i][3],roomsAndProfessors);
			TreeSet<String> cList=findRooms(workingCourseList[i][0],roomsAndCourses);
			TreeSet<String> dList=findRooms(workingCourseList[i][0].substring(0,4),roomsAndDepartments);
			TreeSet<String> cup =new TreeSet<String>();
			cup.addAll(cList);
			cup.addAll(pList);
			TreeSet<String> notdncup = new TreeSet<String>();
			TreeSet<String> rList = new TreeSet<String>();
			notdncup.addAll(dList);
			rList.addAll(dList);
			notdncup.removeAll(cup);
			rList.removeAll(notdncup);

			info.add(rList.toString().substring(1,rList.toString().length()-1)+";");			
			info.add(pList.toString().substring(1,pList.toString().length()-1)+";");
			info.add(cList.toString().substring(1,cList.toString().length()-1)+";");
			info.add(dList.toString().substring(1,dList.toString().length()-1)+";");

			t.put(workingCourseList[i][0], ((ArrayList<String>)info.clone()));
			info.clear();
		}
		return t;
	}
	
	/**
	 * 
	 * @param key
	 * @param source
	 * @return
	 */
	private TreeSet<String> findRooms(String key, String[][] source){
		TreeSet<String> rooms = new TreeSet<String>();
		String[] keys = key.split("  ");
		for (String k:keys){
			for (String[] row:source){
				if (row[0].equals(k)){
					String[] qux = row[1].split(", ");
					for (String quux:qux){
						rooms.add(quux);
					}
				}
			}
		}
		//remove last , from rooms 
		return rooms;
	}

	// ================================================================================================
	/**
	 * Creates the map of Room objects, using the toString() method of each Room as its key.
	 * @param rS - the spreadsheet of rooms to use to generate Rooms
	 * @return the data map which uses Room.toString() keys to access Room objects
	 */
	private Map<String,Room> generateRooms(String[][] rS){
		System.out.println("Generating Rooms");
		TreeMap<String,Room> rooms= new TreeMap<String,Room>();
		for(int row=1; row<rS.length; row++){
			Room r = new Room(rS[row]);
			rooms.put(r.toString(),r);
		}		
		System.out.println("Done Generating Rooms");
		return rooms;
	}
	// ================================================================================================


	// ================================================================================================
	private Map<String,Course> generateCourses(String[][] cS) {
		System.out.println("Generating Courses");
		TreeMap<String,Course> courses= new TreeMap<String,Course>();
		for(int row=1; row<cS.length; row++){
			Course c = new Course(cS[row]);
			courses.put(c.toString(),c);
		}		
		System.out.println("Done Generating Courses");
		return courses;	
	}

	// ================================================================================================

	// ================================================================================================
	/**
	 * 
	 * @param rS
	 * @return
	 */
	private Map<String,HashSet<String>> readRecs(String[][] rS){
		TreeMap<String,HashSet<String>> recs = new TreeMap<String,HashSet<String>>();
		for (String[] row:rS){
			recs.put(row[0], new HashSet<String>(Arrays.asList(row)));
		}
		return recs;
	}
	
	// ================================================================================================
	
	
	// ================================================================================================
	/**
	 * Populates the Sets inside each of the data objects whose names are specified.
	 * @param courses
	 * @param rooms
	 * @param times
	 */	
	@SuppressWarnings("unchecked")
	private void drawEdges(String workingcourselist, String workingroomlist, String recommendedroomslist, String times){
		TreeMap<String,Course> courseMap = (TreeMap<String, Course>) data.get(workingcourselist); 
		TreeMap<String,Room> roomMap = (TreeMap<String, Room>) data.get(workingroomlist);
		TreeMap<String,HashSet<String>> recMap = (TreeMap<String, HashSet<String>>) data.get(recommendedroomslist);
		TreeMap<String,Time> timeMap = (TreeMap<String, Time>) data.get(times);
		
		for (Room r:roomMap.values()){
			TreeMap<String,Room> tmr=((TreeMap<String,Room>)roomMap.clone());
			tmr.remove(r.toString());
			r.addRooms((Set<Room>)tmr.values());
		}

		for (Course c:courseMap.values()){
			double startTime=c.getStartTime();
			double endTime=c.getEndTime();
			double blocks=0;
			ArrayList<Integer> dow = c.getDow();
			if(startTime%100==30){
				startTime+=20;
			}
			if(endTime%100==30){
				endTime+=20;
			} else if(endTime%100==20){
				endTime+=30;
			} else if(endTime%100==50){
				endTime+=50;
			}
			blocks=(endTime-startTime)/50;
			startTime=c.getStartTime();
			for (int i=0; i<blocks; i++){				
				for (Integer j:dow){	
					Time t=timeMap.get(j+":"+startTime);
					t.addCourse(c.addTime(t));
				}
				startTime+=30;
				if (startTime%100==60){
					startTime+=40;
				}
			}
			
			TreeMap<String,Room> cRoomMap=(TreeMap<String,Room>) roomMap.clone();
			Set<String> keys = roomMap.keySet();
			keys.removeAll(recMap.get(c));
			for (String k:keys){
				Room r=roomMap.get(k);
				r.addCourse(c.addPreferredRoom(r));
			}
			
		}
	}

	// ================================================================================================

	// ================================================================================================
	@SuppressWarnings("unchecked")
	public void write(String[] names){
		for (String s:names){
			File f = new File("proto-"+s+"list.csv");
			writeHashToCSV((TreeMap<String,ArrayList<String>>)data.get(s), f);
		}
	}


	private boolean writeHashToCSV(Map<String,ArrayList<String>> d, File f){
		try{
			BufferedWriter bw=new BufferedWriter(new FileWriter(f));
			for (Entry<String, ArrayList<String>> es :d.entrySet()){
				String ts2=es.getValue().toString();
				ts2=ts2.substring(1,ts2.length()-1);
				System.out.println("[1]"+ts2);
				//System.out.println(es.getKey()+";"+ts2+"\n");
				ts2=ts2.replace(";, ", ";");
				System.out.println("[2]"+ts2);
				bw.write(es.getKey()+";"+ts2+"\n");
			}
			bw.close();
			System.out.println("Done writing to file "+f.getName());
			return true;
		} catch (IOException e){
			return false;
		}
	}

	public String spreadsheetStringify(String[][] s){
		String result="";
		for(int row=1;row<s.length;row++){
			for(int col=0;col<s[0].length;col++){
				if(s[row][col]!=null){result+=(s[row][col]);}
				else continue;
			}
			result+="\n";
		}
		return result;
	}

	//TODO:Deprecate if never used
	private <E> void typeCheck(Class<E> type, Object o){
		if (o!=null&&!type.isInstance(o)){
			throw new ClassCastException(badElementMsg(type, o));
		}
	}

	private <E> String badElementMsg(Class<E> type, Object o){
		return "Attempt to use "+o.getClass()+" element with element type "+type;
	}
}
//================================================================================================