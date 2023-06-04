package fr.yncrea.cin3.uno.repository;

import fr.yncrea.cin3.uno.model.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LobbyRepository extends JpaRepository<Lobby, UUID> {

}
