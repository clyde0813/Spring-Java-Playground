package spring.playground.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import spring.playground.domain.dto.MeetingDto;
import spring.playground.domain.entity.Meeting;
import spring.playground.repository.MeetingRepository;
import spring.playground.repository.MemberRepository;
import spring.playground.repository.ParticipantRepository;

public class MeetingServiceImplTest {

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ParticipantRepository ParticipantRepository;

    @InjectMocks
    private MeetingServiceImpl meetingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMeetingValid() {
        // given
        String meetingName = "Study Meeting";
        when(meetingRepository.findByName(meetingName)).thenReturn(Optional.empty());

        // when
        Meeting meeting = Meeting.builder().name(meetingName).build();
        when(meetingRepository.save(any(Meeting.class))).thenReturn(meeting);

        MeetingDto result = meetingService.createMeeting(meetingName);
        
        // then
        assertThat(result);
        assertThat(meetingName).isEqualTo(result.getName());
        verify(meetingRepository, times(1)).save(any(Meeting.class));
    }

    @Test
    void testCreateMeetingDuplicateNameFailure() {
        // given
        String meetingName = "meeting name";
        when(meetingRepository.findByName(meetingName)).thenReturn(Optional.of(Meeting.builder().name(meetingName).build()));

        // when
        
        // then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> meetingService.createMeeting(meetingName));
        assertThat(e.getMessage()).isEqualTo("Meeting name already exists");
    }

    @Test
    void testCreateMeetingNullNameFailure() {
        // given
        String meetingName = null;

        // when
        
        // then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> meetingService.createMeeting(meetingName));
        assertThat(e.getMessage()).isEqualTo("Meeting name is required");
    }

    @Test
    void testCreateMeetingEmptyNameFailure() {
        // given
        String meetingName = "";

        // when
        
        // then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> meetingService.createMeeting(meetingName));
        assertThat(e.getMessage()).isEqualTo("Meeting name is required");
    }

    @Test
    void testDeleteMeeting() {

    }

    @Test
    void testGetMeeting() {

    }

    @Test
    void testJoinMeeting() {

    }

    @Test
    void testLeaveMeeting() {

    }
}
