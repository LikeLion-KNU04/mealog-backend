package knulions.mealog.domain.market.dto.response;

import knulions.mealog.domain.Image.entity.Image;
import lombok.Builder;

import java.util.List;

@Builder
public record MarketIdReadResponse(Long marketId, String title, String content, Long price, List<String> imgUrls) {
}
