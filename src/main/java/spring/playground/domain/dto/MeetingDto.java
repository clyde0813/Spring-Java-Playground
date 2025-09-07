package spring.playground.domain.dto;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spring.playground.domain.entity.Meeting;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingDto {
    
    private Long id;
    private String name;
    private Set<MemberDto> participants;

    public static MeetingDto from(Meeting meeting) {
        return MeetingDto.builder()
            .id(meeting.getId())
            .name(meeting.getName())
            .participants(
                meeting.getParticipants()
                .stream().map(MemberDto::from)
                .collect(Collectors.toSet())
            )
            .build();
    }
}
