package knulions.mealog.domain.market.dto.response;

import lombok.Builder;

@Builder
public record MarketAllReadResponse(Long marketId, String title, String content, Long price, String imageUrl){
}
