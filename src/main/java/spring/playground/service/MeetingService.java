package spring.playground.service;

import spring.playground.domain.dto.MeetingDto;

public interface MeetingService {

    MeetingDto createMeeting(String meetingName);

    MeetingDto getMeeting(Long meetingId);

    void joinMeeting(Long meetingId, String memberName);

    void leaveMeeting(Long meetingId, String memberName);

    void deleteMeeting(Long meetingId);
}
