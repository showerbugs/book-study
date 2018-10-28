package com.paulbutcher;

import java.util.concurrent.BlockingQueue;
import java.util.Map;

// START:counter
class Counter implements Runnable {
  private BlockingQueue<Page> queue;
  private Map<String, Integer> counts;
  
  public Counter(BlockingQueue<Page> queue,
                 Map<String, Integer> counts) {
    this.queue = queue;
    this.counts = counts;
  }

  public void run() {
    try {
      while(true) {
        // START_HIGHLIGHT
        Page page = queue.take();
        // END_HIGHLIGHT
// START:poisonpill
        if (page.isPoisonPill())
          break;
// END:poisonpill

        // START_HIGHLIGHT
        Iterable<String> words = new Words(page.getText());
        for (String word: words)
          countWord(word);
        // END_HIGHLIGHT
      }
    } catch (Exception e) { e.printStackTrace(); }
  }
// END:counter

  private void countWord(String word) {
    Integer currentCount = counts.get(word);
    if (currentCount == null)
      counts.put(word, 1);
    else
      counts.put(word, currentCount + 1);
  }
// START:counter
}
// END:counter
