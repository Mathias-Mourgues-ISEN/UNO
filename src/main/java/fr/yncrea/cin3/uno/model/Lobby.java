package fr.yncrea.cin3.uno.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"lobby\"")
public class Lobby {
    @Id
    @GeneratedValue
    private UUID uuid;

    @ManyToMany
    private List<User> users;

    @OneToOne
    private Game game;

}