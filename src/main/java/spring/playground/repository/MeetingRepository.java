package spring.playground.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.playground.domain.entity.Meeting;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    Optional<Meeting> findByName(String meetingName);
}
