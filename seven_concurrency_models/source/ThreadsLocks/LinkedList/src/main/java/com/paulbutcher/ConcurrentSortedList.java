package com.paulbutcher;

import java.util.concurrent.locks.ReentrantLock;

// START:concurrentsortedlist
class ConcurrentSortedList {

  private class Node {
    int value;
    Node prev;
    Node next;
    ReentrantLock lock = new ReentrantLock();

    Node() {}

    Node(int value, Node prev, Node next) {
      this.value = value; this.prev = prev; this.next = next;
    }
  }

  private final Node head;
  private final Node tail;

  public ConcurrentSortedList() {
    head = new Node(); tail = new Node();
    head.next = tail; tail.prev = head;
  }

  public void insert(int value) {
    Node current = head;
    current.lock.lock(); //<label id="code.lockhead"/>
    Node next = current.next;
    try {
      while (true) {
        next.lock.lock(); //<label id="code.locknext"/>
        try {
          if (next == tail || next.value < value) { //<label id="code.lessthantest"/>
            Node node = new Node(value, current, next); //<label id="code.newnodestart"/>
            next.prev = node;
            current.next = node;
            return; //<label id="code.newnodeend"/>
          }
        } finally { current.lock.unlock(); } //<label id="code.unlockcurrent"/>
        current = next;
        next = current.next;
      }
    } finally { next.lock.unlock(); } //<label id="code.unlocknext"/>
  }
// END:concurrentsortedlist

// START:listsize
  public int size() {
    Node current = tail;
    int count = 0;
	
    while (current.prev != head) {
      ReentrantLock lock = current.lock;
      lock.lock();
      try {
        ++count;
        current = current.prev;
      } finally { lock.unlock(); }
    }
	
    return count;
  }
// END:listsize

  public boolean isSorted() {
    Node current = head;
    while (current.next.next != tail) {
      current = current.next;
      if (current.value < current.next.value)
        return false;
    }
    return true;
  }
// START:concurrentsortedlist
}
// END:concurrentsortedlist
