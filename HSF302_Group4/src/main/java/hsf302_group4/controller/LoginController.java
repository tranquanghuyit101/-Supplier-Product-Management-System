package hsf302_group4.controller;

import hsf302_group4.entity.UserAccount;
import hsf302_group4.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller @RequiredArgsConstructor
public class LoginController {
    private final UserService userService;

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {

        Optional<UserAccount> ou = userService.login(username, password);
        if (ou.isEmpty()) {
            model.addAttribute("error", "username or password is invalid!");
            return "login";
        }
        UserAccount user = ou.get();
        if (user.getRole() != 1 && user.getRole() != 2) {
            model.addAttribute("error", "You have no permission to access this function!");
            return "login";
        }
        session.setAttribute("loginUser", user);
        return "redirect:/products";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}

