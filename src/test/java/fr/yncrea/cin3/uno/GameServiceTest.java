package fr.yncrea.cin3.uno;
import fr.yncrea.cin3.uno.model.Card;
import fr.yncrea.cin3.uno.model.Game;
import fr.yncrea.cin3.uno.model.Player;
import fr.yncrea.cin3.uno.model.User;
import fr.yncrea.cin3.uno.repository.CardRepository;
import fr.yncrea.cin3.uno.repository.GameRepository;
import fr.yncrea.cin3.uno.repository.PlayerRepository;
import fr.yncrea.cin3.uno.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class GameServiceTest {

    private GameService gameService;
    private Game game;
    private Player player;

    @BeforeEach
    public void setup() {
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        GameRepository gameRepository = mock(GameRepository.class);
        CardRepository cardRepository = mock(CardRepository.class);
        gameService = new GameService(playerRepository, gameRepository, cardRepository);
        game = new Game();
        player = new Player();
    }

    @Test
    public void testDrawCard() {
        Card card = new Card("red", 1, "number", "red0");
        game.getDeck().add(card);

        gameService.drawCard(game, player);
        assertEquals(0, game.getDeck().size());
        assertEquals(1, player.getHand().size());
        assertEquals(card, player.getHand().get(0));
    }

    @Test
    public void testPlayCard() {
        Player player1 = new Player("Player 1", UUID.randomUUID(), new User(), 0);
        Player player2 = new Player("Player 2", UUID.randomUUID(), new User(), 1);

        List<Player> players = Arrays.asList(player1, player2);
        game.setPlayers(players);

        Card currentCard = new Card("blue", 2, "number", "blue2");
        game.setCurrentCard(currentCard);

        Card cardPlayed = new Card("blue", 3, "number", "blue3");
        player.getHand().add(cardPlayed);

        game.setCurrentPlayer(player); // Définir le joueur courant

        boolean result = gameService.playCard(game, player, cardPlayed);

        assertTrue(result);
        assertEquals(0, player.getHand().size());
        assertEquals(cardPlayed, game.getCurrentCard());
        assertEquals(currentCard, game.getDeck().get(game.getDeck().size() - 1));
    }

    @Test
    public void testIsWinner() {
        assertTrue(gameService.isWinner(game));

        Card card = new Card("green", 4, "number", "green4");
        game.getCurrentPlayer().getHand().add(card);

        assertFalse(gameService.isWinner(game));
    }

    @Test
    public void testPlusTwo() {
        // Create players
        Player currentPlayer = new Player();
        Player victim = new Player();
        List<Player> players = new ArrayList<>();
        players.add(currentPlayer);
        players.add(victim);

        // Create cards
        Card card1 = new Card("red", 1, "number", "red1");
        Card card2 = new Card("red", 2, "number", "red2");

        // Set up game state
        game.setPlayers(players);
        game.setCurrentPlayer(currentPlayer);
        game.getDeck().add(card1);
        game.getDeck().add(card2);

        // Call the plusTwo() method
        gameService.plusTwo(game);

        // Verify that the victim player's hand size is 2
        assertEquals(2, victim.getHand().size());
    }

    @Test
    public void testPlusFour() {
        // Create players
        Player currentPlayer = new Player();
        Player victim = new Player();
        List<Player> players = new ArrayList<>();
        players.add(currentPlayer);
        players.add(victim);

        // Create cards
        Card card1 = new Card("red", 1, "number", "red1");
        Card card2 = new Card("red", 2, "number", "red2");
        Card card3 = new Card("red", 3, "number", "red3");
        Card card4 = new Card("red", 4, "number", "red4");

        // Set up game state
        game.setPlayers(players);
        game.setCurrentPlayer(currentPlayer);
        game.getDeck().add(card1);
        game.getDeck().add(card2);
        game.getDeck().add(card3);
        game.getDeck().add(card4);

        // Call the plusFour() method
        gameService.plusFour(game);

        // Verify that the victim player's hand size is 4
        assertEquals(4, victim.getHand().size());
    }

    @Test
    public void testReverse() {
        game.setRotation(1);

        gameService.reverse(game);

        assertEquals(-1, game.getRotation());
    }

    @Test
    public void testNextPlayer() {
        Player currentPlayer = new Player();
        game.getPlayers().add(currentPlayer);
        Player nextPlayer = new Player();
        game.getPlayers().add(nextPlayer);
        game.setCurrentPlayer(currentPlayer);

        gameService.nextPlayer(game);

        assertEquals(nextPlayer, game.getCurrentPlayer());
    }

    @Test
    public void testSkipTurn() {
        Player currentPlayer = new Player();
        game.getPlayers().add(currentPlayer);
        Player nextPlayer = new Player();
        game.getPlayers().add(nextPlayer);
        Player nextNextPlayer = new Player();
        game.getPlayers().add(nextNextPlayer);
        game.setCurrentPlayer(currentPlayer);

        gameService.skipTurn(game);

        assertEquals(nextNextPlayer, game.getCurrentPlayer());
    }

    // Les tests pour isGameExist() et getGame() doivent être ajoutés séparément en fonction des spécificités de votre environnement de jeu.

}