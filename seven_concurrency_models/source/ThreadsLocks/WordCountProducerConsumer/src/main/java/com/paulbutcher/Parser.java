package com.paulbutcher;
import java.util.concurrent.BlockingQueue;
// START:parser
class Parser implements Runnable {
  private BlockingQueue<Page> queue;

  public Parser(BlockingQueue<Page> queue) {
    this.queue = queue;
  }
  
  public void run() {
    try {
      // START_HIGHLIGHT
      Iterable<Page> pages = new Pages(100000, "enwiki.xml");
      for (Page page: pages)
        queue.put(page);
      // END_HIGHLIGHT
    } catch (Exception e) { e.printStackTrace(); }
  }
}
// END:parser
