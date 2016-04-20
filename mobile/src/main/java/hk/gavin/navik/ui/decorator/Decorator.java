package hk.gavin.navik.ui.decorator;

public abstract class Decorator<T> {

    public final T object;

    public Decorator(T object) {
        this.object = object;
    }
}
