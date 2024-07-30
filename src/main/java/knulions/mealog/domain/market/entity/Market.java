package knulions.mealog.domain.market.entity;

import jakarta.persistence.*;
import knulions.mealog.domain.Image.entity.Image;
import knulions.mealog.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="market_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private Long price;

    @Column(name = "content")
    private String content;

    @OneToMany(mappedBy = "market", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @Builder
    public Market(Member member,String title, Long price, String content){
        this.member = member;
        this.title = title;
        this.price = price;
        this.content = content;
    }




}
