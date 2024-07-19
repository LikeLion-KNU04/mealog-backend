package knulions.mealog.auth.jwt.dto.response;


import knulions.mealog.global.spec.ApiResponseSpec;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiJwtResponse extends ApiResponseSpec {
    private TokenStatus tokenStatus;

    @Builder
    public ApiJwtResponse(Boolean status, int code, String message, TokenStatus tokenStatus) {
        super(status, code, message);
        this.tokenStatus = tokenStatus;
    }
}