package spring.playground.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import static org.assertj.core.api.Assertions.assertThat;

public class ReentrantTest {

    private int threadCount = TestConfig.THREAD_COUNT.getValue();
    private int threadPool = TestConfig.THREAD_POOL_SIZE.getValue();
    private int sleep = TestConfig.SLEEP_MILLIS.getValue();

    private int testCount = TestConfig.TEST_COUNT.getValue();

    private int counter = 0;

    private final ReentrantLock lock = new ReentrantLock();

    @BeforeEach
    void resetCounter() {
        counter = 0;
    }

    int reentrantSituation(int threadCount, int threadPool, int sleep) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threadPool);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i=0; i<threadCount; i++) {
            executor.execute(() -> {
                lock.lock();
                try {
                    int temp = counter;
                    sleep(sleep);
                    counter = temp + 1;
                }
                finally {
                    lock.unlock();
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);

        return counter;
    }

    // @Test
    void reentrantTest() throws InterruptedException {

        int result = reentrantSituation(threadCount, threadPool, sleep);

        System.out.println("Counter = " + result);
        assertThat(result).isEqualTo(threadCount);
    }

    @Test
    void measureFailureRateUnderReentrant() throws InterruptedException {
        StopWatch sw = new StopWatch();
        sw.start("measureFailureRateUnderReentrant");
        
        int failureCount = 0;

        for(int i=0; i<testCount; i++) {
            int result = reentrantSituation(threadCount, threadPool, sleep);
            if(result != threadCount) {
                failureCount++;
            }
            counter = 0;
        }

        sw.stop();
        System.out.println(sw.prettyPrint());
        System.out.println("Failure rate = " + ((double)failureCount/testCount * 100) + "%");
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
