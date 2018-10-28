package com.paulbutcher;

import java.util.Date;

class DateFormatBug {
  public static void main(String[] args) throws Exception {
    final DateParser parser = new DateParser();
    final String dateString = "2012-01-01";
    final Date dateParsed = parser.parse(dateString);

    class ParsingThread extends Thread {
      public void run() {
        try {
          while(true) {
            Date d = parser.parse(dateString);
            if (!d.equals(dateParsed)) {
              System.out.println("Expected: "+ dateParsed +", got: "+ d);
            }
          }
        } catch (Exception e) {
          System.out.println("Caught: "+ e);
        }
      }
    }

    Thread t1 = new ParsingThread();
    Thread t2 = new ParsingThread();
    t1.start();
    t2.start();
  }
}