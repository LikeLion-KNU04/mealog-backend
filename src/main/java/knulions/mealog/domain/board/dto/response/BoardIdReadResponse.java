package knulions.mealog.domain.board.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record BoardIdReadResponse(Long boardId, String memberName, String memberEmail, String writingTime, String content, List<String> images) {
}
