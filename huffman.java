/**
 * Huffman encoding
 */
package codes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

abstract class HuffmanTree implements Comparable<HuffmanTree> {
    public final int frequency; // the frequency of this tree
    public HuffmanTree(int freq) { frequency = freq; }
 
    // compares on the frequency
    public int compareTo(HuffmanTree tree) {
        return frequency - tree.frequency;
    }
}
 
class HuffmanLeaf extends HuffmanTree {
    public final char value; // the character this leaf represents
 
    public HuffmanLeaf(int freq, char val) {
        super(freq);
        value = val;
    }
}
 
class HuffmanNode extends HuffmanTree {
    public final HuffmanTree left, right; // subtrees
 
    public HuffmanNode(HuffmanTree l, HuffmanTree r) {
        super(l.frequency + r.frequency);
        left = l;
        right = r;
    }
}
 
public class huffman {
    
	static Hashtable <Character,String>finalData = 
            new Hashtable<Character,String>();
	static Hashtable <Character,Integer>freqData = 
            new Hashtable<Character,Integer>();
	static double inputlen;
    public static HuffmanTree buildTree(int[] charFreqs) {
        PriorityQueue<HuffmanTree> trees = new PriorityQueue<HuffmanTree>();
       
        for (int i = 0; i < charFreqs.length; i++)
            if (charFreqs[i] > 0)
                trees.offer(new HuffmanLeaf(charFreqs[i], (char)i));
 
        assert trees.size() > 0;
        // loop until there is only one tree left
        while (trees.size() > 1) {
            
            HuffmanTree a = trees.poll();
            HuffmanTree b = trees.poll();

            // put into new node and re-insert into queue
            trees.offer(new HuffmanNode(a, b));
        }
        return trees.poll();
    }
 
    public static void printCodes(HuffmanTree tree, StringBuffer prefix) {
        assert tree != null;
        if (tree instanceof HuffmanLeaf) {
            HuffmanLeaf leaf = (HuffmanLeaf)tree;
 
            // print character, frequency, and code 
            System.out.println(leaf.value + "\t" + leaf.frequency + "\t" + prefix);
            finalData.put(leaf.value, prefix.toString());
            freqData.put(leaf.value,  leaf.frequency);
 
        } else if (tree instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode)tree;
 
            // traverse left and append 0
            prefix.append('0');
            printCodes(node.left, prefix);
            prefix.deleteCharAt(prefix.length()-1);
 
            // traverse right and append 1
            prefix.append('1');
            printCodes(node.right, prefix);
            prefix.deleteCharAt(prefix.length()-1);
        }
    }
 
    public static void main(String[] args) throws FileNotFoundException {
    	String test = new Scanner(new File("C:/Users/Thara Philips/Documents/thara_docs/Rutgers/Datastructures/Project/dna.txt")).useDelimiter("\\A").next(); 
        inputlen=test.length()*8;
    	System.out.println("Size of input text= "+inputlen);
 
        
        int[] charFreqs = new int[50000];
        // read each character and record the frequencies
        
        for (char c : test.toCharArray())
            charFreqs[c]++;
        
        // build tree
        long startTime = System.nanoTime();
        HuffmanTree tree = buildTree(charFreqs); 
        long endTime = System.nanoTime();
		
        // print out results
        System.out.println("SYMBOL\tWEIGHT\tHUFFMAN CODE");
        long startTime1 = System.nanoTime();
        printCodes(tree, new StringBuffer());
        System.out.println(finalData);
        	  
        binarycode(test);
        long endTime1 = System.nanoTime();
        System.out.println("\nTime taken for building tree "+(endTime - startTime) + " ns");
		System.out.println("\nTime taken for encoding "+(endTime1 - startTime1) + " ns");	
    }
    static void binarycode(String test){
    	StringBuffer tempEncoding = new StringBuffer();
    	for(int i=0;i<test.length();i++){
    		char search=test.charAt(i);
    		tempEncoding.append(finalData.get(search));
    	}
    	System.out.println("Encoded String");
    	//System.out.println(tempEncoding);
    	System.out.println("Size of encoded data is:"+ tempEncoding.length());
    	double compressionratio=inputlen/tempEncoding.length();
    	System.out.println("Compression Ratio is: "+ compressionratio);
    	System.out.println("Percentage of compression: "+((inputlen-tempEncoding.length())/inputlen)*100);
    	entropy();
    }
    
    public static void entropy(){
    	int frequency=0;
    	double entropy=0;
    	double entropytemp=0;
    	double Lavg_final=0; 
    	//System.out.println(freqData.size());
    	Set<Character> keys = freqData.keySet();
        for(Character key: keys){
            frequency+=freqData.get(key);            
        }  
        for(Character key: keys){  
        	//System.out.println(key);
        	double freq=freqData.get(key);
        //	System.out.println(key);
            entropytemp=freq/frequency ; //probability
           // System.out.println(entropytemp);
            Lavg_final+=lengthavg(entropytemp,key);
            
            double infocontent=Math.log(1/entropytemp);
           // System.out.println("Character " + key+" Information content "+ infocontent);
            //System.out.println("entropytemp"+entropytemp);  
            entropy+=(entropytemp*Math.log(1/entropytemp));
//            System.out.println("Entropy"+entropy );
        }        
        System.out.println("Total freq"+ frequency);
        System.out.println("Entropy "+entropy );
        System.out.println("Average length "+ Lavg_final);
    }
    public static double lengthavg(double probability,Character key ){
    	double Lavg=0;    	
    	String len=finalData.get(key);
    	Lavg=len.length()*probability;
    	System.out.println("Lavg"+ Lavg);
    	return Lavg;
    }
}