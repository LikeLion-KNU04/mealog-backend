package knulions.mealog.domain.Image.entity;

import jakarta.persistence.*;
import knulions.mealog.domain.board.entity.Board;
import knulions.mealog.global.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long image_id;

    private String fileName;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    private String url;


    @Builder
    public Image(String fileName,String url, Board board){
        this.fileName=fileName;
        this.url =url;
        this.board=board;
    }

    public void update() {

    }
}

