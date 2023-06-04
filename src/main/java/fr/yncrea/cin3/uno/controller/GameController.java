package fr.yncrea.cin3.uno.controller;

import fr.yncrea.cin3.uno.form.JoinLobbyForm;
import fr.yncrea.cin3.uno.form.RegisterForm;
import fr.yncrea.cin3.uno.model.Card;
import fr.yncrea.cin3.uno.model.Game;
import fr.yncrea.cin3.uno.model.Player;
import fr.yncrea.cin3.uno.model.User;
import fr.yncrea.cin3.uno.repository.CardRepository;
import fr.yncrea.cin3.uno.repository.GameRepository;
import fr.yncrea.cin3.uno.repository.UserRepository;
import fr.yncrea.cin3.uno.service.GameService;
import fr.yncrea.cin3.uno.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class GameController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private LobbyService lobbyService;
    private GameService gameService;
    private Game currentGame; // Variable pour stocker la partie en cours
    private CardRepository cardRepository;

    private Map<String, Game> currentGames;

    public GameController(GameService gameService, CardRepository cardRepository, LobbyService lobbyService) {
        this.gameService = gameService;
        this.cardRepository = cardRepository;
        this.lobbyService = lobbyService;
        this.currentGames = new HashMap<>();
    }

    @PostMapping("/game")
    public String gameAction(Model model) {

        return "redirect:/lobby";
//        if (currentGame == null) {
//            currentGame = gameService.initializeGame();
//        }
//        model.addAttribute("game", currentGame);
//
//        return "game";
    }


    @GetMapping("/game/{gameId}")
    public String game(@PathVariable String gameId, Model model) {
        Game currentGame;
        if (currentGames.containsKey(gameId)) {
            currentGame = currentGames.get(gameId);
        } else {
            List<User> users = lobbyService.getUsers(gameId);
            currentGame = gameService.initializeGame(gameId, users);
            currentGames.put(gameId, currentGame);  // add the new game to the map
        }
        User currentUser = UserRepository.getCurrentUser();  // Get the current user. This depends on your authentication setup.
        Player currentPlayer = currentGame.getPlayers().stream()
                .filter(player -> {
                    assert currentUser != null;
                    return player.getUser().getUuid().equals(currentUser.getUuid());
                })
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Current user is not part of the game"));

        model.addAttribute("game", currentGame);
        model.addAttribute("playerHand", currentPlayer.getHand());
        model.addAttribute("playerID", currentPlayer.getUuid());

        int currentPlayerNumber = currentPlayer.getNumber();

        // Assuming the numbers are from 1 to 4
        int nextPlayerNumber = (currentPlayerNumber % 4) + 1;
        int nextToNextPlayerNumber = (nextPlayerNumber % 4) + 1;
        int nextToNextToNextPlayerNumber = (nextToNextPlayerNumber % 4) + 1;

        model.addAttribute("playerLeft", nextPlayerNumber);
        model.addAttribute("playerTop", nextToNextPlayerNumber);
        model.addAttribute("playerRight", nextToNextToNextPlayerNumber);


        return "game";
    }


    @GetMapping("/cards/{gameId}/{id}")
    public ResponseEntity<Map<String, Boolean>> playCardAction(@PathVariable String gameId, @PathVariable UUID id) {
        Game currentGame = currentGames.get(gameId); // Retrieve the game using gameId

        Card card = cardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Error message"));
        boolean cardCanBePlayed = gameService.playCard(currentGame, currentGame.getCurrentPlayer(), card);
        Map<String, Boolean> response = new HashMap<>();
        response.put("cardCanBePlayed", cardCanBePlayed);
        response.put("hasWon", currentGame.getCurrentPlayer().getHand().isEmpty());
        if (cardCanBePlayed) messagingTemplate.convertAndSend("/topic/game/" + gameId, currentGame);


        return ResponseEntity.ok(response);
    }

    @GetMapping("/win")
    public String win(Model model) {
        return "win"; // Assurez-vous d'avoir une vue "win".
    }


    @GetMapping("/game/{gameId}/draw")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public RedirectView drawCardAction(Model model, @PathVariable String gameId, Authentication authentication) {
        Game currentGame = currentGames.get(gameId);

        String username = authentication.getName();

        Player currentPlayer = currentGame.getPlayers().stream()
                .filter(player -> player.getUser().getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Current user is not part of the game"));

        gameService.drawCard(currentGame, currentPlayer);
        gameService.nextPlayer(currentGame);

        model.addAttribute("game", currentGame);

        return new RedirectView("/game/{gameId}");
    }


}