package veinthrough.api.collection;


import java.util.AbstractQueue;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 A first-in, first-out bounded collection.
 Implemented by circular array.
 */
public class CircularArrayQueue<E> extends AbstractQueue<E> {
    private Object[] elements;
    private int head;
    private int tail;
    private int count;
    /**
     * To monitor concurrent modification, otherwise throws ConcurrentModificationException.
     */
    private int modCount;

    /**
     * Constructs an empty queue.
     *
     * @param capacity the maximum capacity of the queue
     */
    public CircularArrayQueue(int capacity) {
        elements = new Object[capacity];
        count = 0;
        head = 0;
        tail = 0;
    }

    public boolean offer(E newElement) {
        assert newElement != null;
        if (count < elements.length) {
            elements[tail] = newElement;
            tail = (tail + 1) % elements.length;
            count++;
            modCount++;
            return true;
        } else
            return false;
    }

    public E poll() {
        if (count == 0) return null;
        E r = peek();
        head = (head + 1) % elements.length;
        count--;
        modCount++;
        return r;
    }

    @SuppressWarnings("unchecked")
    public E peek() {
        if (count == 0) return null;
        return (E) elements[head];
    }

    public int size() {
        return count;
    }

    public Iterator<E> iterator() {
        return new QueueIterator();

    }

    private class QueueIterator implements Iterator<E> {
        private int offset;
        private int modcountAtConstruction;

        QueueIterator() {
            modcountAtConstruction = modCount;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            E r = (E) elements[(head + offset) % elements.length];
            offset++;
            return r;
        }

        public boolean hasNext() {
            // ConcurrentModificationException
            if (modCount != modcountAtConstruction)
                throw new ConcurrentModificationException();
            return offset < count;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
