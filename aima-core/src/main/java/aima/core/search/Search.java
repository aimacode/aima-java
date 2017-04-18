/**
 *Tried to Implement Fig 11.5 Hierarchical-Search
 * @author Subham Mishra
 * @version 1.0
 * @since 12.03.2016
 * @see https://github.com/aimacode/aima-java/issues/35
 * 
 */
package aima.core.search;
import java.io.*;
public class Search {
	static String[] path={null,null,null,null};
	static int open[]={0,0,0,0};
	public static void main(String args[])
	{
	  String str;
	  int next_step=0;
		for(;;)
         { try
           {
			BufferedReader br = new BufferedReader(new
					InputStreamReader(System.in));
			     str=br.readLine(); //Taking input from user
			     try
			     {
			    	 next_step=Integer.parseInt(str); //Channging string input to Integer typr
			     }
			     catch (NumberFormatException e)
			     {
			    	 System.out.println("Invalid Number"+e);
			    	 next_step=0;
			     }
			  }
         catch(IOException e)
           {
        	 System.out.println(e);
           }
         checkStep(next_step);
         if(checkIfGoal()==0)
         {
        	 printPath();
             break;
         }
         }
      }
	/**
	 * This function checks validity of user input 
	 * and displays appropriate message
	 * @param i : used to receive input from user and check its validity
	 */
  static void checkStep(int i)
  {
	if(i==1)
	{
	if(open[0]==1)
		System.out.println("Already added");
	else
	{
		path[0]=new String("Left Sock");
				open[0]=1;
	}
	}
	if(i==2)
	{
		if(open[1]==1)	
			System.out.println("Already added");
		else
		{
			path[1]=new String("Right Sock");
					open[1]=1;
		}
		
	}
	if(i==3)
	{
		if(open[0]==0)	
			System.out.println("Illegal Add Left Sock First");
		else if(open[2]==1)	
			System.out.println("Already added");
		else
		{
			path[2]=new String("Left Shoe");
					open[2]=1;
		}
	}
	if(i==4)
	{
		if(open[1]==0)	
			System.out.println("Illegal Add Left Sock First");
		else if(open[3]==1)	
			System.out.println("Already added");
		else
		{
			path[3]=new String("Right Shoe");
					open[3]=1;
		}
	}
  }
  /**
   * used to check if we have already reached goal state
   * @return 0:if goal state is already reached 
   *        -1: Otherwise
   */
  static int checkIfGoal()
  {
		if(open[0]==1 && open[1]==1 && open[2]==1 && open[3]==1)
			return 0;
		return -1;
	}
  /**
   * This function prints apth after goal state is reached
   */
  static void printPath()
  {
  	for(int i=0;i<4;i++)
  	System.out.println(path[i]);
  }
  /**
   * 
   * @param i
   * @return 
   */
static String printPath(int i)
{
	return path[i];
}

}
