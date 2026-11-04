package by.it.group551003.khomich.lesson10;
import java.util.NoSuchElementException;

public class MyPriorityQueue<E> {

    private static final int DEFAULT_CAPACITY = 11;
    private E[] queue;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        this.queue = (E[]) new Object[DEFAULT_CAPACITY];
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int initialCapacity) {
        if (initialCapacity < 1) {
            initialCapacity = DEFAULT_CAPACITY;
        }
        this.queue = (E[]) new Object[initialCapacity];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    public void clear() {
        for (int i = 0; i < size; i++) {
            queue[i] = null;
        }
        size = 0;
    }

    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        ensureCapacity();
        int i = size;
        size = i + 1;
        if (i == 0) {
            queue[0] = e;
        } else {
            siftUp(i, e);
        }
        return true;
    }

    public boolean add(E e) {
        return offer(e);
    }

    public E peek() {
        return (size == 0) ? null : queue[0];
    }

    public E element() {
        E x = peek();
        if (x != null) {
            return x;
        }
        throw new NoSuchElementException();
    }


    public E poll() {
        if (size == 0) {
            return null;
        }
        int s = --size;
        E result = queue[0];
        E x = queue[s];
        queue[s] = null;
        if (s != 0) {
            siftDown(0, x);
        }
        return result;
    }

    public E remove() {
        E x = poll();
        if (x != null) {
            return x;
        }
        throw new NoSuchElementException();
    }

    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    public boolean containsAll(Iterable<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        for (Object e : c) {
            if (!contains(e))
                return false;

        }
        return true;
    }

    public boolean addAll(Iterable<? extends E> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        if (c == this) {
            throw new IllegalArgumentException();
        }
        boolean modified = false;
        for (E e : c) {
            if (offer(e)) {
                modified = true;
            }
        }
        return modified;
    }

    public boolean removeAll(Iterable<?> c) {
        if (c == null)
            throw new NullPointerException();

        boolean modified = false;
        for (Object e : c) {
            while (contains(e)) {
                removeElement(e);
                modified = true;
            }
        }
        return modified;
    }

    public boolean retainAll(Iterable<?> c) {
        if (c == null)
            throw new NullPointerException();

        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!containsInIterable(c, queue[i])) {
                removeAt(i);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(queue[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private int indexOf(Object o) {
        if (o != null) {
            for (int i = 0; i < size; i++) {
                if (o.equals(queue[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    private boolean removeElement(Object o) {
        int i = indexOf(o);
        if (i == -1) {
            return false;
        } else {
            removeAt(i);
            return true;
        }
    }

    private void removeAt(int i) {
        int s = --size;
        if (s == i) {
            queue[i] = null;
        } else {
            E moved = queue[s];
            queue[s] = null;
            siftDown(i, moved);
            if (queue[i] == moved) {
                siftUp(i, moved);
            }
        }
    }

    private boolean containsInIterable(Iterable<?> c, Object element) {
        for (Object e : c) {
            if (e == null ? element == null : e.equals(element)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object e = queue[parent];
            if (key.compareTo((E) e) >= 0) {
                break;
            }
            queue[k] = (E) e;
            k = parent;
        }
        queue[k] = x;
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            Object c = queue[child];
            int right = child + 1;
            if (right < size && ((Comparable<? super E>) c).compareTo((E) queue[right]) > 0) {
                child = right;
                c = queue[child];
            }
            if (key.compareTo((E) c) <= 0) {
                break;
            }
            queue[k] = (E) c;
            k = child;
        }
        queue[k] = x;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size >= queue.length) {
            int oldCapacity = queue.length;
            int newCapacity = oldCapacity + (oldCapacity < 64 ? oldCapacity + 2 : oldCapacity >> 1);
            E[] newQueue = (E[]) new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newQueue[i] = queue[i];
            }
            queue = newQueue;
        }
    }
}