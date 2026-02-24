package ir.maktab.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Student extends User {
    private String studentNumber;


    @OneToMany(mappedBy = "student")
    private List<Answer> answers = new ArrayList<>();
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ExamStudent> examStudents = new HashSet<>();
    @ManyToMany(mappedBy = "students")
    private List<Course> enrolledCourses = new ArrayList<>();


}
