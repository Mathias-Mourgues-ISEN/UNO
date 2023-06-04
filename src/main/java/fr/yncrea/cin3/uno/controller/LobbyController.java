package fr.yncrea.cin3.uno.controller;

import fr.yncrea.cin3.uno.form.JoinLobbyForm;
import fr.yncrea.cin3.uno.model.User;
import fr.yncrea.cin3.uno.service.LobbyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class LobbyController {
    private LobbyService lobbyService;

    @GetMapping("/lobby")
    public String lobby(Model model) {
        model.addAttribute("form", new JoinLobbyForm());
        return "lobby";
    }

    @PostMapping("/lobby")
    public String lobbyAction(Model model) {
        model.addAttribute("form", new JoinLobbyForm());
        return "lobby";
    }

    @GetMapping("/create")
    public String create(@AuthenticationPrincipal User user) {
        String lobbyId = lobbyService.createLobby();

        lobbyService.joinLobby(lobbyId, user);

        return "redirect:/waiting4players/" + lobbyId;
    }

    @PostMapping("/join")
    public String joinLobbyByForm(@Valid @ModelAttribute("form") JoinLobbyForm form, BindingResult result, Model model, @AuthenticationPrincipal User user) {
        if (result.hasErrors()) {
            model.addAttribute("form", form);
            return "lobby";
        }
        String lobbyId = form.getLobbyId();


        lobbyService.joinLobby(lobbyId, user);


        return "redirect:/waiting4players/" + lobbyId;
    }

    @GetMapping("/waiting4players")
    public String waiting4players() {
        return "lobby";
    }

    @GetMapping("/waiting4players/{lobbyId}")
    public String waiting4players(@PathVariable String lobbyId, Model model) {
        List<User> users = lobbyService.getUsers(lobbyId);

        if (users == null) {
            return "redirect:/lobby";
        }

        if(users.size() >= 4) {
            return "redirect:/game/" + lobbyId;
        }

        model.addAttribute("lobbyId", lobbyId);
        model.addAttribute("users", users);

        return "waiting4players";
    }
}


