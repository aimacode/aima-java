package aima.core.search.nondeterministic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class store problem
 * This is taken is a separate class to bring in clarity in code and
 * easily modify problem if required.
 * @author Subham Mishra
 * @since 14th march 2016
 */
public class Problem 
{
	public static String goal,initialState; 
	public static void input(String s,String str)
	{
		  initialState=s;
		  goal=str;
    }
}
