package ir.maktab.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDto(
        @Size(min = 3, max = 50) String username,
        @NotBlank(message = "Password is required") String password
) {

}
