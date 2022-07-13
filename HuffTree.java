/** A Huffman coding tree. */
class HuffTree implements Comparable<HuffTree> {
  private HuffBaseNode root;

  /** Constructors. */
  HuffTree(byte el, int wt) {
    root = new HuffLeafNode(el, wt);
  }

  HuffTree(HuffBaseNode l, HuffBaseNode r, int wt) {
    root = new HuffInternalNode(l, r, wt);
  }

  HuffBaseNode root() {
    return root;
  }

  int weight() {
    return root.weight();
  }


  @Override
  public int compareTo(HuffTree o) {
    HuffTree that = (HuffTree) o;
    if (root.weight() < that.weight()) {
      return -1;
    } else if (root.weight() == that.weight()) {

      return 0;

    } else {

      return 1;
    }
  }
}
