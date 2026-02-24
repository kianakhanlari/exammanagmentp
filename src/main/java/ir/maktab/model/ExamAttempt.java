package ir.maktab.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@NamedEntityGraph(
        name = "ExamAttempt.withExamAndStudent",
        attributeNodes = {
                @NamedAttributeNode("examStudent")
        }
)
@AllArgsConstructor


public class ExamAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   @OneToOne
   private ExamStudent examStudent;

    private LocalDateTime startTime;
    private String  remainingTime;

    private int currentQuestionIndex;
    @Enumerated(EnumType.STRING)

    private Status status = Status.IN_PROGRESS;

}
