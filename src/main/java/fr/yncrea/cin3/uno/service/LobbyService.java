package fr.yncrea.cin3.uno.service;

import fr.yncrea.cin3.uno.model.Lobby;
import fr.yncrea.cin3.uno.model.User;
import fr.yncrea.cin3.uno.repository.LobbyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LobbyService {
    private final LobbyRepository lobbyRepository;

    @Transactional
    public String createLobby() {
        Lobby lobby = new Lobby();
        lobbyRepository.save(lobby);
        return lobby.getUuid().toString();
    }

    @Transactional
    public void joinLobby(String lobbyId, User user) {
        Optional<Lobby> optionalLobby = lobbyRepository.findById(UUID.fromString(lobbyId));

        if (!optionalLobby.isPresent()) {
            return;
        }

        Lobby lobby = optionalLobby.get();
        List<User> users = lobby.getUsers();

        if (users == null) {
            users = new ArrayList<>();
        }

        if (users.size() < 4 && !isUserInLobby(lobbyId, user)) {
            users.add(user);
            lobby.setUsers(users);
            lobbyRepository.save(lobby);
        }
    }

    @Transactional
    public List<User> getUsers(String lobbyId) {
        Optional<Lobby> optionalLobby = lobbyRepository.findById(UUID.fromString(lobbyId));

        if (!optionalLobby.isPresent()) {
            return null;
        }

        Lobby lobby = optionalLobby.get();
        return lobby.getUsers();
    }

    @Transactional
    public boolean isUserInLobby(String lobbyId, User user) {
        List<User> users = getUsers(lobbyId);

        if (users == null) {
            return false;
        }else if (users.isEmpty()) {
            return false;
        }else {
            for (User u : users) {
                if (u.getUuid().equals(user.getUuid())) {
                    return true;
                }
            }
        }
        return false;
    }

}
