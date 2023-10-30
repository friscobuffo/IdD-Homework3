package ingegneria_dei_dati.utils;

public class Triple<A,B,C> {
    public A first;
    public B second;
    public C third;

    @Override
    public String toString() {
        return first.toString() + "\n" + second.toString() + "\n" + third.toString();
    }
}
