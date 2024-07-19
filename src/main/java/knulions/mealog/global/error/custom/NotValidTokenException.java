package knulions.mealog.global.error.custom;

import knulions.mealog.auth.jwt.dto.response.TokenStatus;
import knulions.mealog.auth.jwt.error.JwtErrorCode;
import lombok.Getter;

@Getter
public class NotValidTokenException extends RuntimeException{

    private JwtErrorCode errorCode;
    private TokenStatus tokenStatus;

    public NotValidTokenException(JwtErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public NotValidTokenException(JwtErrorCode errorCode, TokenStatus tokenStatus) {
        this.errorCode = errorCode;
        this.tokenStatus = tokenStatus;
    }
}
