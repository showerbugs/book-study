package com.paulbutcher;

import java.util.concurrent.locks.ReentrantLock;

// START:main
public class DiningPhilosophers {

  public static void main(String[] args) throws InterruptedException {
    Philosopher[] philosophers = new Philosopher[5];
    ReentrantLock[] chopsticks = new ReentrantLock[5];
    
    for (int i = 0; i < 5; ++i)
      chopsticks[i] = new ReentrantLock();
    for (int i = 0; i < 5; ++i) {
      philosophers[i] = new Philosopher(chopsticks[i], chopsticks[(i + 1) % 5]);
      philosophers[i].start();
    }
    for (int i = 0; i < 5; ++i)
      philosophers[i].join();
  }
}
// END:main