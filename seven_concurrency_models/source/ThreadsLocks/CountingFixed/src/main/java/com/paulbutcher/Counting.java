package com.paulbutcher;

// START:main
public class Counting {

  public static void main(String[] args) throws InterruptedException {
    
    // START:code.counter
    class Counter {
      private int count = 0;
      // START_HIGHLIGHT
      public synchronized void increment() { ++count; }
      // END_HIGHLIGHT
      public int getCount() { return count; }
    }
    // END:code.counter
    final Counter counter = new Counter();
    
    class CountingThread extends Thread {
      public void run() {
        for(int x = 0; x < 10000; ++x)
          counter.increment();
      }
    }

    CountingThread t1 = new CountingThread();
    CountingThread t2 = new CountingThread();
    
    t1.start(); t2.start();
    t1.join(); t2.join();
    
    System.out.println(counter.getCount());
  }
}
// END:main
