package spring.playground.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spring.playground.domain.entity.Member;
import spring.playground.domain.entity.Participant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    
    private Long id;
    private String name;

    public static MemberDto from(Member member) {
        return MemberDto.builder()
            .id(member.getId())
            .name(member.getName())
            .build();
    }

    public static MemberDto from(Participant participant) {
        return MemberDto.builder()
            .id(participant.getMember().getId())
            .name(participant.getMember().getName())
            .build();
    }
}
