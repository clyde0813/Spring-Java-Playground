package spring.playground.service.impl;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import spring.playground.domain.dto.MeetingDto;
import spring.playground.domain.entity.Meeting;
import spring.playground.repository.MeetingRepository;
import spring.playground.repository.MemberRepository;
import spring.playground.repository.ParticipantRepository;
import spring.playground.service.MeetingService;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService{
    
    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;
    private final ParticipantRepository ParticipantRepository;

    @Override
    public MeetingDto createMeeting(String meetingName) {
        // blank 및 null 예외 처리
        if(meetingName == null || meetingName.isEmpty())
            throw new IllegalArgumentException("Meeting name is required");
        // 중복 이름 예외 처리
        if(meetingRepository.findByName(meetingName).isPresent())
            throw new IllegalArgumentException("Meeting name already exists");

        Meeting meeting = Meeting.builder()
        .name(meetingName)
        .build();

        meetingRepository.save(meeting);

        return MeetingDto.from(meeting);
    }

    @Override
    public MeetingDto getMeeting(Long meetingId) {
        return null;
    }

    @Override
    public void joinMeeting(Long meetingId, String memberName) {
        
    }

    @Override
    public void leaveMeeting(Long meetingId, String memberName) {
        
    }

    @Override
    public void deleteMeeting(Long meetingId) {
        
    }

    @Override
    public void reserveSeat(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow();
        meeting.reserveSeat();
        meetingRepository.save(meeting);
    }

    @Override
    @Transactional
    public void reserveSeatWithLock(Long meetingId) {
        Meeting meeting = meetingRepository.findByIdWithLock(meetingId).orElseThrow();
        meeting.reserveSeat();
    }

    @Override
    public void cancelSeat(Long meetingId) {
        
    }
}
