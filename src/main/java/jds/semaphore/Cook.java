package jds.semaphore;

import java.util.concurrent.Semaphore;

public class Cook implements Runnable {

    private DiningSavagesSemaphore coordinator;

    private Semaphore mutex;
    private Semaphore emptyPot;
    private Semaphore fullPot;

    public Cook(DiningSavagesSemaphore coordinator,
                 Semaphore mutex,
                 Semaphore emptyPot,
                 Semaphore fullPot) {

        this.coordinator = coordinator;

        this.mutex = mutex;
        this.emptyPot = emptyPot;
        this.fullPot = fullPot;
    }

    @Override
    public void run() {

        while(true) {

            /*
                A utilização do método "acquireUninterruptibly()" é essencial para o
                uso dos semáforos, visto que, ele adquire a permissão do semáforo
                bloqueando a thread atual até que uma permissão esteja disponível
            */
            emptyPot.acquireUninterruptibly();

            System.out.println("Cook is refilling pot...");
            coordinator.refillPot();
            System.out.println("Cook refilled pot...");

            /*
             Libera uma permissão incrementando o contador interno do semáforo (UP)
            */
            fullPot.release();
        }
    }
}
