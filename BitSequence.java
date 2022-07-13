import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The BitSequence class stores an arbitrary sequence of bits. The underlying
 * data structure is a dynamic array of primitive bytes. This collection
 * supports append operations and bit-level modification, but it does not
 * support removal.
 * 
 * <p>This class is intended to support efficient serialization. The storage array
 * will be trimmed down to the smallest possible size before the object is
 * serialized.
 * 
 * @author Nathan Sprague
 * @version V1.1 12/6/16
 *
 */
public class BitSequence implements Iterable<Integer>, Serializable {


  private static final long serialVersionUID = 1L;

  private int numBits;
  private byte[] bytes;

  /**
   * Construct an empty bit sequence.
   */
  public BitSequence() {
    numBits = 0;
    bytes = new byte[1];
  }

  /**
   * Copy constructor.
   */
  public BitSequence(BitSequence bits) {
    this();
    this.appendBits(bits);
  }

  /**
   * Return the number of bits in this sequence.
   */
  public int length() {
    return numBits;
  }

  /**
   * Append a bit to the end of this sequence. 0 will be interpreted as the bit
   * 0, all other values will be interpreted as the bit 1.
   */
  public void appendBit(int bit) {
    if (numBits / 8 == bytes.length) {
      resize();
    }

    if (bit != 0) {
      int whichByte = numBits / 8;
      byte bitmask = (byte) (1 << numBits % 8);
      bytes[whichByte] |= bitmask;
    }

    numBits++;
  }

  /**
   * Private helper method for doubling the size of the byte array when it
   * becomes full.
   */
  private void resize() {
    byte[] newBytes = new byte[bytes.length * 2];
    for (int i = 0; i < bytes.length; i++) {
      newBytes[i] = bytes[i];
    }
    bytes = newBytes;
  }

  /**
   * Append a sequence of bits represented as a string. For example if the
   * argument is "001" the bits 001 will be appended to the sequence.
   */
  public void appendBits(String bits) {
    for (int i = 0; i < bits.length(); i++) {
      if (bits.charAt(i) == '0') {
        appendBit(0);
      } else {
        appendBit(1);
      }
    }
  }

  /**
   * Append the provided bit sequence to the end of this sequence.
   */
  public void appendBits(BitSequence bits) {
    for (Integer bit : bits) {
      appendBit(bit);
    }
  }

  /**
   * Set the value of a single bit at a specified index. 0 will be interpreted
   * as the bit 0, all other values will be interpreted as the bit 1.
   */
  public void setBit(int index, int bit) {
    if (index < 0 || index >= numBits) {
      throw new IndexOutOfBoundsException();
    }

    int whichByte = index / 8;
    byte bitmask = (byte) (1 << index % 8);
    if (bit != 0) {
      bytes[whichByte] |= bitmask;
    } else {
      bytes[whichByte] &= ~bitmask;
    }
  }

  /**
   * Get the value of a single bit at a specified index.
   */
  public int getBit(int index) {
    if (index < 0 || index >= numBits) {
      throw new IndexOutOfBoundsException();
    }
    int whichByte = index / 8;
    byte bitmask = (byte) (1 << index % 8);
    return (bitmask & bytes[whichByte]) == 0 ? 0 : 1;
  }



  @Override
  public boolean equals(Object other) {
    if (!(other instanceof BitSequence)) {
      return false;
    }
    BitSequence otherSeq = (BitSequence) other;

    if (otherSeq.length() != length()) {
      return false;
    }

    // Compare each byte. It's fine to compare the partial bytes at the
    // end because they are guaranteed to be all 0's past the last valid
    // index.
    for (int i = 0; i < length() / 8 + 1; i++) {
      if (bytes[i] != otherSeq.bytes[i]) {
        return false;
      }
    }

    return true;
  }

  /**
   * The hashcode for a BitSequence is constructed by xoring each subsequent
   * byte at a rotating offset.
   */
  @Override
  public int hashCode() {
    int result = 0;

    for (int i = 0; i < length() / 8 + 1; i++) {
      result ^= bytes[i] << (i % 4) * 8;
    }
    return result;
  }

  /**
   * Return a string representation of this bit sequence. For example the bit
   * sequence 001 will result in the string "001".
   */
  @Override
  public String toString() {

    String result = "";
    for (int bit : this) {
      result += bit;
    }
    return result;
  }

  @Override
  public Iterator<Integer> iterator() {
    return new BitIterator();
  }

  private class BitIterator implements Iterator<Integer> {

    int index = 0;

    @Override
    public boolean hasNext() {
      return index < numBits;
    }

    @Override
    public Integer next() {
      if (hasNext()) {
        int result = getBit(index);
        index++;
        return result;
      } else {
        throw new NoSuchElementException();
      }

    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }

  }

  /**
   * Resize the array so that there are no unused entries.
   */
  private void trim() {
    byte[] newBytes = new byte[(numBits + 7) / 8];
    for (int i = 0; i < newBytes.length; i++) {
      newBytes[i] = bytes[i];
    }
    bytes = newBytes;
  }

  /**
   * This method overrides the default writeObject method to make sure that the
   * array is trimmed before serialization.
   * 
   * @throws IOException If there is a problem writing to the provided output
   *         stream.
   */
  private void writeObject(java.io.ObjectOutputStream out)
      throws IOException {
    trim();
    out.defaultWriteObject();
  }

}
