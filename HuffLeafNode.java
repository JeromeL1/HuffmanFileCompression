/** Huffman tree node: Leaf class. */
public class HuffLeafNode extends HuffBaseNode {
  private byte element; // Element for this node

  /** Constructor. */
  public HuffLeafNode(byte el, int wt) {
    super(wt);
    element = el;

  }

  /**
   * byte getter.
   * 
   * @return The element value.
   * 
   **/
  public byte value() {
    return element;
  }

  /**
   * leaf is a leaf.
   * 
   *  @Return true. 
   *
   **/
  public boolean isLeaf() {
    return true;
  }
}
