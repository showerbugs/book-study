package com.paulbutcher;

// Wikipedia makes use of the full range of Unicode characters, and (sadly)
// these confuse the standard Java BreakIterator. So use the ICU4J version
import com.ibm.icu.text.BreakIterator;
import java.util.Iterator;

class Words implements Iterable<String> {

  private final String text;

  public Words(String text) {
    this.text = text;
  }

  private class WordIterator implements Iterator<String> {

    private BreakIterator wordBoundary;
    private int start;
    private int end;
    String nextWord = "";

    public WordIterator() {
      wordBoundary = BreakIterator.getWordInstance();
      wordBoundary.setText(text);
      start = wordBoundary.first();
      end = wordBoundary.next();
      getNextWord();
    }

    public boolean hasNext() { return !nextWord.isEmpty(); }

    public String next() {
      String s = nextWord;
      getNextWord();
      return s;
    }

    public void remove() { throw new UnsupportedOperationException(); }

    // Find the next non-blank word
    private void getNextWord() {
      nextWord = "";
      while (nextWord.isEmpty() && end != BreakIterator.DONE) {
        nextWord = text.substring(start, end).trim();
        start = end;
        end = wordBoundary.next();
      }
    }
  }

  public Iterator<String> iterator() {
    return new WordIterator();
  }
}