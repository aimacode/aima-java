package aima.core.search.nondeterministic;

/*
AIMA Java
Figure 11.5 - Hierarchical Search
The algorithm takes the input from a file called problem.txt as an hierarchical
problem object class has not been created.
This can be later on implemented.
Written by Shubhankar Mohapatra
*/


import java.io.*;
class problem //class for storing the problem
{
	String goal;
	String initial;
	void input()throws IOException
	{
		FileReader f=new FileReader("src/main/resources/aima/core/search/problem.txt");
		BufferedReader br=new BufferedReader(f);
		//System.out.println("Enter goal and initial state");
		goal=br.readLine();
		initial=br.readLine();
		br.close();

	}

}

public class HierarchicalSearch
{
	public static String frontier[]=new String[100]; //storing the HLAs
	static int front=-1;
	public String output[]=new String[10];
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
	 void hierarchical(problem p)throws IOException
	
	{
	
		

		//Hierrachical Search

		String plan=pop();
		String we=hla(plan); //Calling the HLA
		if(we=="")
			{
				print();
				//System.exit(0);
				return;
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
				//System.out.println("Enter refinement for "+outcome+" and "+we);
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
		//System.out.println("Enter result of "+x+" and "+y);
		String k=br.readLine();
		if(k.equalsIgnoreCase("null"))
			return "";
		else
			return k;

	}


	static String hla(String s)throws IOException
	{
		//System.out.println("Enter HLA in plan "+s);
		String k=br.readLine();
		if(k.equalsIgnoreCase("null"))
			return "";
		else
			return k;

	}
	static String action(String p,String l)throws IOException
	{
		//System.out.println("Enter action " +l+ " "+p);
		String k=br.readLine();
		return k;
	}

	public void set()throws IOException
	{
		problem p=new problem();
		p.input();
		f=new FileReader("src/main/resources/aima/core/search/input.txt");
		br=new BufferedReader(f);
		push(p.goal);
		hierarchical(p);
	}
	
	void print()throws IOException
	{ 
		//Function to print the HLAs in reverse order
		
		
		for(int p=0;p<10;p++)
			output[p]="";
		for(int i=front;i>=0;i--)
		{	if(frontier[i].equalsIgnoreCase("null")) //Not printing the null HLAs
			{}
			else	
			output[out++]=frontier[i];
	}
	}


}