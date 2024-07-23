package knulions.mealog.domain.board.entity;

import jakarta.persistence.*;
import knulions.mealog.domain.Image.entity.Image;
import knulions.mealog.domain.board.dto.request.BoardUpdateRequest;
import knulions.mealog.domain.comment.entity.Comment;
import knulions.mealog.domain.member.entity.Member;
import knulions.mealog.global.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="program_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "content")
    private String content;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Board(Member member, String content){
        this.member = member;
        this.content = content;
    }

    public void addImageList(Image image) {
        images.add(image);
        image.setBoard(this);
    }


    public void updateBoard(BoardUpdateRequest boardUpdateRequest) {
        this.content = boardUpdateRequest.content();
    }

}
