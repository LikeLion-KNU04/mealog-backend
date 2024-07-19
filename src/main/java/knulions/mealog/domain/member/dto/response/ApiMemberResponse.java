package knulions.mealog.domain.member.dto.response;


import knulions.mealog.global.spec.ApiResponseSpec;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiMemberResponse<T> extends ApiResponseSpec {

    private T data;

    @Builder
    public ApiMemberResponse(Boolean status, int code, String message, T data) {
        super(status, code, message);
        this.data = data;
    }
}
