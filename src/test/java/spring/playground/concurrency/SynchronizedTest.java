package spring.playground.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import static org.assertj.core.api.Assertions.assertThat;

public class SynchronizedTest {

    private int threadCount = 100;
    private int threadPool = 10;
    private int sleep = 10;

    private int testCount = 100;

    private int counter = 0;

    @BeforeEach
    void resetCounter() {
        counter = 0;
    }

    int synchronizedSituation(int threadCount, int threadPool, int sleep) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threadPool);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i=0; i<threadCount; i++) {
            executor.execute(() -> {
                synchronized (this) {
                    int temp = counter;
                    sleep(sleep);
                    counter = temp + 1;
                }
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);

        return counter;
    }

    // @Test
    void synchronizedTest() throws InterruptedException {

        int result = synchronizedSituation(threadCount, threadPool, sleep);

        System.out.println("Counter = " + result);
        assertThat(result).isEqualTo(threadCount);
    }

    @Test
    void measureFailureRateUnderSynchronized() throws InterruptedException {
        StopWatch sw = new StopWatch();
        sw.start("measureFailureRateUnderSynchronized");
        
        int failureCount = 0;

        for(int i=0; i<testCount; i++) {
            int result = synchronizedSituation(threadCount, threadPool, sleep);
            if(result != threadCount) {
                System.out.println("ThreadCount = " + threadCount + " Counter = " + result + " at iteration " + i);
                failureCount++;
            }
            counter = 0;
        }

        sw.stop();
        System.out.println(sw.prettyPrint());
        System.out.println("Failure rate = " + (double)failureCount/testCount);
        assertThat(failureCount).isEqualTo(0);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
