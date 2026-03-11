package org.example;

public interface IntContainer {
    boolean add(int value);
    boolean contains(int value);
    boolean remove(int value);
    int size();
    void clear();
}
