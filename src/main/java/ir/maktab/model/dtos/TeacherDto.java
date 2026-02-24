package ir.maktab.model.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class TeacherDto {
    @NotBlank(message ="Name is required")
    @Size(min = 3, max = 50)
    public String userName;
    @NotBlank(message = "Password is required")
    public String passWord;
    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters")
    public String fullName;
    @Email
    public String email;
    @NotBlank(message = "Specialty is required")
    public String specialty;

}
