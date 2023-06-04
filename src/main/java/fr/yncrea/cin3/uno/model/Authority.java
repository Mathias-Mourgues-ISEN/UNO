package fr.yncrea.cin3.uno.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Authority implements GrantedAuthority {
    @Id
    @GeneratedValue
    private UUID uuid;

    private String authority;
}
