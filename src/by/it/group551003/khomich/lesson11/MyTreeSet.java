package by.it.group551003.khomich.lesson11;
import java.util.Comparator;

public class MyTreeSet<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private E[] elements;
    private int size;
    private final Comparator<? super E> comparator;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = null;
    }

    @SuppressWarnings("unchecked")
    public MyTreeSet(Comparator<? super E> comparator) {
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
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
            for (int i = 0; i < size; i++)
                elements[i] = null;
        size = 0;
    }

    public boolean contains(Object o) {
        return binarySearch(o) >= 0;
    }

    public boolean add(E e) {
        if (e == null)
            throw new NullPointerException();

        int index = binarySearch(e);
        if (index >= 0)
            return false;

        int insertionPoint = -(index + 1);
        ensureCapacity();

        if (insertionPoint < size)
            System.arraycopy(elements, insertionPoint, elements, insertionPoint + 1, size - insertionPoint);

        elements[insertionPoint] = e;
        size++;
        return true;
    }

    public boolean remove(Object o) {
        if (o == null)
            return false;

        int index = binarySearch(o);
        if (index < 0)
            return false;

        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elements, index + 1, elements, index, numMoved);

        elements[--size] = null;
        return true;
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
        for (int i = size - 1; i >= 0; i--) {
            if (!containsInIterable(c, elements[i])) {
                remove(elements[i]);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public String toString() {
        if (size == 0)
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1)
                sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private int binarySearch(Object key) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = compare(elements[mid], key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid;
        }
        return -(low + 1);
    }

    @SuppressWarnings("unchecked")
    private int compare(E e1, Object e2) {
        if (comparator != null)
            return comparator.compare(e1, (E) e2);

        return ((Comparable<? super E>) e1).compareTo((E) e2);
    }

    private boolean containsInIterable(Iterable<?> c, Object element) {
        for (Object e : c)
            if (e == null ? element == null : e.equals(element))
                return true;

        return false;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            E[] newElements = (E[]) new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }
}