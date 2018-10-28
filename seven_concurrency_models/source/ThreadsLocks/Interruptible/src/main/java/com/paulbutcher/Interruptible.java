package com.paulbutcher;

import java.util.concurrent.locks.ReentrantLock;

// START:main
public class Interruptible {

  public static void main(String[] args) throws InterruptedException {

// START:lockInterruptibly
    final ReentrantLock l1 = new ReentrantLock();
    final ReentrantLock l2 = new ReentrantLock();

    Thread t1 = new Thread() {
      public void run() {
        try {
          // START_HIGHLIGHT
          l1.lockInterruptibly();
          // END_HIGHLIGHT
          Thread.sleep(1000);
          // START_HIGHLIGHT
          l2.lockInterruptibly();
          // END_HIGHLIGHT
        } catch (InterruptedException e) { System.out.println("t1 interrupted"); }
      }
    };
// END:lockInterruptibly

    Thread t2 = new Thread() {
      public void run() {
        try {
          // START_HIGHLIGHT
          l2.lockInterruptibly();
          // END_HIGHLIGHT
          Thread.sleep(1000);
          // START_HIGHLIGHT
          l1.lockInterruptibly();
          // END_HIGHLIGHT
        } catch (InterruptedException e) { System.out.println("t2 interrupted"); }
      }
    };

    t1.start(); t2.start();
    Thread.sleep(2000);
    t1.interrupt(); t2.interrupt();
    t1.join(); t2.join();
  }
}
// END:main