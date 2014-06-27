package ru.shadam.ftlmapper.domain;

/**
 * @author sala
 */
public class Tuple4<T1, T2, T3, T4> extends Tuple3<T1, T2, T3> {
    protected final T4 fourth;

    public Tuple4(T1 first, T2 second, T3 third, T4 fourth) {
        super(first, second, third);
        this.fourth = fourth;
    }

    public T4 getFourth() {
        return fourth;
    }


    @Override
    public String toString() {
        return "Tuple4{" +
                "first=" + first +
                "second=" + second +
                "third=" + third +
                "fourth=" + fourth +
                '}';
    }
}
