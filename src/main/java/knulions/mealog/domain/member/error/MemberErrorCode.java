package knulions.mealog.domain.member.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode {
    // 409 Member 회원가입 이메일 중복 관련 오류
    MEMBER_ALREADY_EXISTS_EMAIL(HttpStatus.CONFLICT,
            "Member의 Email이 중복됩니다."),

    // 400 Member 회원가입 이메일 형식이 잘못 됨
    MEMBER_NOT_VALID_EMAIL(HttpStatus.BAD_REQUEST,
            "Member의 Email 형식이 잘못 되었습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
