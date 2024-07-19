package org.zerock.b01.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Log4j2
public class Custom403Handler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("-------------ACCESS DENIED----------------");

        response.setStatus(HttpStatus.FORBIDDEN.value());

        //JSON 요청이 있는지 확인
        String contentType = request.getHeader("Content-Type");

        boolean jsonRequset = contentType.startsWith("application/json");

        log.info("isJSON: " + jsonRequset);

        //일반 request
        if (!jsonRequset) {
            response.sendRedirect("/member/login?error=ACCESS_DENIED");
        }
    }
}
