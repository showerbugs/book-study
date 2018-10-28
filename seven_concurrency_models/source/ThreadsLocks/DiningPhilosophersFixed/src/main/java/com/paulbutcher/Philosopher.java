package com.paulbutcher;

import java.util.Random;

// START:philosopher
class Philosopher extends Thread {
// START_HIGHLIGHT
  private Chopstick first, second;
// END_HIGHLIGHT
  private Random random;
// END:philosopher
  private int thinkCount;
// START:philosopher

  public Philosopher(Chopstick left, Chopstick right) {
// START_HIGHLIGHT
    if(left.getId() < right.getId()) {
      first = left; second = right;
    } else {
      first = right; second = left;
    }
// END_HIGHLIGHT
    random = new Random();
  }

  public void run() {
    try {
      while(true) {
// END:philosopher
        ++thinkCount;
        if (thinkCount % 10 == 0)
          System.out.println("Philosopher " + this + " has thought " + thinkCount + " times");
// START:philosopher
        Thread.sleep(random.nextInt(1000));     // Think for a while
// START_HIGHLIGHT
        synchronized(first) {                   // Grab first chopstick
          synchronized(second) {                // Grab second chopstick
// END_HIGHLIGHT
            Thread.sleep(random.nextInt(1000)); // Eat for a while
          }
        }
      }
    } catch(InterruptedException e) {}
  }
}
// END:philosopher