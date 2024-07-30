package knulions.mealog.domain.market.dto.request;

import lombok.Builder;

@Builder
public record MarketSaveRequest(String title, Long price, String content) {
}
