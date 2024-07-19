package knulions.mealog.auth.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import knulions.mealog.auth.jwt.dto.response.ApiJwtResponse;
import knulions.mealog.auth.jwt.dto.response.TokenStatus;
import knulions.mealog.global.error.custom.NotValidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (NotValidTokenException e) {
            handleException(response, e.getErrorCode().getHttpStatus(), e.getErrorCode().getMessage(), e.getTokenStatus());
        } catch (UsernameNotFoundException e) {
            handleException(response, HttpStatus.NOT_FOUND, e.getMessage(), TokenStatus.VALID);
        }
    }

    private void handleException(HttpServletResponse response, HttpStatus status, String message, TokenStatus tokenStatus) throws IOException {
        ApiJwtResponse apiJwtResponse = ApiJwtResponse.builder()
                .status(false)
                .code(status.value())
                .message(message)
                .tokenStatus(tokenStatus)
                .build();

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getWriter(), apiJwtResponse);
    }


}
