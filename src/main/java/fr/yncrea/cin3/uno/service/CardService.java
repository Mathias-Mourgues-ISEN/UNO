package fr.yncrea.cin3.uno.service;

import fr.yncrea.cin3.uno.model.Card;
import fr.yncrea.cin3.uno.repository.CardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CardService {
    private CardRepository cardRepository;
    public Card getCard(String id) {
        return cardRepository.findById(UUID.fromString(id)).orElse(null);

    }
}
