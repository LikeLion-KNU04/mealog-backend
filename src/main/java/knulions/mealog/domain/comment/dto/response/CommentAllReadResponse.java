package knulions.mealog.domain.comment.dto.response;

import lombok.Builder;

@Builder
public record CommentAllReadResponse(Long commentId, String content, String writingTime) {
}
