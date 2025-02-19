package vn.edu.usth.mcma.backend.dto.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAdmin {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}
