package com.paulbutcher;

import java.util.regex.*;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

// START:contribution
class Contribution {
  static final Pattern pattern = Pattern.compile("^([^\\s]*) (\\d*) (\\d*) (.*)$"); // <label id="code.contributorpattern"/> 
  static final DateTimeFormatter isoFormat = ISODateTimeFormat.dateTimeNoMillis(); // <label id="code.isoformat"/>

  public long timestamp;
  public int id;
  public int contributorId;
  public String username;

  public Contribution(String line) {
    Matcher matcher = pattern.matcher(line);
    if(matcher.find()) {
      timestamp = isoFormat.parseDateTime(matcher.group(1)).getMillis(); // <label id="code.getmillis"/>
      id = Integer.parseInt(matcher.group(2));
      contributorId = Integer.parseInt(matcher.group(3));
      username = matcher.group(4);
    }
  }
}
// END:contribution
