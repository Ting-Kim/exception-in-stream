package org.example.common;


import lombok.Getter;

@Getter
public class ObjectWithException<T> {

    T origin;
    Exception ex;

    public ObjectWithException(T origin, Exception ex) {
        this.origin = origin;
        this.ex = ex;
    }
}
