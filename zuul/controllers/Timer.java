package zuul.controllers;

public class Timer implements Runnable {

  private boolean doStop = false;
  private int counter = 0;

  public synchronized void doStop() {
    this.doStop = true;
  }

  private synchronized boolean keepRunning() {
    return this.doStop == false;
  }

  public synchronized int getCounter() {
    return counter;
  }

  @Override
  public void run() {
    while (keepRunning()) {
      counter++;

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}