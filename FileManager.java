import java.util.*;
import java.util.Map.Entry;
import java.io.*;

public class FileManager {

	private HashMap<String,File> files;
	private HashMap<String,String[][]> spreadsheets;
	private HashMap<String,Object> data;

	public FileManager(){

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
		if (!f.getName().endsWith(".csv")){
			throw new IllegalArgumentException(f.getName()+" is the incorrect file type");
		}
		addFile(f,name);
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
	 * Returns the set of all the File names in FileManager.
	 * @return the set of all the File names in FileManager
	 */
	public Set<String> getNames(){
		return this.files.keySet();
	}
	// ================================================================================================
	
	// ================================================================================================
	/**
	 * Returns the spreadsheet representation associated with the specified name, or null if no such 
	 * spreadsheet exists.
	 * @param name - the name whose associated spreadsheet to be returned
	 * @return - the two-dimensional array of Strings representation of the spreadsheet
	 */
	public String[][] getSpreadsheet(String name){
		return spreadsheets.get(name);
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
	public static String[][] makeSpreadsheet(File file) throws IOException{
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
	
	public void phaseOne(){
		//process something
		//write something
	}
	
	public void phaseTwo(){
		
	}
	
	private <E> void typeCheck(Class<E> type, Object o){
		if (o!=null&&!type.isInstance(o)){
			throw new ClassCastException(badElementMsg(type, o));
		}
	}
	
	private <E> String badElementMsg(Class<E> type, Object o){
		return "Attempt to use "+o.getClass()+" element with element type "+type;
	}
	
	public Object process(String name){
		//TODO:Use reflection on name
		generateRooms(name);
		return null;
	}
	
	private void generateRooms(String name){
		System.out.println("Generating Rooms");
		String[][] rS = this.spreadsheets.get(name); 
		ArrayList<Room> rooms= new ArrayList<Room>(rS.length);
		for(int row=1; row<rS.length; row++){
			rooms.add(new Room(rS[row]));
		}		
		this.data.put(name, rooms);
		System.out.println("Done Generating Rooms");
	}
	
	public boolean writeHashToCSV(String name, String fileName){
		try{
			File f = new File(fileName);
			Object o = this.data.get(name);
			typeCheck(HashMap.class, o);
			if (o instanceof HashMap<?,?>){
				throw new ClassCastException();
			}
			@SuppressWarnings("unchecked")
			HashMap<String,ArrayList<String>> hm=(HashMap<String,ArrayList<String>>) o;
			
			BufferedWriter bw=new BufferedWriter(new FileWriter(f));
			for (Entry<String, ArrayList<String>> es :hm.entrySet()){
				String ts2=es.getValue().toString();
				ts2=ts2.substring(1,ts2.length()-1);
				//System.out.println(ts.getFirst()+";"+ts2+"\n");
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
}
//================================================================================================