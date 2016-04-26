

// Created by 
// Nirali Shah
// 04/08/2016


import java.io.BufferedReader;
import java.math.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;


//define a class Node
class ShannonNode
{
	static String text;
	static int initiallength;
	static int finallength;
	//static int n;
	static HashMap <Character,Integer>frequencyData = 
            new HashMap<Character,Integer>();
    char sym;
    float pro;  // pro= probability of each symbol
    //int[] arr;
    int top=-1;    //top= the code size of each symbol
    static int length;
    int codelength;

    
    // Node Constructor
public ShannonNode()
{
	sym=' ';
	//arr=new int[length/8+2];
}

//scan the file and figure out the frequency of the various symbols.
static void createFreqData(){
    for(int cnt = 0;cnt < text.length();cnt++){
      char key = text.charAt(cnt);
      //System.out.print(key);
      if(frequencyData.containsKey(key)){
        int value = frequencyData.get(key);
        value += 1;
        //System.out.print(value);
        frequencyData.put(key,value);
      }else{
        frequencyData.put(key,1);  // key is the symbol and value is its frequency
        //System.out.print("1\n");
      }
      //end else
    }//end for loop
  }//end createFreqData
  //-----------------------------------------------------//
  
static int [] shannon(int l,int h,ShannonNode s[],int index, int a[])
{
    float pack1=0,pack2=0,diff1=0,diff2=0;
	 //double pack1=0,pack2=0,diff1=0,diff2=0;
	
    int i,k=0,j;
    if((index+1)==h || index==h || index>h)
    {
        if(index==h )
        {
        a[++(s[index].top)]=0;
        return a;
        }
        else if( index>h)
        {
        	return a;
        }
        else
        {
        //s[h].arr[++(s[h].top)]=0;
        a[++(s[index].top)]=1;
        return a;
        }
    }
    else
    {								
        for(i=l;i<=h-1;i++)
        pack1=pack1+s[i].pro;		//permutations are done to get the groups         								
        pack2=pack2+s[h].pro;		//having the sum of their probabilities almost equal
        diff1=pack1-pack2;
        if(diff1< 0)
        diff1=diff1*-1;
        j=2;
        while(j!=h-l+1)
        {
            k=h-j;
            pack1=pack2=0;
            for(i=l;i<=k;i++)
            pack1=pack1+s[i].pro;
            for(i=h;i>k;i--)
            pack2=pack2+s[i].pro;
            diff2=pack1-pack2;
            if(diff2< 0)
            diff2=diff2*-1;
            if(diff2>=diff1)
            break;
            diff1=diff2;
            j++;
            
            //cout<<"k="<<k;
        }
        k++;
        //cout<<"k="<<k;
       if(index<=k)						//once the group is found '0s' are 
       {								//assigned to one group and '1s' are assigned to the other
        for(i=l;i<=k;i++)
        a[++(s[i].top)]=1;
        shannon(l,k,s,index,a);
       }
       else
       {
        for(i=k+1;i<=h;i++)
        a[++(s[i].top)]=0;
        shannon(k+1,h,s,index,a);
       }
    }
   return a;
}

//main method starts
public static void main(String[] args)throws NumberFormatException, IOException
{
	float avgcodewordlen=0;
	//long startTime = System.currentTimeMillis();
	long start1 = System.nanoTime();
    int i,j;
    float x,total=0;
    String ch;
    ShannonNode temp = new ShannonNode();
    System.out.println("Enter the CSV file name where the datasets are");
	Scanner in= new Scanner(System.in);  //input from the file
	String filename=in.nextLine(); 
    text = new Scanner(new File(filename)).useDelimiter("\\A").next(); 
    System.out.println("Raw Data: ");
   // System.out.println("text="+text);
   // System.out.println("actual text.length()="+text.length());
    length=text.length()*8;					// length of the file in bits
    System.out.println("length of text"+length);
    initiallength=length;
    ShannonNode[] s=new ShannonNode[length+2];
	 
	 for(int m=0;m<length+2;m++)
	 {
		 s[m]=new ShannonNode();
	 }
	 int index=0;
    createFreqData();  // calculate frequency of symbols
    
    Iterator iterator = frequencyData.keySet().iterator();

    for (Entry<Character, Integer> entry : frequencyData.entrySet()) {
    	  char key = entry.getKey();
    	  s[index].sym=key;
    	  int value =entry.getValue();
    	  s[index].pro=(float)((float)value/(length/8));
    	 // s[index].pro=value;
    	  total=total+s[index].pro;
    	  System.out.println(key + " " + value);
    	// System.out.println(s[index].sym + " " + s[index].pro);
    	  // do stuff
    	  index++;
    	}
    System.out.println("total"+total);
	 //System.out.println("index="+index);
    s[index].pro=1-total;
    //System.out.println("s[index].pro"+s[index].pro);
    //s[index].pro=length-total;
    for(j=1;j<=index-1;j++)
    {
        for(i=0;i< index-1;i++)
        {
            if((s[i].pro)>(s[i+1].pro))
            {								// sort the symbols according to the probabilities
                temp.pro=s[i].pro;
                temp.sym=s[i].sym;           
                s[i].pro=s[i+1].pro;
                s[i].sym=s[i+1].sym;
                s[i+1].pro=temp.pro;
                s[i+1].sym=temp.sym;
            }
        }
    }
//    for(i=0;i<index;i++)
//    s[i].top=-1;
   
//   shannon(0,index-1,s);
    //shannon(0,(s.length)-1,s);
    long end1 = System.nanoTime();
    long nanoseconds = (end1 - start1);
    System.out.println("Constant Time ="+nanoseconds);
    
    long start2 = System.nanoTime();
    System.out.println("---------------------------------------------------------------");
    System.out.println("\n\n\n\tSymbol\tProbability\tCode Size\tCode\tEntropy");
    int count;
    for(i=index-1;i>=0;i--)
    {
    	s[i].top=-1;
    	//System.out.println("s[i].top="+s[i].top);
    	count=0;
    	 int arr[]=new int[length/8];
        System.out.print("\n\n\n\t"+s[i].sym+"\t"+s[i].pro+"\t");
        int result[]=shannon(0,index-1,s,i,arr);
        System.out.print((s[i].top+1)+"\t");
       // finallength=finallength+(s[i].top*frequencyData.get(s[i].sym));
        //System.out.println(frequencyData.get(s[i].sym));
        for(j=0;j<=s[i].top;j++)
        {   	
        System.out.print(result[j]);
        	count++;
        }
        System.out.print("\t"+(-Math.log(s[i].pro)));
        int valueofkey=frequencyData.get(s[i].sym);
        finallength=finallength+(s[i].top+1)*(valueofkey);
        avgcodewordlen=avgcodewordlen+((s[i].top+1)*s[i].pro);
     
    }
   // int valueofa=frequencyData.get(s[i].sym);
    //System.out.println("Value of 'a' key="+valueofa);
    System.out.println("\n---------------------------------------------------------------");
  // System.out.println("final length="+finallength);
//   finallength=count;
   
   
    System.out.println("Average Code Word Length="+avgcodewordlen);
   System.out.println("finallength="+finallength);
   System.out.println("Compression Ratio="+(float)initiallength/finallength);
   double entropy=0;
   for(int k=0;k<index;k++)
   {
	   entropy=entropy+ (s[k].pro*(Math.log(1/s[k].pro)));
   }
   System.out.println("Entropy="+(entropy)+"On average there are these many bits of info in each code. Also called efiiciency of code");
   double sumavcodwordlen=0;
//   for(int k=0;k<index;k++)
//   {
//	   sumavcodwordlen=sumavcodwordlen+ ((s[k].top+1)*s[k].pro);
//   }
   //System.out.println("sumavcodwordlen"+sumavcodwordlen);
   //double avgcodewrdlen=sumavcodwordlen/index;
  // System.out.println("Average Code Word Length="+avgcodewrdlen);
   System.out.println("Efficiency of code="+(entropy)/(avgcodewordlen));
   System.out.println("Compression Percentage="+((float)(initiallength-finallength)/initiallength)*100);
   
   long end2 = System.nanoTime();
   long nano = (end2 - start2) ;
   System.out.println("Encode Time ="+nano);
}
}
