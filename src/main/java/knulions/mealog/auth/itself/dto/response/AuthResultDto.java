package knulions.mealog.auth.itself.dto.response;


import knulions.mealog.global.spec.ApiResponseSpec;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthResultDto<T> extends ApiResponseSpec {
    private T data;

    @Builder
    public AuthResultDto(Boolean status, int code, String message, T data) {
        super(status, code, message);
        this.data = data;
    }
}