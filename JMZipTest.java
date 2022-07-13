import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit tests for the CS240 Huffman Coding Project.
 * 
 * @author Nathan Sprague
 * @version 3/2019
 *
 */
class JMZipTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private PrintStream originalOut;
  private PrintStream originalErr;

  // Keep a list of all the files that are created so that they can be deleted.
  private String[] createdFiles = {"empty.dat", "empty.jmz", "one_byte.dat", "one_byte.jmz",
      "mary.txt", "mary.jmz", "fibonacci.dat", "fibonacci.jmz", "bytes.dat", "bytes.jmz",
      "bytes_restored.dat", "flubber.jmz"};


  /**
   * Create several files that can be used to test encoding and decoding.
   * 
   * Reset System.out and System.err so they can be checked in tests.
   * 
   * @throws IOException
   */
  @BeforeEach
  public void setUp() throws IOException {
    originalOut = System.out;
    originalErr = System.err;
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));

    // Create an empty file.
    PrintWriter pw = new PrintWriter(new File("empty.dat"));
    pw.close();

    // Create a file containing a single byte.
    FileOutputStream fo = new FileOutputStream(new File("one_byte.dat"));
    fo.write(42);
    fo.close();

    // Create a short text file.
    pw = new PrintWriter(new File("mary.txt"));
    pw.print("Mary had a little lamb.  It's fleece was white as snow.\n");
    pw.close();

    // Create a short binary file containing truncated elements from the
    // Fibonacci sequence.
    fo = new FileOutputStream(new File("fibonacci.dat"));

    int prev1 = 1;
    int prev2 = 1;
    for (int i = 0; i < 1000; i++) {
      int next = prev1 + prev2;
      fo.write(next);
      prev1 = prev2;
      prev2 = next;
    }
    fo.close();

    // Create a file containing known counts of many different bytes.
    fo = new FileOutputStream(new File("bytes.dat"));

    for (int i = 0; i < 100; i++) { // Write the bytes 0-99
      for (int j = 0; j < i + 10; j++) { // Write each byte b+10 times.
        fo.write(i);
      }
    }
    fo.close();
  }

  /**
   * Restore System.out and System.err, also delete all files that were created
   * in setUp.
   */
  @AfterEach
  public void tearDown() {
    System.setOut(originalOut);
    System.setErr(originalErr);

    for (String fileName : createdFiles) {
      File file = new File(fileName);
      file.delete();
    }
  }

  // -----------------------------------------------
  // TESTS FOR JMZip COMMAND LINE ARGUMENT ERROR HANDLING
  //
  // NOTE: These tests only confirm that *some* error output is produced. It is
  // your responsibility to make sure that the output is appropriate.
  // -----------------------------------------------

  /**
   * Helper method to confirm that SOME output is generated.
   */
  private void assertProducesErrorMessage() {
    String resultOut = outContent.toString();
    String resultErr = errContent.toString();
    assertTrue(resultOut.length() != 0 || resultErr.length() != 0);
  }

  @Test
  public void testZipHandlesZeroArguments() {
    JMZip.main(new String[] {});
    assertProducesErrorMessage();
  }

  @Test
  public void testZipHandlesOneMissingArgument() {
    JMZip.main(new String[] {"file.txt"});
    assertProducesErrorMessage();
  }

  @Test
  public void testZipHandlesUnreadableFile() {
    JMZip.main(new String[] {"BLAfjdlSFl.txt", "bytes.jmz"});
    assertProducesErrorMessage();
  }

  @Test
  public void testZipHandlesUnwriteableFile() {
    JMZip.main(new String[] {"mary.txt", "/bytes.jmz"});
    assertProducesErrorMessage();
  }

  // -----------------------------------------------
  // TESTS FOR JMUnzip COMMAND LINE ARGUMENT ERROR HANDLING
  // -----------------------------------------------

  @Test
  public void testUnzipHandlesZeroArguments() {
    JMUnzip.main(new String[] {});
    assertProducesErrorMessage();
  }

  @Test
  public void testUnzipHandlesOneMissingArgument() {
    JMUnzip.main(new String[] {"file.txt"});
    assertProducesErrorMessage();
  }

  @Test
  public void testUnzipHandlesUnreadableFile() {
    JMUnzip.main(new String[] {"BLAfjdlSFl.txt", "out.jmz"});
    assertProducesErrorMessage();
  }

  @Test
  public void testUnzipHandlesWrongFileFormat() {
    JMUnzip.main(new String[] {"mary.txt", "blah.txt"});
    assertProducesErrorMessage();
  }

//  // -----------------------------------------------
//  // TESTS FOR FILE CREATION by JMZip
//  // -----------------------------------------------
//
//  @Test
//  public void testJMZipCreatesFile() throws IOException {
//    JMZip.main(new String[] {"mary.txt", "flubber.jmz"});
//
//    File file = new File("flubber.jmz");
//    assertTrue(file.exists());
//  }
//
//  // -----------------------------------------------
//  // TESTS FOR CORRECT FREQUENCIES in files created by JMZip
//  // -----------------------------------------------
//
//  private HuffmanSave loadSaved(String name) throws IOException, ClassNotFoundException {
//    FileInputStream fileIn = new FileInputStream(new File(name));
//    ObjectInputStream in;
//    in = new ObjectInputStream(fileIn);
//    HuffmanSave result = (HuffmanSave) in.readObject();
//    in.close();
//    fileIn.close();
//    return result;
//  }
//
//  @Test
//  public void testOneByteFileCorrectFrequencies() throws ClassNotFoundException, IOException {
//    JMZip.main(new String[] {"one_byte.dat", "one_byte.jmz"});
//
//    HuffmanSave result = loadSaved("one_byte.jmz");
//    assertEquals(1, result.getFrequencies().size());
//    assertEquals(1, (int) result.getFrequencies().get((byte) 42));
//  }
//
//  @Test
//  public void testMultiByteFileCorrectFrequencies() throws ClassNotFoundException, IOException {
//    JMZip.main(new String[] {"bytes.dat", "bytes.jmz"});
//
//    HuffmanSave result = loadSaved("bytes.jmz");
//    assertEquals(100, result.getFrequencies().size());
//    for (int i = 0; i < 100; i++) {
//      assertEquals(i + 10, (int) result.getFrequencies().get((byte) i));
//    }
//  }
//
//  // -----------------------------------------------
//  // TESTS FOR CORRECT ENCODING LENGTH
//  // -----------------------------------------------
//
//  @Test
//  public void testEmptyFileCorrectEncodingLength() throws ClassNotFoundException, IOException {
//
//    HuffmanSave result = loadSaved("empty.jmz");
//    assertEquals(0, result.getEncoding().length());
//  }
//
//  @Test
//  public void testOneByteFileCorrectEncodingLength() throws ClassNotFoundException, IOException {
//    JMZip.main(new String[] {"one_byte.dat", "one_byte.jmz"});
//
//    HuffmanSave result = loadSaved("one_byte.jmz");
//    assertEquals(1, result.getEncoding().length());
//  }
//
//  @Test
//  public void testTextFileCorrectEncodingLength() throws ClassNotFoundException, IOException {
//    JMZip.main(new String[] {"mary.txt", "mary.jmz"});
//
//    HuffmanSave result = loadSaved("mary.jmz");
//    assertEquals(227, result.getEncoding().length());
//  }
//
//  @Test
//  public void testFibonacciFileCorrectEncodingLength() throws ClassNotFoundException, IOException {
//    JMZip.main(new String[] {"fibonacci.dat", "fibonacci.jmz"});
//
//    HuffmanSave result = loadSaved("fibonacci.jmz");
//    assertEquals(7147, result.getEncoding().length());
//  }
//
//  @Test
//  public void testByteFileCorrectEncodingLength() throws ClassNotFoundException, IOException {
//    JMZip.main(new String[] {"bytes.dat", "bytes.jmz"});
//
//    HuffmanSave result = loadSaved("bytes.jmz");
//    assertEquals(38557, result.getEncoding().length());
//  }

  // -----------------------------------------------
  // TESTS FOR CORRECT RECONSTRUCTION
  // -----------------------------------------------

  private void checkReconstruction(String name) throws IOException {
    JMZip.main(new String[] {name, "bytes.jmz"});
    JMUnzip.main(new String[] {"bytes.jmz", "bytes_restored.dat"});
    Path path1 = FileSystems.getDefault().getPath(name);
    Path path2 = FileSystems.getDefault().getPath("bytes_restored.dat");
    byte[] bytes1 = Files.readAllBytes(path1);
    byte[] bytes2 = Files.readAllBytes(path2);
    org.junit.Assert.assertArrayEquals(bytes1, bytes2);
  }

  @Test
  public void testEmptyFileCorrectlyRestored() throws IOException {
    checkReconstruction("empty.dat");
  }

  @Test
  public void testOneByteFileCorrectlyRestored() throws IOException {
    checkReconstruction("one_byte.dat");
  }

  @Test
  public void testTextFileCorrectlyRestored() throws IOException {
    checkReconstruction("mary.txt");
  }

  @Test
  public void testFibonacciFileCorrectlyRestored() throws IOException {
    checkReconstruction("fibonacci.dat");
  }

  @Test
  public void testByteFileCorrectlyRestored() throws IOException {
    checkReconstruction("bytes.dat");
  }
  
}
