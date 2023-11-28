package jds.monitor;

public class Cook implements Runnable {

    private DiningSavagesMonitor monitor;

    public Cook(DiningSavagesMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while(true) {
            monitor.refillPot();
        }
    }
}
