package knulions.mealog.domain.comment.repository;

import knulions.mealog.domain.board.entity.Board;
import knulions.mealog.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findByBoard(Board board);

    Board findByBoardIdAndMemberId(Long boardId, Long memberId);

    int countCommentsByBoardId(Long boardId);
}
