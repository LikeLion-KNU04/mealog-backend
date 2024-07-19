package knulions.mealog.global.error.custom;

import knulions.mealog.domain.member.error.MemberErrorCode;
import lombok.Getter;

@Getter
public class MemberException extends RuntimeException{

    private MemberErrorCode errorCode;

    public MemberException(MemberErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
