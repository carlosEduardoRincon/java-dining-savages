package jds.monitor;

public class Savage implements Runnable {

    private DiningSavagesMonitor monitor;

    private int id;

    public Savage(int id, DiningSavagesMonitor monitor) {
        this.monitor = monitor;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000);
                System.out.println("Savage " + id + " is eating meal");
                monitor.eat(id);
            }
        } catch( InterruptedException e ) {
            e.printStackTrace();
        }
    }
}
