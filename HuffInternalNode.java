/** Huffman tree node: Internal class. */
public class HuffInternalNode extends HuffBaseNode {
  private HuffBaseNode left;
  private HuffBaseNode right;

  /** Constructor. */
  public HuffInternalNode(HuffBaseNode l, HuffBaseNode r, int wt) {
    super(wt);
    left = l;
    right = r;

  }

  /**
   * left child.
   * 
   * @return The left child.
   */
  public HuffBaseNode left() {
    return left;
  }

  /**
   * right child.
   * 
   * @return The right child.
   */
  public HuffBaseNode right() {
    return right;
  }

  /**
   * internal is not leaf.
   * 
   * @Return false
   */
  public boolean isLeaf() {
    return false;
  }
}
