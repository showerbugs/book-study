package com.paulbutcher;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

// START:philosopher
class Philosopher extends Thread {
  private ReentrantLock leftChopstick, rightChopstick;
  private Random random;
// END:philosopher
  private int thinkCount;
// START:philosopher

  public Philosopher(ReentrantLock leftChopstick, ReentrantLock rightChopstick) {
    this.leftChopstick = leftChopstick; this.rightChopstick = rightChopstick;
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
        Thread.sleep(random.nextInt(1000)); // Think for a while
        leftChopstick.lock();
        try {
          // START_HIGHLIGHT
          if (rightChopstick.tryLock(1000, TimeUnit.MILLISECONDS)) {
          // END_HIGHLIGHT
            // Got the right chopstick
            try {
              Thread.sleep(random.nextInt(1000)); // Eat for a while
            } finally { rightChopstick.unlock(); }
          } else {
            // START_HIGHLIGHT
            // Didn't get the right chopstick - give up and go back to thinking
            // END_HIGHLIGHT
// END:philosopher
            System.out.println("Philosopher " + this + " timed out");
// START:philosopher
          }
        } finally { leftChopstick.unlock(); }
      }
    } catch(InterruptedException e) {}
  }
}
// END:philosopher