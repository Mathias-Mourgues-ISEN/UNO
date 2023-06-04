package fr.yncrea.cin3.uno.service;

import fr.yncrea.cin3.uno.form.RegisterForm;
import fr.yncrea.cin3.uno.model.User;
import fr.yncrea.cin3.uno.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    private PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Transactional
    public Page<User> getUsers(Pageable pageable) {
        int start = (int) pageable.getOffset();
        List<User> users = userRepository.findAll();
        int end = Math.min((start + pageable.getPageSize()), users.size());
        return new PageImpl<>(users.subList(start, end), pageable, users.size());
    }


    @Transactional
    public void blockUser(String userId) {
        var users = userRepository.findAll();
        for (User user : users) {
            if (user.getUuid().toString().equals(userId)) {
                user.setEnabled(false);
                userRepository.save(user);
            }
        }
    }

    @Transactional
    public void unblockUser(String userId) {
        var users = userRepository.findAll();
        for (User user : users) {
            if (user.getUuid().toString().equals(userId)) {
                user.setEnabled(true);
                userRepository.save(user);
            }
        }
    }


    @Transactional
    public void createAccount(RegisterForm form) {
        var user = new User();
        user.setPseudo(form.getPseudo());
        user.setUsername(form.getUsername());
        user.setEnabled(true);

        var hashedPassword = encoder.encode(form.getPassword());
        user.setPassword(hashedPassword);

        userRepository.save(user);
    }


}
