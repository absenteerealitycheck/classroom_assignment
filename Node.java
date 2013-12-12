import java.util.Set;
import java.util.HashSet;

public abstract class Node {
	private int color=-1;
	private String name;
	private int capacity;
	private String type;
	private Set<Course> courseNeighbors=new HashSet<Course>();
	private Set<Room> roomNeighbors=new HashSet<Room>();
	Node(){}
	/**
	 * Changes the color for this Node to the specified value.
	 * @param color - the value to which the color should be changed.
	 */
	public void setColor(int color){this.color=color;}
	/**
	 * Returns the color for this Node.
	 * @return the color for this Node.
	 */
	public int getColor(){return this.color;}
	/**
	 * Changes the name for this Node to the specified value.
	 * @param name - the value to which the name should be changed.
	 */
	public void setName(String name){this.name=name;}
	/**
	 * Returns the name for this Node.
	 * @return the name for this Node.
	 */
	public String getName(){return this.name;}
	/**
	 * Changes the capacity for this Node to the specified value.
	 * @param capacity - the value to which the capacity should be changed.
	 */
	public void setCapacity(int capacity){this.capacity=capacity;}
	/**
	 * Returns the capacity for this Node.
	 * @return the capacity for this Node.
	 */
	public int getCapacity(){return this.capacity;}
	/**
	 * Changes the type for this Node to the specified value.
	 * @param type - the value to which the type should be changed.
	 */
	public void setType(String type){this.type=type;}
	/**
	 * Returns the type for this Node.
	 * @return the type for this Node.
	 */
	public String getType(){return this.type;}
	/**
	 * Ensures that this Node contains the specified Node as a neighbor.
	 * @param n - the Node to add to the neighbors list
	 * @return this Node
	 */
	public Node addNeighbor(Node n){
		if (n instanceof Course){this.courseNeighbors.add((Course)n);} 
		else if (n instanceof Room){this.roomNeighbors.add((Room)n);}
		return this;
	}
	/**
	 * Ensures that this Node does not contain the specified Node as a neighbor.
	 * @param n - the Node to remove from the neighbors list, if it is present
	 * @return this Node
	 */
	public Node removeNeighbor(Node n){
		if (n instanceof Course){this.courseNeighbors.remove((Course)n);} 
		else if (n instanceof Room){this.roomNeighbors.remove((Room)n);}
		return this;
	}
	/**
	 * Returns a Set of all neighbors of this Node.
	 * @return the Set of all neighbors of this Node.
	 */
	public Set<Node> getNeighbors(){
		Set<Node> tmp= new HashSet<Node>(this.roomNeighbors);
		tmp.addAll(this.courseNeighbors);
		return tmp;
	}
	public Set<Course> getCourseNeighbors(){
		return this.courseNeighbors;
	}
	public Set<Room> getRoomNeighbors(){
		return this.roomNeighbors;
	}
	/**
	 * Returns the number of neighbors for this Node.
	 * @return the number of neighbors for this Node.
	 */
	public int getNeighborCount(){return this.roomNeighbors.size()+this.courseNeighbors.size();}
	/**
	 * Returns true if the specified Node is contained in the neighbors set for this Node.
	 * @param n - the Node whose presence in this Node's neighbor set is to be tested
	 * @return true if this Node contains the specified Node in its neighbor set
	 */
	public boolean isNeighbor(Node n){return this.getNeighbors().contains(n);}
	abstract public String toString();
}
