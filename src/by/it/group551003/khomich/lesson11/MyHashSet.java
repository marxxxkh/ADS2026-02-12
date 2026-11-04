package by.it.group551003.khomich.lesson11;

public class MyHashSet<E> {

    private static class Node<E> {
        E key;
        Node<E> next;
        Node(E key, Node<E> next) {
            this.key = key;
            this.next = next;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public MyHashSet(int initialCapacity) {
        int capacity = initialCapacity <= 0 ? DEFAULT_CAPACITY : initialCapacity;
        table = (Node<E>[]) new Node[capacity];
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    public void clear() {
        if (size > 0)
            for (int i = 0; i < table.length; i++)
                table[i] = null;
        size = 0;
    }

    public boolean contains(Object o) {
        int index = getIndex(o, table.length);
        Node<E> current = table[index];
        while (current != null) {
            if (keysEqual(current.key, o))
                return true;
            current = current.next;
        }
        return false;
    }

    public boolean add(E e) {
        int index = getIndex(e, table.length);
        Node<E> current = table[index];

        while (current != null) {
            if (keysEqual(current.key, e))
                return false;
            current = current.next;
        }

        table[index] = new Node<>(e, table[index]);
        size++;

        if (size >= threshold)
            resize();

        return true;
    }

    public boolean remove(Object o) {
        int index = getIndex(o, table.length);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (keysEqual(current.key, o)) {
                if (prev == null)
                    table[index] = current.next;
                else
                    prev.next = current.next;
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public String toString() {
        if (size == 0)
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;

        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                if (!first)
                    sb.append(", ");
                sb.append(current.key);
                first = false;
                current = current.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    private int getIndex(Object o, int length) {
        if (o == null)
            return 0;
        int hash = o.hashCode();
        return (hash ^ (hash >>> 16)) & (length - 1);
    }

    private boolean keysEqual(Object k1, Object k2) {
        if (k1 == k2)
            return true;
        if (k1 == null || k2 == null)
            return false;
        return k1.equals(k2);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                Node<E> next = current.next;
                int newIndex = getIndex(current.key, newCapacity);

                current.next = newTable[newIndex];
                newTable[newIndex] = current;
                current = next;
            }
        }

        table = newTable;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
    }
}