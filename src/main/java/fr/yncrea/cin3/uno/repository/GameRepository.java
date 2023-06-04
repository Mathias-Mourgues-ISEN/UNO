package fr.yncrea.cin3.uno.repository;

import fr.yncrea.cin3.uno.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {

    Game findFirstByOrderByUuidDesc();
}