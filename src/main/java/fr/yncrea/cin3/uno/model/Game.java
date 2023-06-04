package fr.yncrea.cin3.uno.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
@Entity
@Table
public class Game {
    @Id
    @GeneratedValue
    private UUID uuid;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "game", cascade = CascadeType.ALL)
    private List<Player> players;

    @OrderColumn(name = "orderId")
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Card> deck;

    @OneToOne(cascade = CascadeType.ALL)
    private Player currentPlayer;

    private int rotation;

    @OneToOne(cascade = CascadeType.ALL)
    private Card currentCard;

    // Constructeur sans argument
    public Game() {
        this.players = new LinkedList<>();
        this.rotation = 1;
        this.deck = new LinkedList<>();
        this.currentPlayer = new Player();
    }

    // Autres constructeurs, getters et setters

    public Game(UUID uuid) {
        this.uuid = uuid;
        this.players = new LinkedList<>();
        this.rotation = 1;
        this.deck = new LinkedList<>();
        this.currentPlayer = new Player();
    }


    // Autres méthodes et logique métier
}