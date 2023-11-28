package jds.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/*
*   Nesta solução foram utilizados os pacotes do Java 5,
*   estes foram criados justamente para fornecer recursos
*   avançados para programação concorrente e paralela
* */

public class DiningSavagesSemaphore {

    /*
        Semaphore:
        Mecanismo utilizado para controlar o acesso concorrente a um conjunto de recursos,
        contudo, diferente de outros mecanismos, ele tem capacidade de controlar o acesso
        a mais de um thread simultaneamente.
    */

    private Semaphore mutex;
    private Semaphore emptyPot;
    private Semaphore fullPot;

    private final int potCapacity;
    private int foodInPot;

    public DiningSavagesSemaphore(int potCapacity) {
        /*
            Aqui são instanciados os semáforos do "jantar", note-se que são passados
            valores nos seus construtores estes definem as permissões.
            As permissões são usadas para controlar o acesso concorrente a um recurso
            compartilhado, ou seja, quando uma thread quer acessar o recurso, ela precisa
            adquirir uma permissão, neste contexto existem dois casos:
            - Se a contagem de permissões for maior que zero, a thread adquire uma
             permissão e continua.
            - Se a contagem for zero, a thread fica bloqueada até que outra thread
            libere uma permissão.
         */
        mutex = new Semaphore(1);
        emptyPot = new Semaphore(0);
        fullPot = new Semaphore(0);

        /*
            Em sequência são alocados a capacidade total da panela e o seu valor inicial,
            que corresponde ao mesmo da capacidade
        */
        this.potCapacity = potCapacity;
        this.foodInPot = potCapacity;

        System.out.println("Method: Semaphore");
    }

    // Método que consulta o valor a panela
    public int getFoodInPot() {
        return foodInPot;
    }

    // Método que decrementa o valor a panela
    public void eat() {
        foodInPot -= 1;
    }

    // Método que preenche a panela
    public boolean refillPot() {
        foodInPot = potCapacity;
        return true;
    }

    // Métodos que retornam os semáforos definidos
    public Semaphore getMutex() {
        return mutex;
    }

    public Semaphore getEmptyPot() {
        return emptyPot;
    }

    public Semaphore getFullPot() {
        return fullPot;
    }

    // Método principal
    public void start(int numberSavages) {
        // Os Executors simplificam a criação, execução e controle de threads em ambientes concorrentes.

        // Criação das threads simbolizando os selvagens
        ExecutorService savagePool = Executors.newFixedThreadPool(numberSavages);
        for(int savage = 0; savage < numberSavages; savage++) {
            savagePool.submit(new Savage(savage,
                    this,
                    this.getMutex(),
                    this.getEmptyPot(),
                    this.getFullPot()));
        }

        // Criação de thread simbolizando o cozinheiro
        ExecutorService cookPool = Executors.newSingleThreadExecutor();
        cookPool.submit(new Cook( this,
                this.getMutex(),
                this.getEmptyPot(),
                this.getFullPot()));

    }
}