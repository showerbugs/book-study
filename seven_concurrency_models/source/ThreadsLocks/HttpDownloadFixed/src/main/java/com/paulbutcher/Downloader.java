package com.paulbutcher;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

class Downloader extends Thread {
  private InputStream in;
  private OutputStream out;
  private ArrayList<ProgressListener> listeners;

  public Downloader(URL url, String outputFilename) throws IOException {
    in = url.openConnection().getInputStream();
    out = new FileOutputStream(outputFilename);
    listeners = new ArrayList<ProgressListener>();
  }

  public synchronized void addListener(ProgressListener listener) {
    listeners.add(listener);
  }

  public synchronized void removeListener(ProgressListener listener) {
    listeners.remove(listener);
  }

  // START:updateProgress
  private void updateProgress(int n) {
    ArrayList<ProgressListener> listenersCopy;
    synchronized(this) {
      // START_HIGHLIGHT
      listenersCopy = (ArrayList<ProgressListener>)listeners.clone();
      // END_HIGHLIGHT
    }
    for (ProgressListener listener: listenersCopy)
      listener.onProgress(n);
  }
  // END:updateProgress

  public void run() {
    int n = 0, total = 0;
    byte[] buffer = new byte[1024];

    try {
      while((n = in.read(buffer)) != -1) {
        out.write(buffer, 0, n);
        total += n;
        updateProgress(total);
      }
      out.flush();
    } catch (IOException e) { }
  }
}
