package fr.yncrea.cin3.uno.model;

import fr.yncrea.cin3.uno.service.GameService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
public class Card {
    @Id
    @GeneratedValue
    private UUID uuid;

    private String color;

    private int number;

    private String symbol;

    private String link;

    public Card() {
    }

    public Card(String color, int number, String symbol, String link) {
        this.color = color;
        this.number = number;
        this.symbol = symbol;
        this.link = link;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Card card = (Card) obj;
        // Compare the attributes of 'this' Card and 'card'
        return Objects.equals(uuid, card.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}