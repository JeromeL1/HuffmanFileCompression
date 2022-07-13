import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * unzip class.
 * @author jeromeli
 * 
 */
public class JMUnzip {

  /**
   * main method.
   * @param args command line args.
   */
  public static void main(String[] args) {

    HuffmanSave huffSave;
    FileInputStream fileIn;
    ObjectInputStream objIn;
    HuffTree tree;
    FileOutputStream fileOut = null;
    byte[] arr = null;

    // Early exit
    if (args.length < 2) {
      System.err.println("You must enter a file name.");
      return;
    }

    if (!args[0].endsWith(".jmz")) {
      System.err.print("file to unzip is not jmz file.");
      return;
    }


    try {
      fileIn = new FileInputStream(args[0]);
      objIn = new ObjectInputStream(fileIn);
      huffSave = (HuffmanSave) objIn.readObject();
      objIn.close();
      fileIn.close();

      BuildHuffTree.setSeqMap(huffSave.getFrequencies());
      tree = BuildHuffTree.buildTree();
      arr = BuildHuffTree.decodeSeq(tree, huffSave.getEncoding());

    } catch (IOException ioe) {
      System.err.print("");
      return;
    } catch (ClassNotFoundException i) {
      System.err.print("");
    }

    try {
      fileOut = new FileOutputStream(args[1]);
    } catch (FileNotFoundException fnfe) {
      fnfe.printStackTrace();
    }

    try {
      if (fileOut != null) {
        fileOut.write(arr);
      }
    } catch (IOException i) {
      i.printStackTrace();
    }

  }
}
