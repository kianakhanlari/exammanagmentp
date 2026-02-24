package ir.maktab.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@NamedEntityGraph(
        name = "Exam.withQuestions",
        attributeNodes = {
                @NamedAttributeNode(value = "examQuestions", subgraph = "examQuestionsGraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "examQuestionsGraph",
                        attributeNodes = {
                                @NamedAttributeNode("question")
                        }
                )
        }
)

@Table(name = "exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private LocalDate examDate;

    private int totalQuestions;
    private double totalQuestionScore;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ExamQuestion> examQuestions = new HashSet<>();

    @OneToMany(mappedBy = "exam")
    private List<Answer> answers = new ArrayList<>();
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ExamStudent> examStudents = new HashSet<>();

    public Exam(String title, LocalDate examDate, double totalQuestionScore) {
        this.title = title;
        this.examDate = examDate;
        this.totalQuestionScore = totalQuestionScore;
    }

    public void addQuestion(Question q, double score){
        ExamQuestion eq = new ExamQuestion();
        eq.setExam(this);
        eq.setQuestion(q);
        eq.setScore(score);

        examQuestions.add(eq);
    }




}
