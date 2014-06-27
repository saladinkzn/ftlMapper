package ru.shadam.ftlmapper.domain;

/**
 * @author sala
 */
public class Tuple2<T1, T2> {
    protected final T1 first;
    protected final T2 second;

    public Tuple2(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }


    @Override
    public String toString() {
        return "Tuple2{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
