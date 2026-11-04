package by.it.group551003.khomich.lesson10;
import java.util.NoSuchElementException;

public class MyLinkedList<E> {

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    private int size = 0;
    private Node<E> first;
    private Node<E> last;

    public MyLinkedList() {
    }

    public int size() {
        return size;
    }

    public boolean add(E element) {
        addLast(element);
        return true;
    }

    public void addFirst(E element) {
        Node<E> f = first;
        Node<E> newNode = new Node<>(null, element, f);
        first = newNode;
        if (f == null)
            last = newNode;
        else
            f.prev = newNode;

        size++;
    }

    public void addLast(E element) {
        Node<E> l = last;
        Node<E> newNode = new Node<>(l, element, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }

    public E getFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        return first.item;
    }

    public E getLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return last.item;
    }

    public E element() {
        return getFirst();
    }

    public E pollFirst() {
        if (first == null) {
            return null;
        }
        E element = first.item;
        Node<E> nextNode = first.next;
        first.item = null;
        first.next = null;
        first = nextNode;
        if (nextNode == null) {
            last = null;
        } else {
            nextNode.prev = null;
        }
        size--;
        return element;
    }

    public E pollLast() {
        if (last == null) {
            return null;
        }
        E element = last.item;
        Node<E> prevNode = last.prev;
        last.item = null;
        last.prev = null;
        last = prevNode;
        if (prevNode == null) {
            first = null;
        } else {
            prevNode.next = null;
        }
        size--;
        return element;
    }

    public E poll() {
        return pollFirst();
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> x;
        if (index < (size >> 1)) {
            x = first;
            for (int i = 0; i < index; i++) {
                x = x.next;
            }
        } else {
            x = last;
            for (int i = size - 1; i > index; i--) {
                x = x.prev;
            }
        }
        return unlink(x);
    }

    public boolean remove(Object o) {
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    private E unlink(Node<E> x) {
        E element = x.item;
        Node<E> next = x.next;
        Node<E> prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        size--;
        return element;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> x = first;
        while (x != null) {
            sb.append(x.item);
            x = x.next;
            if (x != null) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}