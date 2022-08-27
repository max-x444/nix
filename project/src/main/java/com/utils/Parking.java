package com.utils;

import com.model.vehicle.Vehicle;
import lombok.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;


public class Parking<T extends Vehicle> implements Iterable<T> {
    private Node head;
    private int size;

    private class Node {
        private T data;
        private Node next;
        private Node previous;
        private final String restylingNumber;
        private final String time;

        private Node(T data, Node next, Node previous) {
            this.data = data;
            this.next = next;
            this.previous = previous;
            this.restylingNumber = UUID.randomUUID().toString();
            this.time = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        }
    }

    public void add(@NonNull T vehicle) {
        Node newNode = new Node(vehicle, null, null);
        if (head == null) {
            head = newNode;
        } else {
            Node oldHead = head;
            head = newNode;
            head.next = oldHead;
            oldHead.previous = head;
        }
        size++;
    }

    private Optional<Node> searchNode(String restylingNumber) {
        Node temp = head;
        while (temp != null) {
            if (temp.restylingNumber.equals(restylingNumber)) {
                return Optional.of(temp);
            }
            temp = temp.next;
        }
        return Optional.empty();
    }

    public T search(@NonNull String restylingNumber) {
        Optional<Node> founded = searchNode(restylingNumber);
        if (founded.isPresent()) {
            return founded.get().data;
        } else {
            throw new NoSuchElementException("No vehicle found by restyling number: " + restylingNumber);
        }
    }

    public boolean remove(@NonNull String restylingNumber) {
        Optional<Node> founded = searchNode(restylingNumber);
        Node elementToBeRemoved;
        if (founded.isPresent()) {
            elementToBeRemoved = founded.get();
        } else {
            return false;
        }
        if (elementToBeRemoved.previous == null) {
            if (elementToBeRemoved.next == null) {
                head = null;
                size--;
                return true;
            }
            head = elementToBeRemoved.next;
        }
        if (elementToBeRemoved.next == null) {
            elementToBeRemoved = elementToBeRemoved.previous;
            elementToBeRemoved.next = null;
        }
        if (elementToBeRemoved.previous != null && elementToBeRemoved.next != null) {
            Node temp = elementToBeRemoved.next;
            elementToBeRemoved = elementToBeRemoved.previous;
            elementToBeRemoved.next = temp;
        }
        size--;
        return true;
    }

    public boolean set(@NonNull String restylingNumber, T vehicle) {
        Optional<Node> founded = searchNode(restylingNumber);
        if (founded.isPresent()) {
            founded.get().data = vehicle;
            return true;
        } else {
            return false;
        }
    }

    public int getSize() {
        return size;
    }

    public String getHeadTime() {
        if (head == null) {
            return "There are no vehicle in the parking lot";
        }
        return head.time;
    }

    public String getTailTime() {
        if (head == null) {
            return "There are no vehicle in the parking lot";
        } else {
            Node tail = head;
            while (true) {
                if (tail.next == null) {
                    return tail.time;
                }
                tail = tail.next;
            }
        }
    }

    public String getRestylingNumber(T vehicle) {
        Node temp = head;
        while (temp != null) {
            if (temp.data.equals(vehicle)) {
                return temp.restylingNumber;
            }
            temp = temp.next;
        }
        return "There is no such vehicle in the parking lot";
    }

    public String print() {
        Node temp = head;
        StringJoiner stringJoiner = new StringJoiner("--");
        while (temp != null) {
            stringJoiner.add(temp.data.getClass().getSimpleName());
            temp = temp.next;
        }
        return stringJoiner.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new ParkingIterator();
    }

    private class ParkingIterator implements Iterator<T> {
        private Node head;

        public ParkingIterator() {
            this.head = Parking.this.head;
        }

        @Override
        public boolean hasNext() {
            return this.head != null;
        }

        @Override
        public T next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            T data = head.data;
            head = head.next;
            return data;
        }
    }
}