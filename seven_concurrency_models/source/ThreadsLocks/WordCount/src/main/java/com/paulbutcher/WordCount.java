package com.paulbutcher;

import java.util.Map;
import java.util.HashMap;

// START:main
public class WordCount {
  private static final HashMap<String, Integer> counts = 
    new HashMap<String, Integer>();

  public static void main(String[] args) throws Exception {
// END:main
    long start = System.currentTimeMillis();
// START:main
    Iterable<Page> pages = new Pages(100000, "enwiki.xml");
    for(Page page: pages) {
      Iterable<String> words = new Words(page.getText());
      for (String word: words)
        countWord(word);
    }
// END:main
    long end = System.currentTimeMillis();
    System.out.println("Elapsed time: " + (end - start) + "ms");

    // for (Map.Entry<String, Integer> e: counts.entrySet()) {
    //   System.out.println(e);
    // }
// START:main
  }

  private static void countWord(String word) {
    Integer currentCount = counts.get(word);
    if (currentCount == null)
      counts.put(word, 1);
    else
      counts.put(word, currentCount + 1);
  }
}
// END:main
