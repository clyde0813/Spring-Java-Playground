package spring.playground.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.playground.domain.entity.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    
}
