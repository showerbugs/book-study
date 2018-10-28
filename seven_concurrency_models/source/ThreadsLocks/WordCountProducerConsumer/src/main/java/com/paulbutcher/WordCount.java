package com.paulbutcher;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public class WordCount {

  public static void main(String[] args) throws Exception {
    // START:mainloop
    ArrayBlockingQueue<Page> queue = new ArrayBlockingQueue<Page>(100);
    HashMap<String, Integer> counts = new HashMap<String, Integer>();

    Thread counter = new Thread(new Counter(queue, counts));
    Thread parser = new Thread(new Parser(queue));
    // END:mainloop
    long start = System.currentTimeMillis();
    // START:mainloop
	
    counter.start();
    parser.start();
    parser.join();
    queue.put(new PoisonPill());
    counter.join();
    // END:mainloop
    long end = System.currentTimeMillis();
    System.out.println("Elapsed time: " + (end - start) + "ms");

    // for (Map.Entry<String, Integer> e: counts.entrySet()) {
    //   System.out.println(e);
    // }
  }
}
