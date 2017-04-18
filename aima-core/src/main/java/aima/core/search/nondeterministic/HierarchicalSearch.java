/*
AIMA Java
Figure 11.5 - Hierarchical Search
 The algorithm takes the input from a file called problem.txt as an hierarchical
 problem object class has not been created.
 This can be later on implemented.
Written by Shubhankar Mohapatra
*/

package aima.core.search.nondeterministic;
import java.io.*;
class problem //class for storing the problem
{
	String goal;
	String initial;
	void input()throws IOException
	{
		FileReader f=new FileReader("problem.txt");
		BufferedReader br=new BufferedReader(f);
		
		goal=br.readLine();
		initial=br.readLine();

	}

}

class HierarchicalSearch
{
	public static String frontier[]=new String[100]; //storing the HLAs
	static int front=-1;
	public static String output[]=new String[1000];
	static int out=0;
	public static FileReader f=null;
	public static BufferedReader br=null;

	
	static String pop()throws IOException
	
	{
		//Function to pop HLA not considering the null HLAs
		int k=front; 
		while(frontier[k].equalsIgnoreCase("null"))
		k--;
		return frontier[k];
	}
	static void push(String st)throws IOException
	{
		//Function to push HLA
		frontier[++front]=st;

	}
	static void hierarchical(problem p)throws IOException
	
	{
	
		

		//Hierrachical Search

		String plan=pop();
		String we=hla(plan); //Calling the HLA
		if(we=="")
			{
				print();
				System.exit(0);
			}

		String prefix=action(we,"before");
		String suffix=action(we,"after");
		push(suffix);
		push(we);

		String outcome=result(p.initial,prefix); //Outcome 
		if(outcome.equalsIgnoreCase(p.goal))
			print();
		else
		{
			while(true)
			{
                String inp=br.readLine();
				
				push(inp);
				if(inp.equalsIgnoreCase("null"))
						break;
			}
			hierarchical(p);
		}	



	}
	static String result(String x,String y)throws IOException
	{
		
		String k=br.readLine();
		if(k.equalsIgnoreCase("null"))
			return "";
		else
			return k;

	}


	static String hla(String s)throws IOException
	{
		
		String k=br.readLine();
		if(k.equalsIgnoreCase("null"))
			return "";
		else
			return k;

	}
	static String action(String p,String l)throws IOException
	{
		
		String k=br.readLine();
		return k;
	}

	public static void main(String args[])throws IOException, FileNotFoundException
	
	{
		problem p=new problem();
		p.input();
		f=new FileReader("input.txt");
		br=new BufferedReader(f);
		push(p.goal);
		hierarchical(p);
	}

	static void print()throws IOException
	{ 
		//Function to print the HLAs in reverse order
		
		for(int i=front;i>=0;i--)
		{	if(frontier[i].equalsIgnoreCase("null")) //Not printing the null HLAs
			{}
			else	
			output[out++]=frontier[i];
	}
	}


}