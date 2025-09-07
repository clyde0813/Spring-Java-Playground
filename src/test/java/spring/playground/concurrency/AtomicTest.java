package spring.playground.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import static org.assertj.core.api.Assertions.assertThat;

public class AtomicTest {

    private int threadCount = 100;
    private int threadPool = 10;
    private int sleep = 10;

    private int testCount = 100;

    private AtomicInteger counter = new AtomicInteger(0);

    @BeforeEach
    void resetCounter() {
        counter = new AtomicInteger(0);
    }

    int atomicSituation(int threadCount, int threadPool, int sleep) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threadPool);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i=0; i<threadCount; i++) {
            executor.execute(() -> {
                    sleep(sleep);
                    counter.incrementAndGet();
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);

        return counter.get();
    }

    // @Test
    void atomicTest() throws InterruptedException {

        int result = atomicSituation(threadCount, threadPool, sleep);

        System.out.println("Counter = " + result);
        assertThat(result).isEqualTo(threadCount);
    }

    @Test
    void measureFailureRateUnderAtomic() throws InterruptedException {
        StopWatch sw = new StopWatch();
        sw.start("measureFailureRateUnderAtomic");
        
        int failureCount = 0;

        for(int i=0; i<testCount; i++) {
            int result = atomicSituation(threadCount, threadPool, sleep);
            if(result != threadCount) {
                System.out.println("ThreadCount = " + threadCount + " Counter = " + result + " at iteration " + i);
                failureCount++;
            }
            counter = new AtomicInteger(0);
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
