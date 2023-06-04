package fr.yncrea.cin3.uno.service;

import fr.yncrea.cin3.uno.model.Card;
import fr.yncrea.cin3.uno.model.Game;
import fr.yncrea.cin3.uno.model.Player;
import fr.yncrea.cin3.uno.model.User;
import fr.yncrea.cin3.uno.repository.CardRepository;
import fr.yncrea.cin3.uno.repository.GameRepository;
import fr.yncrea.cin3.uno.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class GameService {

    private final PlayerRepository playerRepository;

    private final GameRepository gameRepository;

    private final CardRepository cardRepository;


    public Game initializeGame(String lobbyId, List<User> users) {
        Game game = new Game(UUID.fromString(lobbyId));

        Player player1 = new Player(users.get(0).getUsername(), users.get(0).getUuid(), users.get(0), 0);
        Player player2 = new Player(users.get(1).getUsername(), users.get(1).getUuid(), users.get(1), 1);
        Player player3 = new Player(users.get(2).getUsername(), users.get(2).getUuid(), users.get(2), 2);
        Player player4 = new Player(users.get(3).getUsername(), users.get(3).getUuid(), users.get(3), 3);

        List<Player> players = Arrays.asList(player1, player2, player3, player4);
        game.setPlayers(players);

        Random random = new Random();
        Player firstPlayer = players.get(random.nextInt(players.size()));
        game.setCurrentPlayer(firstPlayer);
        System.out.println(game.getCurrentPlayer().getNumber());

        var cards = cardRepository.findAll();
        Collections.shuffle(cards);
        game.setDeck(cards);

        for (int i = 0; i < 7; i++) {
            for (Player player : players) {
                player.getHand().add(game.getDeck().remove(0));
            }
        }
        while (game.getDeck().get(0).getNumber() == -1){
            game.getDeck().add(game.getDeck().get(0));
            game.getDeck().remove(0);

        }
        game.setCurrentCard(game.getDeck().remove(0));

        gameRepository.save(game);
        playerRepository.saveAll(Arrays.asList(player1, player2, player3, player4));
        return game;
    }


    //BASIC GAME LOGIC
    public void drawCard(Game game, Player player) {
        // Récupération de la carte du dessus du deck
        Card card = game.getDeck().remove(0);
        // Ajout de la carte à la main du joueur
        player.getHand().add(card);
    }

    public boolean playCard(Game game, Player player, Card cardPlayed) {
        // Récupération de la carte du dessus du deck
        Card currentCard = game.getCurrentCard();


        if (player.equals(game.getCurrentPlayer())) {
            // Vérification que la carte peut être jouée
            if (cardPlayed.getColor().equals(currentCard.getColor()) ||
                    cardPlayed.getNumber() == currentCard.getNumber() && cardPlayed.getNumber() != -1 ||
                    cardPlayed.getColor().equals("black") ||
                    currentCard.getColor().equals("black") ||
                    Objects.equals(cardPlayed.getSymbol(), currentCard.getSymbol()) && cardPlayed.getNumber() == -1) {
                //mettre currentcard a la fin de la pioche deck
                game.getDeck().add(currentCard);
                // Suppression de la carte de la main du joueur
                player.getHand().remove(cardPlayed);
                // Mise à jour de la carte du dessus du deck
                game.setCurrentCard(cardPlayed);

                if(cardPlayed.getSymbol().equals("reverse")) {
                    System.out.println("reverse");
                    reverse(game);
                }
                if(cardPlayed.getSymbol().equals("skip")) {
                    System.out.println("skip");
                    skipTurn(game);
                }
                if(cardPlayed.getSymbol().equals("draw_two")) {
                    System.out.println("draw_two");
                    plusTwo(game);
                }
                if(cardPlayed.getSymbol().equals("wild_draw_four")) {
                    System.out.println("draw_four");
                    plusFour(game);
                }

                nextPlayer(game);
                return true;
            }
        }

        return false;
    }

    //WIN
    public boolean isWinner(Game game) {
        for (Player player : game.getPlayers()) {
            if (player.getHand().isEmpty()) {
                return true;
            }
        }
        return false;
    }


    //EFFECTS
    public void plusTwo(Game game) {

        Player currentPlayer = game.getCurrentPlayer();
        int currentIndex = game.getPlayers().indexOf(currentPlayer);

        int victimIndex = (currentIndex + game.getRotation()) % game.getPlayers().size();

        if (victimIndex < 0) {
            victimIndex += game.getPlayers().size();
        }
        Player victim = game.getPlayers().get(victimIndex);

        for (int i = 0; i < 2; i++) {
            drawCard(game, victim);
        }
        nextPlayer(game);
    }

    public void plusFour(Game game) {

        Player currentPlayer = game.getCurrentPlayer();
        int currentIndex = game.getPlayers().indexOf(currentPlayer);

        int victimIndex = (currentIndex + game.getRotation()) % game.getPlayers().size();

        if (victimIndex < 0) {
            victimIndex += game.getPlayers().size();
        }
        Player victim = game.getPlayers().get(victimIndex);

        for (int i = 0; i < 4; i++) {
            drawCard(game, victim);
        }
        nextPlayer(game);
    }

    public void reverse(Game game) {
        game.setRotation(-game.getRotation());
    }

    //nextPlayer
    public void nextPlayer(Game game) {
        Player currentPlayer = game.getCurrentPlayer();
        int currentIndex = game.getPlayers().indexOf(currentPlayer);

        int nextIndex = (currentIndex + game.getRotation()) % game.getPlayers().size();
        if (nextIndex < 0) {
            nextIndex += game.getPlayers().size();
        }

        Player nextPlayer = game.getPlayers().get(nextIndex);
        game.setCurrentPlayer(nextPlayer);
    }

    public void skipTurn(Game game) {
        nextPlayer(game);
    }

    @Transactional
    public boolean isGameExist(String lobbyId) {
        return gameRepository.existsById(UUID.fromString(lobbyId));
    }

    @Transactional
    public Game getGame(String lobbyId) {
        return gameRepository.findById(UUID.fromString(lobbyId)).get();
    }
}