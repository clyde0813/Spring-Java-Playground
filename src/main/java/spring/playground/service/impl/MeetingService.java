package spring.playground.service.impl;

import spring.playground.domain.dto.MeetingDto;

public interface MeetingService {

    MeetingDto createMeeting(String name);

    MeetingDto getMeeting(String name);

    MeetingDto getMeeting(Long id);

    void joinMeeting(String meetingName, String memberName);

    void leaveMeeting(String meetingName, String memberName);
    
    void deleteMeeting(String name);
}
