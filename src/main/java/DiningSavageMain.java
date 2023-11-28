import jds.monitor.DiningSavagesMonitor;
import jds.semaphore.DiningSavagesSemaphore;

public class DiningSavageMain {

    public static void main( String[] args ) {

        // Define variáveis de entrada
        String method = "monitor";
        int numberSavages = 30;
        int potCapacity = 5;

        // Seleciona qual o método de resolução do problema
        if(method.equals("monitor")){
            new DiningSavagesMonitor(potCapacity).start(numberSavages);
        } else if (method.equals("semaphore")) {
            new DiningSavagesSemaphore(potCapacity).start(numberSavages);
        }
    }
}

