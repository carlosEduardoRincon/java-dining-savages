package jds.semaphore;

import java.util.concurrent.Semaphore;

public class Savage implements Runnable {

    private DiningSavagesSemaphore coordinator;
    private int id;

    private Semaphore mutex;
    private Semaphore emptyPot;
    private Semaphore fullPot;

    public Savage(int id,
                   DiningSavagesSemaphore coordinator,
                   Semaphore mutex,
                   Semaphore emptyPot,
                   Semaphore fullPot) {
        this.coordinator = coordinator;
        this.id = id;

        this.mutex = mutex;
        this.emptyPot = emptyPot;
        this.fullPot = fullPot;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(2 * 1000);
                //mutex.acquireUninterruptibly();

                // CASE: Panela esteja vazia
                if (coordinator.getFoodInPot() == 0) {
                    /*
                        Sinaliza que a panela está vazia, ou seja, liberando
                        uma permissão(cozinheiro)
                    */
                    emptyPot.release();
                    /*
                        Bloqueia até que seja sinalizada(panela cheia)
                    */
                    fullPot.acquireUninterruptibly();
                }

                System.out.println("Savage " + id
                        + " is eating food "
                        + coordinator.getFoodInPot());
                coordinator.eat();

                //mutex.release(); // Libera o semáforo principal (próximo selvagem)
            }
        } catch( InterruptedException e ) {
            e.printStackTrace();
        }
    }
}