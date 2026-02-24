package ir.maktab.repository;

import ir.maktab.model.*;
import jakarta.persistence.EntityManager;

import java.util.List;

public interface ExamRepository {
    List<Question> findAllQuestion(EntityManager em);

    List<Exam> findAllExamesByCourseTitle(EntityManager em, String title);

    void saveExamQuestions(EntityManager em, ExamQuestion examQuestion);

    void saveExam(EntityManager em, Exam exam);

    void saveQuestion(EntityManager em, Question question);

    void removeExamByTitle(EntityManager em, String titelExam);

    void updateExamByTitle(EntityManager em, Exam exam);

    Question findQuestionByTitle(EntityManager em, String titelQuestion);

    void updateQuestion(EntityManager em, Question question);

    List<Question> findAllQuestionsByExamId(EntityManager em, long examId);

    Exam findExamByTitleExam(EntityManager em, String title);

    List<Question> findQuestionsByExamId(EntityManager em, long examId);

    void saveAnswer(EntityManager em, Answer answer);

    void saveExamQuestion(EntityManager em, ExamQuestion examQuestion);

    Exam findExamByExamId(EntityManager em, long id);

    ExamAttempt findAttemptByExamId(EntityManager em, Long studentId, Long examId);

    void saveExamAttemp(EntityManager em, ExamAttempt examAttempt);

    void saveExamStudent(EntityManager em, ExamStudent examStudent);

    List<Answer> findAnswersByStudentIdAndExamId(EntityManager em, long studentId, long examId);
   ExamQuestion findExamQuestionByQuestionId(EntityManager em,long questionId);
   Question findQuestionByQuestionId(EntityManager em,long questionId);
   ExamStudent findExamStudentByStudentIdAndExamId(EntityManager em,long studentId,long examId);
   void  updateExamStudent (EntityManager em,ExamStudent examStudent);
   List<ExamStudent> findExamStudentsByExamId(EntityManager em,long examid);
     void  updateAnswer(EntityManager em,Answer answer);
    ExamAttempt findAttemptByExamIdByStudentId(EntityManager em,long examid,long studentid);
    void updateAttempt(EntityManager em,ExamAttempt examAttempt);
}
