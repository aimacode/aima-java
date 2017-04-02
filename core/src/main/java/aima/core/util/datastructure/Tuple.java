package aima.core.util.datastructure;

/**
 * Created by @AdrianBZG on 2/04/17.
 */

import java.util.function.Function;

/**
 * Util class: a parametric tuple type
 */
public class Tuple<A, B> {
    private A fst;
    private B snd;

    public Tuple(A fst, B snd) {
        this.fst = fst;
        this.snd = snd;
    }

    public A getWidth() {
        return fst;
    }

    public B getHeight() {
        return snd;
    }

    public A getX() {
        return fst;
    }

    public B getY() {
        return snd;
    }

    public A getFst() {
        return fst;
    }

    public void setFst(A fst) {
        this.fst = fst;
    }

    public B getSnd() {
        return snd;
    }

    public void setSnd(B snd) {
        this.snd = snd;
    }

    public void chgFst(Function<A, A> fun) {
        fst = fun.apply(getFst());
    }

    public void chgSnd(Function<B, B> fun) {
        snd = fun.apply(getSnd());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple<?, ?> tuple = (Tuple<?, ?>) o;

        return tuple.getX() == this.getX() && tuple.getY() == this.getY();
    }

    @Override
    public int hashCode() {
        int result = fst != null ? fst.hashCode() : 0;
        result = 31 * result + (snd != null ? snd.hashCode() : 0);
        return result;
    }

    @Override
    public String toString () {
        return getX() + ", " + getY();
    }
}
