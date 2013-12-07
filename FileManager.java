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
	public void addFile(File f, String name){
		files.put(name,f);
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * Adds a .csv File f to the FileManager and associate it with the specified name.
	 * @param f - the File to add
	 * @param name - the name to associate with the File
	 * @throws IllegalArgumentException - if the specified File is not a .csv file
	 */
	public void addCSVFile(File f, String name){
		System.out.println("Adding "+f.getName());
		System.out.println("Does End with "+f.getName().endsWith(".csv"));
		
		if (!f.getName().endsWith(".csv")){
			throw new IllegalArgumentException(f.getName()+" is the incorrect file type");
		}
		addFile(f,name);
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
	public Set<String> process(String name){
		//TODO:Use reflection on name
		Pill p=Pill.valueOf(name);
		switch(p){
		case historical:
			System.out.println("Processing Hisorical data");
			data.put("roomsandcourses", associateFieldAndRooms(getSpreadsheet(name),1));
			data.put("roomsanddepartments", associateFieldAndRooms(getSpreadsheet(name),2));
			data.put("roomsandprofessors", associateFieldAndRooms(getSpreadsheet(name),3));
			return data.keySet();
		case course:
			data.put(name, generateRooms(getSpreadsheet(name)));
			TreeSet<String> s = new TreeSet<String>();
			s.add(name);
			return s;
		case department:
			break;
		case professor:
			break;
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
	
	// ================================================================================================
	/**
	 * Creates the map of Room objects, using the toString() method of each Room as its key.
	 * @param rS - the spreadsheet of rooms to use to generate Rooms
	 * @return the data map which uses Room.toString() keys to access Room objects
	 */
	private Map<String,?> generateRooms(String[][] rS){
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
				//System.out.println(es.getKey()+";"+ts2+"\n");
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