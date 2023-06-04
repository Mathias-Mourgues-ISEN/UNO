package fr.yncrea.cin3.uno.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterForm {

    @Size(min = 2, max = 40)
    private String pseudo;

    @Size(min = 4, max = 120)
    @Email
    private String username;

    @NotEmpty
    private String password;
}
