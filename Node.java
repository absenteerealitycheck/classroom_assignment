import java.util.Set;

public abstract class Node {
	private int color=-1;
	Node(){}
	abstract Set<Node> getNeighbors();
	abstract int getNeighborCount();
	abstract boolean isNeighbor(Node n);
	public void setColor(int color){this.color=color;}
	public int getColor(){return this.color;}
}
