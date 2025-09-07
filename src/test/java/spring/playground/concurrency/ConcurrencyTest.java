package spring.playground.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConcurrencyTest {

    private int threadCount = 100;
    private int threadPool = 10;
    private int sleep = 100;

    private int testCount = 100;

    private int counter = 0;

    @BeforeEach
    void resetCounter() {
        counter = 0;
    }

    int raceConditionSituation(int threadCount, int threadPool, int sleep) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threadPool);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i=0; i<threadCount; i++) {
            executor.execute(() -> {
                int temp = counter;
                sleep(sleep);
                counter = temp + 1;
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);

        return counter;
    }

    @Test
    void raceConditionTest() throws InterruptedException {

        int result = raceConditionSituation(threadCount, threadPool, sleep);

        System.out.println("Counter = " + result);
        assertThat(result).isEqualTo(threadCount);
    }

    @Test
    void measureFailureRateUnderRaceCondition() throws InterruptedException {
        int failureCount = 0;

        for(int i=0; i<testCount; i++) {
            int result = raceConditionSituation(threadCount, threadPool, sleep);
            if(result != threadCount) {
                System.out.println("ThreadCount = " + threadCount + " Counter = " + result + " at iteration " + i);
                failureCount++;
            }
            counter = 0;
        }

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
