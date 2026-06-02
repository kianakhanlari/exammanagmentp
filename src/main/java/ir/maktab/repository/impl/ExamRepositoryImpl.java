package ir.maktab.repository.impl;

import ir.maktab.model.*;
import ir.maktab.repository.ExamRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class ExamRepositoryImpl implements ExamRepository {

    @Override
    public List<Question> findAllQuestionsByExamId(EntityManager em, long examId) {
        return em.createQuery(
                        "select  q from Question q " +
                                "join q.examQuestions eq " +
                                "where eq.exam.id = :examId", Question.class)
                .setParameter("examId", examId)
                .getResultList();
    }

    @Override
    public Exam findExamByTitleExam(EntityManager em, String title) {
        return em.createQuery("select e from Exam e where e.title = :title"
                        ,
                        Exam.class)
                .setParameter("title", title)
                .getSingleResult();
    }

    public List<Question> findQuestionsByExamId(EntityManager em, long examId) {
        return em.createQuery(
                        "SELECT eq.question FROM ExamQuestion eq " +
                                "WHERE eq.exam.id = :examId",
                        Question.class
                )
                .setParameter("examId", examId)
                .getResultList();
    }

    @Override
    public void saveAnswer(EntityManager em, Answer answer) {
        em.persist(answer);
    }

    @Override
    public void saveExamQuestion(EntityManager em, ExamQuestion examQuestion) {
        em.persist(examQuestion);
    }

    @Override
    public Exam findExamByExamId(EntityManager em, long id) {

        EntityGraph<?> graph = em.getEntityGraph("Exam.withQuestions");

        return em.createQuery(
                        "select e from Exam e where e.id = :id",
                        Exam.class)
                .setParameter("id", id)
                .setHint("jakarta.persistence.fetchgraph", graph)
                .getSingleResult();
    }
    @Override
    public ExamAttempt findAttemptByExamId(EntityManager em, Long studentId, Long examId) {

        EntityGraph<?> graph =
                em.getEntityGraph("ExamAttempt.withExamAndStudent");

        return em.createQuery(
                        "select e from ExamAttempt e " +
                                "where e.examStudent.student.id = :studentId " +
                                "and e.examStudent.exam.id = :examId",
                        ExamAttempt.class)
                .setParameter("studentId", studentId)
                .setParameter("examId", examId)
                .setHint("jakarta.persistence.loadgraph", graph)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }



    @Override
    public void saveExamAttemp(EntityManager em, ExamAttempt examAttempt) {
        em.merge(examAttempt);

    }

    @Override
    public void saveExamStudent(EntityManager em, ExamStudent examStudent) {
        em.persist(examStudent);
    }

    @Override
    public List<Answer> findAnswersByStudentIdAndExamId(EntityManager em,
                                                        long studentId,
                                                        long examId) {

        return em.createQuery(
                        "SELECT a FROM Answer a " +
                                "WHERE a.student.id = :studentId " +
                                "AND a.exam.id = :examId",
                        Answer.class
                )
                .setParameter("studentId", studentId)
                .setParameter("examId", examId)
                .getResultList();
    }

    @Override
    public ExamQuestion findExamQuestionByQuestionId(EntityManager em,
                                                     long questionId) {

        return em.createQuery(
                        "SELECT e FROM ExamQuestion e " +
                                "WHERE e.question.id = :questionId",
                        ExamQuestion.class
                )
                .setParameter("questionId", questionId)
                .getSingleResult();
    }



    @Override
    public Question findQuestionByQuestionId(EntityManager em,
                                             long questionId) {

        return em.createQuery(
                        "SELECT q FROM Question q " +
                                "WHERE q.id = :questionId",
                        Question.class
                )
                .setParameter("questionId", questionId)
                .getSingleResult();
    }

    @Override
    public ExamStudent findExamStudentByStudentIdAndExamId(
            EntityManager em,
            long studentId,
            long examId) {

        return em.createQuery(
                        "SELECT e FROM ExamStudent e " +
                                "WHERE e.student.id = :studentId " +
                                "AND e.exam.id = :examId",
                        ExamStudent.class
                )
                .setParameter("studentId", studentId)
                .setParameter("examId", examId)
                .getSingleResult();
    }

    @Override
    public void updateExamStudent(EntityManager em,ExamStudent examStudent) {
       em.merge(examStudent);

    }

    @Override
    public List<ExamStudent> findExamStudentsByExamId(EntityManager em, long examid) {
        return  em.createQuery("select e from ExamStudent e "
        +"where e.exam.id=:examId")
                .setParameter("examId",examid)
                .getResultList();
    }

    @Override
    public void updateAnswer(EntityManager em,Answer answer) {
        em.createQuery("UPDATE Answer a SET a.score = :score WHERE a.id = :id")
                .setParameter("score", answer.getScore())
                .setParameter("id", answer.getId())
                .executeUpdate();
    }

    @Override
    public ExamAttempt findAttemptByExamIdByStudentId(EntityManager em, long examId, long studentId) {

        EntityGraph<?> graph =
                em.getEntityGraph("ExamAttempt.withExamAndStudent");

        return em.createQuery(
                        "select e from ExamAttempt e " +
                                "where e.examStudent.exam.id = :examId " +
                                "and e.examStudent.student.id = :studentId",
                        ExamAttempt.class)
                .setParameter("examId", examId)
                .setParameter("studentId", studentId)
                .setHint("jakarta.persistence.loadgraph", graph)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateAttempt(EntityManager em, ExamAttempt examAttempt) {
        em.merge(examAttempt);
    }


    @Override
    public List<Question> findAllQuestion(EntityManager em) {
        return em.createQuery("select q from Question q", Question.class)
                .getResultList();
    }

    @Override
    public List<Exam> findAllExamesByCourseTitle(EntityManager em, String title) {
        return em.createQuery(
                        "SELECT e FROM Exam e WHERE e.course.title = :title", Exam.class
                )
                .setParameter("title", title)
                .getResultList();
    }

    public void saveExamQuestions(EntityManager em, ExamQuestion examQuestion) {
        em.merge(examQuestion);
    }

    @Override
    public void saveExam(EntityManager em, Exam exam) {

        em.merge(exam);
    }

    @Override
    public void saveQuestion(EntityManager em, Question question) {
        em.persist(question);
    }

    @Override
    public void removeExamByTitle(EntityManager em, String titelExam) {
        em.createQuery(
                        "DELETE FROM Exam e WHERE e.title = :title")
                .setParameter("title", titelExam)
                .executeUpdate();

    }

    @Override
    public void updateExamByTitle(EntityManager em, Exam exam) {
        em.createQuery(
                        "UPDATE Exam e " +
                                "SET e.examDate = :examDate " +
                                "WHERE e.title = :title"
                )
                .setParameter("examDate", exam.getExamDate())
                .setParameter("title", exam.getTitle())
                .executeUpdate();
    }

    @Override
    public Question findQuestionByTitle(EntityManager em, String title) {
        return em.createQuery(
                        "select q from Question q where q.title=:title", Question.class)
                .setParameter("title", title)
                .getSingleResult();
    }

    @Override
    public void updateQuestion(EntityManager em, Question question) {

        em.createQuery(
                        "UPDATE Question q SET q.text = :text WHERE q.title = :title"
                )
                .setParameter("text", question.getText())
                .setParameter("title", question.getTitle())
                .executeUpdate();

    }


}


