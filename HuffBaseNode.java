/** Huffman tree node implementation: Base class. */
public abstract class HuffBaseNode {

  private int weight;

  public HuffBaseNode() {

  }

  public HuffBaseNode(int weight) {
    this.weight = weight;
  }

  public int weight() {
    return weight;
  }

  public abstract boolean isLeaf();

}

