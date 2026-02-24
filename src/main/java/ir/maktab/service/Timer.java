package ir.maktab.service;

public class Timer implements Runnable {
    private int secondsLeft = 30 * 60;
    private volatile boolean timeUp = true;

    public Timer(String remainingTime) {
      this.secondsLeft=secondsLeft;
    }
    public Timer(){

    }

    @Override
    public void run() {

        while (secondsLeft > 0&& timeUp) {
            try {
                Thread.sleep(1000);
                secondsLeft--;
            } catch (InterruptedException e) {
                break;
            }
        }
    }
    public void stop() {
       timeUp = false;
    }

    public String getRemainingTime() {

        int min = secondsLeft / 60;
        int sec = secondsLeft % 60;
        return String.format("%02d:%02d", min, sec);
    }

}
