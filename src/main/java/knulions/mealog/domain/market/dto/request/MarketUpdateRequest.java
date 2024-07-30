package knulions.mealog.domain.market.dto.request;

import lombok.Builder;

@Builder
public record MarketUpdateRequest(String title, Long price, String content){
}
