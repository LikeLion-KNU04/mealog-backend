package knulions.mealog.domain.comment.dto.request;


import lombok.Builder;

@Builder
public record CommentSaveRequest(String content) {
}
