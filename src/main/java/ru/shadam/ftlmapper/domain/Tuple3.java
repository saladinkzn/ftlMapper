package ru.shadam.ftlmapper.domain;

/**
 * @author sala
 */
public class Tuple3<T1, T2, T3> extends Tuple2<T1, T2> {
    protected T3 third;

    public Tuple3(T1 first, T2 second, T3 third) {
        super(first, second);
        this.third = third;
    }

    public T3 getThird() {
        return third;
    }


    @Override
    public String toString() {
        return "Tuple3{" +
                "first=" + first +
                "second=" + second +
                "third=" + third +
                '}';
    }
}
