package knulions.mealog.domain.comment.entity;

import jakarta.persistence.*;
import knulions.mealog.domain.board.entity.Board;
import knulions.mealog.domain.member.entity.Member;
import knulions.mealog.global.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Entity
@RequiredArgsConstructor
@Getter
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "boardId")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @Builder
    public Comment(Board board, Member member,  String content)
    {
        this.board = board;
        this.member = member;
        this.content = content;
    }
}
