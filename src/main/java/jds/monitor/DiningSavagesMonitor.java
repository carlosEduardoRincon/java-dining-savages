package jds.monitor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
*   Nesta solução foram utilizados os pacotes do Java 5,
*   estes foram criados justamente para fornecer recursos
*   avançados para programação concorrente e paralela
* */

public class DiningSavagesMonitor {
    /*
        Lock:
        Mecanismo mais avançado do que o recurso "synchronized" do Java,
        este permite a capacidade de adquirir e liberar explicitamente um
        bloqueio. Para esse uso foi utilizada a "interface" de implementação
        chamada como 'ReentrantLock'.

        Condition:
        Mecanismo associado com a classe "Lock" que permite uma
        comunicação entre threads mais sofisticada do que os métodos
        "wait()" e "notify".
    */
    private Lock lock;
    private Condition cook;
    private Condition savages;

    private final int potCapacity;
    private int foodInPot;

    public DiningSavagesMonitor(int potCapacity) {

        /*
            Aqui são instanciados os mecanismos para o controle
            da sincronização
        */

        this.lock = new ReentrantLock();
        this.savages = this.lock.newCondition();
        this.cook = this.lock.newCondition();

        /*
            Em sequência são setados a capacidade total da panela e o seu valor inicial,
            que corresponde ao mesmo da capacidade
        */

        this.potCapacity = potCapacity;
        this.foodInPot = potCapacity;

        System.out.println("Method: Monitor");
    }

    // Método que decrementa o valor a panela
    public void eat(int savage) {
        lock.lock(); // Adquire o bloqueio

        // CASE: Panela esteja vazia
        while (foodInPot == 0) {
            // Acorda uma thread(cozinheiro) que está na espera ("notify()")
            cook.signal();
            // Processo(selvagem) fica em aguardo até que um sinal seja emitido ("wait()")
            savages.awaitUninterruptibly();
        }

        System.out.println("Savage " + savage + " eating food " + foodInPot);
        --foodInPot;

        lock.unlock(); // Desbloqueia
    }

    // Método para preencher a panela
    public void refillPot() {
        lock.lock(); // Adquire o bloqueio

        // CASE: Panela esteja vazia
        if (foodInPot == 0) {
            foodInPot = potCapacity; // Enche a panela
            // Notifica todas as threads(selvagens) que estão em espera ("notifyAll()")
            savages.signalAll();
            System.out.println("Cook refilled pot...");
        }

        System.out.println( "Cook sleeping..." );
        // Processo(selvagem) fica em aguardo até que um sinal seja emitido ("wait()")
        cook.awaitUninterruptibly();

        lock.unlock(); // Desbloqueia
    }

    // Método principal
    public void start(int numberSavages) {
        // Os Executors simplificam a criação, execução e controle de threads em ambientes concorrentes

        // Criação das threads simbolizando os selvagens
        ExecutorService savagePool = Executors.newFixedThreadPool(numberSavages);
        for(int savage = 0; savage < numberSavages; savage++) {
            savagePool.submit(new Savage(savage, this));
        }

        // Criação de thread simbolizando o cozinheiro
        ExecutorService cookPool = Executors.newSingleThreadExecutor();
        cookPool.submit(new Cook(this));
    }

}
