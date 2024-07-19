package knulions.mealog.global.error.custom;

import lombok.Getter;

@Getter
public class CookieNotFoundException extends RuntimeException{
    public CookieNotFoundException() {
    }

    public CookieNotFoundException(String message) {
        super(message);
    }

    public CookieNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CookieNotFoundException(Throwable cause) {
        super(cause);
    }

    public CookieNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
