package knulions.mealog.auth.itself.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record SignUpRequestDto(@NotEmpty(message = "NickName은 null일 수 없습니다")
                               String nickName,
                               @Email(message = "Email의 형식이 잘못되었습니다")
                               String email,
                               @NotEmpty(message = "Password는 null일 수 없습니다")
                               String password) {
}
