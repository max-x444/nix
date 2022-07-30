package com.utils;

import com.model.Vehicle;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Comparator;

public class BinaryTree<T extends Vehicle> {
    private static final int INDENT = 4;
    private final Comparator<T> comparator;
    private Node root;

    public BinaryTree(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    private class Node {
        private final T data;
        private Node leftChild;
        private Node rightChild;

        public Node(T data) {
            this.data = data;
            this.leftChild = null;
            this.rightChild = null;
        }
    }

    public void add(@NonNull T vehicle) {
        Node newNode = new Node(vehicle);
        if (root == null) {
            root = newNode;
        } else {
            Node temp = root;
            while (true) {
                if (comparator.compare(temp.data, vehicle) > 0) {
                    if (temp.leftChild != null) {
                        temp = root.leftChild;
                    } else {
                        temp.leftChild = newNode;
                        return;
                    }
                } else {
                    if (temp.rightChild != null) {
                        temp = root.rightChild;
                    } else {
                        temp.rightChild = newNode;
                        return;
                    }
                }
            }
        }
    }

    private void printTree(Node root, int space) {
        if (root == null) {
            return;
        }
        space += INDENT;
        printTree(root.rightChild, space);
        for (int i = INDENT; i < space; i++) {
            System.out.print(" ");
        }
        System.out.print(root.data.getPrice() + "\n");
        printTree(root.leftChild, space);
    }

    public void print() {
        printTree(root, 0);
    }

    public BigDecimal getValue(Node node, BigDecimal value) {
        if (node == null) {
            return BigDecimal.ZERO;
        }
        value = value.add(node.data.getPrice());
        if (node.leftChild != null) {
            value = getValue(node.leftChild, value);
        }
        if (node.rightChild != null) {
            value = getValue(node.rightChild, value);
        }
        return value;
    }

    public BigDecimal getValueLeftBranch() {
        if (root == null) {
            return BigDecimal.ZERO;
        } else {
            return getValue(root.leftChild, root.data.getPrice());
        }
    }

    public BigDecimal getValueRightBranch() {
        if (root == null) {
            return BigDecimal.ZERO;
        } else {
            return getValue(root.rightChild, root.data.getPrice());
        }
    }
}