package ir.maktab.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NamedEntityGraph(
        name = "course.withExams",
        attributeNodes = {
                @NamedAttributeNode("exams")
        }
)
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToMany
    @JoinTable(
            name = "course_student",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students = new ArrayList<>();
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Exam> exams = new ArrayList<>();

    public Course(String title, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Course(String title,
                  LocalDate startDate,
                  LocalDate endDate,
                  Teacher teacher) {

        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.teacher = teacher;
    }
    public Course(String title,
                  LocalDate startDate,
                  LocalDate endDate,
                  List<Student>students) {

        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.students = students;
    }
}
