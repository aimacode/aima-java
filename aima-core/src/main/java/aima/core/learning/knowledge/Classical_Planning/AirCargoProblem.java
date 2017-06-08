package aima.core.learning.knowledge.Classical_Planning;

import java.io.*;
import java.util.*;
public class AirCargoProblem {
	public static Cargo Load(Cargo c,Plane p,Airport a){
		if(!(at(c,a) && at(p,a)))
			return null;
		notAt(c,a);
		In(c,p);
		return c;
	}       
    
	public static Cargo Unload(Cargo c,Plane p,Airport a){
		if(!(in(c,p) && at(p,a)))
			return null;
		At(c,a);
		notIn(c,p);
		return c;
	}
    
	public static Plane Fly(Plane p,Airport from,Airport to){
		if(!at(p,from))
			return null;
		notAt(p,from);
		At(p,to);
		return p;
	}
    

	public static void At(Plane p,Airport a){
		p.airport = a;
	}
	public static void At(Cargo c,Airport a){
		c.loaded = false;
		c.airport = a;
	}
	public static void In(Cargo c,Plane p){
		c.loaded = true;
		c.plane = p;
	}
	public static void notIn(Cargo c,Plane p){
		c.loaded = false;
		c.plane = null;
	}
	public static void notAt(Cargo c,Airport a){
		c.loaded = true;
		c.airport = null;
	}
	public static void notAt(Plane p,Airport a){
		if(a.equals(p.airport))
            p.airport = null;
	}
	public static boolean at(Cargo c,Airport a){
		if(c.loaded == false  && a.equals(c.airport))
				return true;
		else return false;
	}
	public static boolean at(Plane p,Airport a){
		if(a.equals(p.airport))
			return true;
		else return false;
	}
	public static boolean in(Cargo c,Plane p){
		if(c.loaded == true && p.equals(c.plane))
			return true;
		else return false;
	}
	public static void main(String[] args){
		Cargo C1 = new Cargo("C1");
		Cargo C2 = new Cargo("C2");
		Plane P1 = new Plane("P1");
		Plane P2 = new Plane("P2");
		Airport JFK = new Airport("JFK");
		Airport SFO = new Airport("SFO");
		At(C1,SFO);
		At(C2,JFK);
		At(P1,SFO);
		At(P2,JFK);
		C1 = Load(C1,P1,SFO);
		P1 = Fly(P1,SFO,JFK);       
		C2 = Load(C2,P2,JFK);
		P2 = Fly(P2,JFK,SFO);
		C2 = Unload(C2,P2,SFO);
		C1 = Unload(C1,P1,JFK);
		System.out.println(at(C1,JFK)+" "+at(C2,SFO));  
	}
}
class Airport{
	String name;
    Airport(String s){
    	this.name = s;
    }
    public boolean equals(Airport a){
    	return (this.name).equals(a.name);
    }
}
class Cargo{
	String name;
	boolean loaded;
	Airport airport;
	Plane plane;
	Cargo(String s){
		this.name = s;
		this.loaded = false;
	}
}
class Plane{
	String name;
	Airport airport;
	Plane(String p){
		this.name = p;
	}
	public boolean equals(Plane a){
    	return (this.name).equals(a.name) && (this.airport).equals(a.airport);
    }
}