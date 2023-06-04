package fr.yncrea.cin3.uno.controller;

import fr.yncrea.cin3.uno.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/admin")
    public String admin(Model model, @RequestParam(defaultValue = "0") int page) {
        model.addAttribute("users", userService.getUsers(PageRequest.of(page, 5)));
        return "admin";
    }

    @PostMapping("/admin/block/{userId}")
    public String blockUser(@PathVariable String userId) {
        userService.blockUser(userId);
        return "redirect:/admin";
    }

    @PostMapping("/admin/unblock/{userId}")
    public String unblockUser(@PathVariable String userId) {
        userService.unblockUser(userId);
        return "redirect:/admin";
    }
}
