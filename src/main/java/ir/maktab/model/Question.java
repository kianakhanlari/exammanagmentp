package ir.maktab.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor

@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String text;
    @Enumerated(EnumType.STRING)
    private Option typeQuestion;
    @OneToMany(mappedBy = "question")
    private List<ExamQuestion> examQuestions = new ArrayList<>();
    private String correctAnswer;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();


    public Question(String title, String correctAnswer) {
        this.title=title;
        this.correctAnswer=correctAnswer;
    }
}
