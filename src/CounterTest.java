public class CounterTest {

    static final int NUM_THREADS = 4;
    static final int OPS_PER_THREAD = 100_000;

    // Cada thread incrementa e decrementa o mesmo número de vezes.
    // Resultado esperado (correto): 0.
    // Resultado sem sincronização: valor imprevisível.

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Teste SEM sincronização ===");
        runTest(false);

        System.out.println("\n=== Teste COM sincronização ===");
        runTest(true);
    }

    static void runTest(boolean useSynchronized) throws InterruptedException {
        Counter unsync = new Counter();
        SynchronizedCounter sync = new SynchronizedCounter();

        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < OPS_PER_THREAD; j++) {
                    if (useSynchronized) {
                        sync.increment();
                        sync.decrement();
                    } else {
                        unsync.increment();
                        unsync.decrement();
                    }
                }
            });
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        int result = useSynchronized ? sync.value() : unsync.value();
        int expected = 0;
        boolean ok = result == expected;

        System.out.printf("Threads: %d | Ops por thread: %,d%n", NUM_THREADS, OPS_PER_THREAD * 2);
        System.out.printf("Resultado obtido : %d%n", result);
        System.out.printf("Resultado esperado: %d%n", expected);
        System.out.printf("Consistente: %s%n", ok ? "SIM" : "NAO — race condition detectada!");
    }
}
