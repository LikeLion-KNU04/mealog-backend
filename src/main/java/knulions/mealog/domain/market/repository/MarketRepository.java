package knulions.mealog.domain.market.repository;

import knulions.mealog.domain.market.entity.Market;
import knulions.mealog.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.yaml.snakeyaml.error.Mark;

import java.util.List;

public interface MarketRepository extends JpaRepository<Market, Long> {
    List<Market> findAllByMember(Member member);

}
