import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * zip class.
 * 
 * @author jeromeli
 *
 */
public class JMZip {
  /**
   * main method.
   * 
   * @param args command line args.
   */
  public static void main(String[] args) {
    HuffmanSave huffSave = null;

    try {
      // Early exit
      if (args.length < 2 || args[1].substring(0, 1).equals("/")) {
        System.err.println("You must enter a file name.");
        return;
      }


      HashMap<Byte, Integer> seqMap;
      HuffTree tree = null;

      FileInputStream fis = new FileInputStream(args[0]);
      BufferedInputStream bis = new BufferedInputStream(fis);

      byte[] arr = bis.readAllBytes();
      bis.close();

      BitSequence bit = null;

      seqMap = BuildHuffTree.readFreq(arr);
      tree = BuildHuffTree.buildTree();
      bit = BuildHuffTree.encode(tree, arr);
      huffSave = new HuffmanSave(bit, seqMap);


    } catch (FileNotFoundException fnfe) {
      System.err.print("file not found.");
    } catch (IOException ioe) {
      System.err.println("Unable to open file.");
    }

    try {
      FileOutputStream fileOut = new FileOutputStream(args[1]);
      ObjectOutputStream zipFile = new ObjectOutputStream(fileOut);
      zipFile.writeObject(huffSave);
      zipFile.close();
    } catch (FileNotFoundException fnfe) {
      System.err.print(args[1].toString() + "is not found");
      return;

    } catch (IOException ioe) {

      System.err.print(args[1].toString() + "is not valid");
      return;

    }

  }

}
