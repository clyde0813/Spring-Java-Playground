package spring.playground.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.playground.domain.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    
}
