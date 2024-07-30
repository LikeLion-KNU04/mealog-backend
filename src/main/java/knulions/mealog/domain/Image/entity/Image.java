package knulions.mealog.domain.Image.entity;

import jakarta.persistence.*;
import knulions.mealog.domain.board.entity.Board;
import knulions.mealog.domain.market.entity.Market; // Market 엔티티 임포트
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

    @ManyToOne
    @JoinColumn(name = "market_id")
    private Market market;

    private String url;

    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    @Builder
    public Image(String fileName, String url, Board board, Market market, ImageType imageType) {
        this.fileName = fileName;
        this.url = url;
        this.board = board;
        this.market = market;
        this.imageType = imageType;
    }

    public void update() {
        // update 메서드 구현
    }
}
