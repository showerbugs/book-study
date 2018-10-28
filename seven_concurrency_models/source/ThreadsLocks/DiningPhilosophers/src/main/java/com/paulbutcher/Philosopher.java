package com.paulbutcher;

import java.util.Random;

// START:philosopher
class Philosopher extends Thread {
  private Chopstick left, right;
  private Random random;
// END:philosopher
  private int thinkCount;
// START:philosopher

  public Philosopher(Chopstick left, Chopstick right) {
    this.left = left; this.right = right;
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
        synchronized(left) {                    // Grab left chopstick // <label id="code.syncleft"/>
          synchronized(right) {                 // Grab right chopstick // <label id="code.syncright"/>
            Thread.sleep(random.nextInt(1000)); // Eat for a while
          }
        }
      }
    } catch(InterruptedException e) {}
  }
}
// END:philosopher