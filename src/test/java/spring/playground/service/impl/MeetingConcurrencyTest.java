package spring.playground.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StopWatch;

import spring.playground.domain.entity.Meeting;
import spring.playground.repository.MeetingRepository;

@SpringBootTest
@ActiveProfiles("dev")
public class MeetingConcurrencyTest {
    
    @Autowired MeetingRepository meetingRepository;
    @Autowired MeetingServiceImpl meetingService;

    @BeforeEach
    void setup() {
        meetingRepository.saveAndFlush(
            Meeting.builder().name("meeting name").build()
        );
    }

    @Test
    void testCreateMeetingConcurrency() throws InterruptedException {
        StopWatch sw = new StopWatch();
        sw.start("testCreateMeetingConcurrency");

        int threadPool = 100;
        int threadCount = 100;

        Long meetingId = meetingRepository.findByName("meeting name").get().getId();

        ExecutorService executor = Executors.newFixedThreadPool(threadPool);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i=0; i<threadCount; i++) {
            executor.execute(() -> {
                try {
                    meetingService.reserveSeatWithLock(meetingId);
                }
                catch (Exception e) {
                    System.out.println(Thread.currentThread().getName() + " error: " + e.getMessage());
                }
                finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        sw.stop();
        Meeting m = meetingRepository.findById(meetingId).get();
        System.out.println("seats = " + m.getSeats());
        System.out.println(sw.prettyPrint());
        assertThat(m.getSeats()).isEqualTo(0);
    }
}
