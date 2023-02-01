package generics;

import java.util.Iterator;
import java.util.Objects;

public class SinglyLinkedList<T> implements Iterable<SinglyLinkedList<T>.Node> {

    public SinglyLinkedList() {
        validate(this);
    }

    @Override
    public Iterator<Node> iterator() {
        return new Iterator<>() {
            private Node cur = start;

            @Override
            public boolean hasNext() {
                return cur != null;
            }

            @Override
            public Node next() {
                Node cache = cur;
                cur = cur.next;
                return cache;
            }
        };
    }

    public class Node {
        public T value;
        public Node next;

        public Node(T value, Node next) {
            this.value = value;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Node node = (Node) o;
            if (this.value.getClass() != node.value.getClass())
                return false;
            return Objects.equals(value, node.value) && Objects.equals(next, node.next);
        }
    }

    private Node start = null;
    private Node end = null;
    private int size = 0;

    public void add(T value) {
        validate(this);
        size++;
        if (start == null) {
            start = new Node(value, null);
            end = start;
            return;
        }
        end.next = new Node(value, null);
        end = end.next;
        validate(this);
    }

    public static <U> void validate(SinglyLinkedList<U> list) {
        if (list.start == null && list.end == null) {
            return;
        }
        assert list.start != null && list.end != null : "either start or end was null";
        assert list.end.next == null : "end.next was not null";
        assert list.iterator().hasNext() : "iterator returned no elements, when there has to be at least one";
        assert list.iterator().next() == list.start : "iterator didn't return start as the first element";
        int num = 0;
        for (var ignored : list) {
            num++;
        }
        assert list.size == num : "sizes didn't match";
        // all fine.
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SinglyLinkedList<?> that = (SinglyLinkedList<?>) o;
        if (size != that.size || start != that.start || end != that.end) {
            return false;
        }
        Iterator<? extends SinglyLinkedList<?>.Node> thisIterator = this.iterator(), thatIterator = that.iterator();
        while (thisIterator.hasNext() && thatIterator.hasNext()) {
            SinglyLinkedList<?>.Node thisNode = thisIterator.next(), thatNode = thatIterator.next();
            if (!thisNode.equals(thatNode)) {
                return false;
            }
        }
        return thisIterator.hasNext() == thatIterator.hasNext();
    }

    public void appendAllNoCopy(SinglyLinkedList<T> other) {
        assert this != other : "cannot append this to this";
        validate(other);
        validate(this);
        if (other.getStart() == null && other.getEnd() == null) {
            return;
        }
        if (this.getStart() == null && this.getEnd() == null) {
            this.start = other.getStart();
            this.end = other.getEnd();
            size = other.size;
            return;
        }
        end.next = other.getStart();
        end = other.getEnd();
        size += other.size;
        validate(this);
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }

}