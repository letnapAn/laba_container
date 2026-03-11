package org.example;

public class IntHashSet implements IntContainer {
    private static final int DEFAULT_CAPACITY = 16;
    private Node[] buckets;
    private int size;

    private static class Node {
        int value;
        Node next;
        Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    public IntHashSet() {
        this.buckets = new Node[DEFAULT_CAPACITY];
    }

    private int hash(int value) {
        int h = (value == Integer.MIN_VALUE) ? 0 : Math.abs(value);
        return h % buckets.length;
    }

    @Override
    public boolean add(int value) {
        int index = hash(value);
        Node current = buckets[index];
        while (current != null) {
            if (current.value == value) return false;
            current = current.next;
        }
        buckets[index] = new Node(value, buckets[index]);
        size++;
        return true;
    }

    @Override
    public boolean contains(int value) {
        int index = hash(value);
        Node current = buckets[index];
        while (current != null) {
            if (current.value == value) return true;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean remove(int value) {
        int index = hash(value);
        Node current = buckets[index];
        Node prev = null;
        while (current != null) {
            if (current.value == value) {
                if (prev == null) buckets[index] = current.next;
                else prev.next = current.next;
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        buckets = new Node[DEFAULT_CAPACITY];
        size = 0;
    }
}