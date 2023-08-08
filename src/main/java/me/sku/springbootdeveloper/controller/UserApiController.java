package me.sku.springbootdeveloper.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.sku.springbootdeveloper.dto.AddUserRequest;
import me.sku.springbootdeveloper.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserService userService;

    @PostMapping("/user")
    public String signup(AddUserRequest request) {//가입폼에서 요청받으면 서비스 메서드를 사용해 사용자를 저장한 뒤, 로그인 페이지로 이동
        userService.save(request);//회원가입
        return "redirect:/login";//로그인페이지로 가라
    }

    @GetMapping("/logout")//logout get요청시 로그아웃 담당 핸들러인  SecurityContextLogoutHandler 의 로그아웃 메서드를 호출해서 록아웃
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

}
