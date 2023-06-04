package fr.yncrea.cin3.uno.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
public class Player {
    @Id
    @GeneratedValue
    private UUID uuid;

    private String name;

    private int number;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Card> hand;

    @ManyToOne
    private Game game;


    @ManyToOne
    @JoinColumn(name = "user_uuid")
    private User user;



    public Player() {
        this.hand = new LinkedList<>();
    }

    // Constructeurs, getters et setters (à ajouter selon les besoins)
    public Player(String name, UUID uuid, User user, int number) {
        this.name = name;
        this.uuid = uuid;
        this.hand = new LinkedList<>();
        this.user = user;
        this.number = number;
    }

    public Player(String s) {
    }

    public Player(String s, User user) {
    }

    public User getUser() {
        return this.user;
    }

}