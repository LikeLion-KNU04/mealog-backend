package knulions.mealog.domain.board.repository;

import knulions.mealog.domain.board.entity.Board;
import knulions.mealog.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAllByMember(Member member);

}

