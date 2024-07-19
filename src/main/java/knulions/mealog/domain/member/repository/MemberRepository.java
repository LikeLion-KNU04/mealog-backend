package knulions.mealog.domain.member.repository;

import knulions.mealog.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByRefresh(String refresh);
    boolean existsByEmail(String email);
}
