package spring.playground.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import spring.playground.repository.MeetingRepository;

@SpringBootTest
@Transactional
public class MeetingConcurrencyTest {
    
    @Autowired MeetingRepository meetingRepository;

    @Test
    void optimisticLockTest()

}
