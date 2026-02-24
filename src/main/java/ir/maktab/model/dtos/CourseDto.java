package ir.maktab.model.dtos;

import ir.maktab.model.Exam;
import ir.maktab.model.Student;
import ir.maktab.model.Teacher;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class CourseDto {
    public int id;
    @NotBlank(message = "Title must not be blank")
    public String title;
    @NotNull(message = "Start date must not be null")
    public LocalDate startDate;
    @NotNull(message = "End date must not be null")
    @FutureOrPresent(message = "End date must be today or in the future")
    public LocalDate endDate;
    public Teacher teacher;
    public List<String> students = new ArrayList<>();
    public List<Exam> exams = new ArrayList<>();

}
