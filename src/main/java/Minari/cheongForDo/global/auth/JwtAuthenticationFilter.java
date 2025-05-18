package Minari.cheongForDo.global.auth;

import Minari.cheongForDo.global.exception.CustomErrorCode;
import Minari.cheongForDo.global.response.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/member/login") || path.equals("/member/register");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null) {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            if (!jwtUtils.isExpired(token)) {
                Authentication authentication = jwtUtils.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                setErrorResponse(response, CustomErrorCode.JWT_WAS_EXPIRED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    public void setErrorResponse(
            HttpServletResponse response,
            CustomErrorCode errorCode
    ) throws IOException {
        response.setStatus(errorCode.getCode().value());
        response.setContentType("application/json;charset=UTF-8");

        response.getWriter().write(
                objectMapper.writeValueAsString(
                        BaseResponse.of(
                                false,
                                errorCode.getStatus(),
                                errorCode.getMessage(),
                                null
                        )
                )
        );
    }
}