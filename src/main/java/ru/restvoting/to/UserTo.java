package ru.restvoting.to;

import lombok.*;
import ru.restvoting.HasIdAndEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.io.Serial;
import java.io.Serializable;

@Value
@EqualsAndHashCode(callSuper = true)
public class UserTo extends NamedTo implements HasIdAndEmail, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Email
    @NotBlank
    @Size(max = 128)
    String email;

    @NotBlank
    @Size(min = 5, max = 32, message = "length must be between 5 and 32 characters")
    String password;

    @ConstructorProperties({"id", "name", "email", "password"})
    public UserTo(Integer id, String name, String email, String password) {
        super(id, name);
        this.email = email;
        this.password = password;
    }

}
