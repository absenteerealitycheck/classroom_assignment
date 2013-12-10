import java.util.Set;

public abstract class Node {
	Node(){}
	abstract Set<Node> getNeighbors();
	abstract int getNeighborCount();
	abstract boolean isNeighbor(Node n);
}
