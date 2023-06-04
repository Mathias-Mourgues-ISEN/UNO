package fr.yncrea.cin3.uno.controller;

import fr.yncrea.cin3.uno.form.RegisterForm;
import fr.yncrea.cin3.uno.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    private UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("form", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String registerAction(@Valid @ModelAttribute("form") RegisterForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("form", form);

            return "register";
        }

        userService.createAccount(form);

        return "redirect:/";
    }
}
