package spring.playground.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import spring.playground.domain.entity.Meeting;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    Optional<Meeting> findByName(String meetingName);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Meeting m where m.id = :meetingId")
    Optional<Meeting> findByIdWithLock(@Param("meetingId") Long meetingId);
}
