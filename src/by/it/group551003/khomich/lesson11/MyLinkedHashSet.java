package by.it.group551003.khomich.lesson11;

public class MyLinkedHashSet<E> {

    private static class Node<E> {
        E key;
        Node<E> next;
        Node<E> before;
        Node<E> after;
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

    private Node<E> head;
    private Node<E> tail;
    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity) {
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
        head = null;
        tail = null;
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

        Node<E> newNode = new Node<>(e, table[index]);
        table[index] = newNode;
        linkNodeAtEnd(newNode);
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
                unlinkNode(current);
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }

        return false;
    }

    public boolean containsAll(Iterable<?> c) {
        if (c == null)
            throw new NullPointerException();
        for (Object e : c)
            if (!contains(e))
                return false;
        return true;
    }

    public boolean addAll(Iterable<? extends E> c) {
        if (c == null)
            throw new NullPointerException();
        boolean modified = false;
        for (E e : c)
            if (add(e))
                modified = true;
        return modified;
    }

    public boolean removeAll(Iterable<?> c) {
        if (c == null)
            throw new NullPointerException();
        boolean modified = false;
        for (Object e : c)
            if (remove(e))
                modified = true;
        return modified;
    }

    public boolean retainAll(Iterable<?> c) {
        if (c == null)
            throw new NullPointerException();
        boolean modified = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.after;
            if (!containsInIterable(c, current.key)) {
                remove(current.key);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public String toString() {
        if (size == 0)
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.key);
            current = current.after;
            if (current != null)
                sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    private void linkNodeAtEnd(Node<E> node) {
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            tail.after = node;
            node.before = tail;
            tail = node;
        }
    }

    private void unlinkNode(Node<E> node) {
        if (node.before == null)
            head = node.after;
        else
            node.before.after = node.after;

        if (node.after == null)
            tail = node.before;
        else
            node.after.before = node.before;

        node.before = null;
        node.after = null;
    }

    private boolean containsInIterable(Iterable<?> c, Object element) {
        for (Object e : c)
            if (keysEqual(e, element))
                return true;
        return false;
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

        Node<E> current = head;
        while (current != null) {
            int newIndex = getIndex(current.key, newCapacity);
            current.next = newTable[newIndex];
            newTable[newIndex] = current;
            current = current.after;
        }

        table = newTable;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
    }
}