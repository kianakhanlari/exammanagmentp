package ir.maktab.model.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class StudentDto {
    @NotBlank(message ="Name is required")
    @Size(min = 3, max = 50)
    public String userName;
    public String passWord;
    public String fullName;
    public String studentNumber;
    @Email
    public String email;
}
