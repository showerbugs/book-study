package com.paulbutcher;

import java.util.concurrent.atomic.AtomicInteger;

// START:main
public class Counting {
  public static void main(String[] args) throws InterruptedException {
    
    // START_HIGHLIGHT
    final AtomicInteger counter = new AtomicInteger();
    // END_HIGHLIGHT
    
    class CountingThread extends Thread {
      public void run() {
        for(int x = 0; x < 10000; ++x)
          // START_HIGHLIGHT
          counter.incrementAndGet();
          // END_HIGHLIGHT
      }
    }

    CountingThread t1 = new CountingThread();
    CountingThread t2 = new CountingThread();
    
    t1.start(); t2.start();
    t1.join(); t2.join();
    
    System.out.println(counter.get());
  }
}
// END:main
