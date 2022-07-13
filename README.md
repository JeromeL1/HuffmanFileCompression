# HuffmanFileCompression

## BitSequence.java
this class represents an arbitrarily long sequence of bits. You will use this class to represent the Huffman-encoded version of the input file.

## HuffmanSave.java
container class that stores a BitSequence along with a HashMap of frequency data. Recall that decoding the bit sequence created during the Huffman coding process requires us to have access to the Huffman tree that was used to create the code. A HuffmanSave object stores all of the information necessary to reconstruct a compressed file. The frequency data can be used to rebuild the Huffman tree and the BitSequence stores the encoded data.

## BuildHuffTree.java
This builds a priorityqueue of hufftree. The file is first converted to a array of bytes, then it is organized into a hashmap of frequency of each unique bytes. It then builds a queue of tree if the size of freq map is more than 1, otherwise one hufftree object is constructed. The queue of tree of is then used to build one hufftree with the byte and its freq considered. This class also includes the utility to encode hufftree into a bitsequence class or decode decode bitsequence into byte array. 

## HuffmanSave.java
This stores the bitseuqnece and the freq map.

## JMZip/JMUnzip

zip and unzip drivers
