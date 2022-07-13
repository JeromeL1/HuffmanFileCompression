import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * bunch of utility methods for building the hufftree.
 * @author jeromeli and opendsa huffman tree implementations.
 *
 */
public class BuildHuffTree {

  public static PriorityQueue<HuffTree> queue;
  public static HashMap<Byte, Integer> frequency;
  public static HashMap<Byte, String> seqMap;
  public static Iterator<Integer> iterator;

  /**
   * reads freq and puts it in a freq hashmap.
   * 
   * @param arr byte arr.
   * @return hashmap.
   * @throws IOException ioexception.
   */
  public static HashMap<Byte, Integer> readFreq(byte[] arr) throws IOException {

    frequency = new HashMap<Byte, Integer>();

    for (byte b : arr) {

      if (frequency.containsKey(b)) {
        int temp = frequency.get(b).intValue();
        frequency.put(b, ++temp);
      } else {
        frequency.put(b, 1);
      }

    }

    return frequency;

  }

  /**
   * builds hufftree.
   * 
   * @return hufftree.
   */
  public static HuffTree buildTree() {

    queue = new PriorityQueue<HuffTree>();
    for (Byte b : frequency.keySet()) {
      queue.add(new HuffTree(b, frequency.get(b)));

      if (frequency.keySet().size() == 1) {
        return new HuffTree(b, frequency.get(b));
      }

    }

    HuffTree tmp1 = null;
    HuffTree tmp2 = null;
    HuffTree tmp3 = null;

    while (queue.size() > 1) { // While two items left
      tmp1 = queue.poll();
      tmp2 = queue.poll();
      tmp3 = new HuffTree(tmp1.root(), tmp2.root(), tmp1.weight() + tmp2.weight());
      queue.add(tmp3); // Return new tree to heap
    }

    return tmp3;
  }

  /**
   * makes the bit sequence.
   * 
   * @param tree tree that it traverses.
   * @param bytes all the bytes from the file.
   * @return the bitSequence.
   */
  public static BitSequence encode(HuffTree tree, byte[] bytes) {
    BitSequence onesNZeros = new BitSequence();
    seqMap = new HashMap<>();

    if (bytes.length == 0) {
      return onesNZeros;
    }
    if (bytes.length == 1) {
      seqMap.put(bytes[0], "0");
      onesNZeros.appendBits(seqMap.get(bytes[0]));
    } else {
      encode(tree.root(), "");

      for (byte b : bytes) {
        onesNZeros.appendBits(seqMap.get(b));
      }
    }

    return onesNZeros;
  }

  /**
   * helper traverse method, it appends binary stuff to the sequence.
   * 
   * @param node root node.
   * @param bits the sequence.
   */
  public static void encode(HuffBaseNode node, String bits) {

    if (node.isLeaf()) {

      HuffLeafNode leaf = (HuffLeafNode) node;
      seqMap.put(leaf.value(), bits);
      return;

    }

    HuffInternalNode internal = (HuffInternalNode) node;
    encode(internal.left(), bits + "0");
    encode(internal.right(), bits + "1");

  }

  /**
   * decodes the sequence.
   * 
   * @param tree tree to traverse.
   * @param onesNZeros bit sequence from huff save.
   * @return byte arr.
   */
  public static byte[] decodeSeq(HuffTree tree, BitSequence onesNZeros) {

    ArrayList<Byte> list = new ArrayList<>();

    byte[] result = new byte[0];

    if (onesNZeros.length() == 0) {
      return result;
    } else if (onesNZeros.length() == 1) {
      HuffLeafNode leaf = (HuffLeafNode) tree.root();
      list.add(leaf.value());
      result = new byte[list.size()];
      result[0] = list.get(0);
    } else {

      iterator = onesNZeros.iterator();
      while (iterator.hasNext()) {
        decodeTree(tree.root(), list);
      }
      result = new byte[list.size()];
      for (int i = 0; i < list.size(); i++) {

        result[i] = list.get(i);

      }

    }


    return result;
  }

  /**
   * helper method.
   * 
   * @param node tree node to traverse.
   * @param list list that keeps track of bytes.
   */
  private static void decodeTree(HuffBaseNode node, ArrayList<Byte> list) {

    if (node.isLeaf()) {
      HuffLeafNode leaf = (HuffLeafNode) node;
      list.add(leaf.value());
      return;

    }

    HuffInternalNode internal = (HuffInternalNode) node;
    if (iterator.hasNext() && iterator.next() == 1) {
      decodeTree(internal.right(), list);
    } else {
      decodeTree(internal.left(), list);
    }
  }

  /**
   * it sets freq from the huffSave.
   * 
   * @param freq the freq hashmap.
   */
  public static void setSeqMap(HashMap<Byte, Integer> freq) {
    frequency = freq;
  }

}
