package spring.playground.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import static org.assertj.core.api.Assertions.assertThat;

public class ConcurrencyTest {

    private final int threadCount = TestConfig.THREAD_COUNT.getValue();
    private final int threadPool = TestConfig.THREAD_POOL_SIZE.getValue();
    private final int sleep = TestConfig.SLEEP_MILLIS.getValue();

    private final int testCount = TestConfig.TEST_COUNT.getValue();

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

    // @Test
    void raceConditionTest() throws InterruptedException {

        int result = raceConditionSituation(threadCount, threadPool, sleep);

        System.out.println("Counter = " + result);
        assertThat(result).isNotEqualTo(threadCount);
    }

    @Test
    void measureFailureRateUnderRaceCondition() throws InterruptedException {
        StopWatch sw = new StopWatch();
        sw.start("measureFailureRateUnderRaceCondition");
        
        int failureCount = 0;

        for(int i=0; i<testCount; i++) {
            int result = raceConditionSituation(threadCount, threadPool, sleep);
            if(result != threadCount) {
                failureCount++;
            }
            counter = 0;
        }

        sw.stop();
        System.out.println("Concurrency Failure Test - Expecting 100%");
        System.out.print(sw.prettyPrint());
        System.out.println("Failure rate = " + ((double)failureCount/testCount * 100) + "% \n");
        assertThat(failureCount).isNotEqualTo(0);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
