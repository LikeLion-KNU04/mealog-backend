package knulions.mealog.domain.board.dto.response;

import lombok.Builder;
@Builder
public record BoardAllReadResponse(Long id, String writingTime, String content, String imageUrl){
}
