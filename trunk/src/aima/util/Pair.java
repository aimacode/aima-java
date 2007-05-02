package aima.util;

public class Pair<X, Y> {
    private final X a;

    private final Y b;

    public Pair(X a, Y b) {
	this.a = a;
	this.b = b;
    }

    public X getFirst() {
	return a;
    }

    public Y getSecond() {
	return b;
    }

    public boolean equals(Object o) {
    	
    Pair<X, Y> p =  (Pair<X, Y>)o;
	return a.equals(p.a) && b.equals(p.b);
    }

    public int hashCode() {
	return a.hashCode() + 31 * b.hashCode();
    }
    
    public String toString(){
    	return "< "+getFirst().toString()+ " , " + getSecond().toString()+" > ";
    }

}
