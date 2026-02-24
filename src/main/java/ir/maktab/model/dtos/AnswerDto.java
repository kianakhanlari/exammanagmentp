package ir.maktab.model.dtos;

import ir.maktab.model.Exam;
import ir.maktab.model.Question;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {
    public long studentId;
    public Question question;
    public Exam exam;
    public String response;

}
