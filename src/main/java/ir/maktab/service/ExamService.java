package ir.maktab.service;

import ir.maktab.model.*;
import ir.maktab.model.dtos.AnswerDto;
import ir.maktab.repository.ExamRepository;

import ir.maktab.util.JpaUtil;
import ir.maktab.util.ValidationUtil;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ExamService {
    private final ExamRepository examRepository;
    private final StudentService studentService;

    public ExamService(ExamRepository examRepository, StudentService studentService) {

        this.examRepository = examRepository;

        this.studentService = studentService;
    }

    public List<Question> showQuestionBank() {
        EntityManager em = JpaUtil.getEntityManager();
        return JpaUtil.executeTransaction(em, () ->
                examRepository.findAllQuestion(em)
        );
    }

    public List<Exam> findAllExamesByCourseTitle(String title) {
        EntityManager em = JpaUtil.getEntityManager();
        return JpaUtil.executeTransaction(em, () ->
                examRepository.findAllExamesByCourseTitle(em, title)
        );
    }

    /*  public void saveQuestion(Question question){
          EntityManager em = JpaUtil.getEntityManager();
          JpaUtil.executeTransaction(em, () -> {
              examRepository.saveQuestion(em,question);
              return null;
          });

      }*/
    public void saveExam(Exam exam) {
        ValidationUtil.validate(exam);
        EntityManager em = JpaUtil.getEntityManager();
        JpaUtil.executeTransaction(em, () -> {

            examRepository.saveExam(em, exam);
            return null;
        });

    }

    public double calculateTotalScore(long examId) {
        EntityManager em = JpaUtil.getEntityManager();

        return JpaUtil.executeTransaction(em, () -> {
            List<Question> totalQuestion = examRepository.findAllQuestionsByExamId(em, examId);

            double totalScore = totalQuestion.stream()
                    .flatMap(q -> q.getExamQuestions().stream())
                    .mapToDouble(ExamQuestion::getScore)
                    .sum();

            return totalScore;
        });
    }

    public void removeExamByTitle(String titelExam) {
        EntityManager em = JpaUtil.getEntityManager();
        JpaUtil.executeTransaction(em, () -> {
            examRepository.removeExamByTitle(em, titelExam);
            return null;
        });
    }

    public void updateExamByTitle(Exam exam) {

        EntityManager em = JpaUtil.getEntityManager();
        JpaUtil.executeTransaction(em, () -> {
            examRepository.updateExamByTitle(em, exam);
            return null;
        });

    }

    public Question findQuestionByTitle(String titelQuestion) {
        EntityManager em = JpaUtil.getEntityManager();

        return JpaUtil.executeTransaction(em,
                () -> examRepository.findQuestionByTitle(em, titelQuestion)
        );
    }

    public void updateQuestion(Question question) {
        EntityManager em = JpaUtil.getEntityManager();

        JpaUtil.executeTransaction(em, () -> {
            examRepository.updateQuestion(em, question);
            return null;
        });

    }

    public List<Question> findQuestionsByExamTitle(String title) {
        Exam exam = findExamByTitleExam(title);

        EntityManager em = JpaUtil.getEntityManager();

        return JpaUtil.executeTransaction(em, () -> {


            List<Question> totalQuestion =
                    examRepository.findAllQuestionsByExamId(em, exam.getId());

            return totalQuestion;
        });
    }

    public Exam findExamByTitleExam(String title) {

        EntityManager em = JpaUtil.getEntityManager();

        return JpaUtil.executeTransaction(em, () -> {

            Exam exam = examRepository.findExamByTitleExam(em, title);
            if (exam == null) {
                System.out.println("هیج ازمونی با این عنوان وجود ندارد");
            }
            return exam;
        });

    }

    public List<Question> findQuestionsByExamId(long examId) {

        EntityManager em = JpaUtil.getEntityManager();

        return JpaUtil.executeTransaction(em, () -> {

            List<Question> questions = examRepository.findQuestionsByExamId(em, examId);
            if (questions.isEmpty()) {
                throw new RuntimeException("there is not any question");
            }
            return questions;
        });
    }

    public void saveAnswer(AnswerDto answerDto) {
        EntityManager em = JpaUtil.getEntityManager();
        JpaUtil.executeTransaction(em, () -> {
            Answer answer = new Answer();

            answer.setResponse(answerDto.response);

            Exam exam = findExamByExamId(answerDto.exam.getId());
            answer.setExam(exam);
            Student student = studentService.findStudentByUserId(answerDto.studentId);
            answer.setStudent(student);
            answer.setQuestion(answerDto.question);
            examRepository.saveAnswer(em, answer);
            return null;
        });
    }

    public Exam findExamByExamId(long id) {

        EntityManager em = JpaUtil.getEntityManager();
        return JpaUtil.executeTransaction(em, () -> {

            Exam exam = examRepository.findExamByExamId(em, id);
            if (exam == null) {
                throw new RuntimeException("exam not found");

            }
            return exam;
        });
    }


    public void saveExamQuestion(ExamQuestion examQuestion) {


        EntityManager em = JpaUtil.getEntityManager();
        JpaUtil.executeTransaction(em, () -> {

            examRepository.saveExamQuestion(em, examQuestion);
            return null;
        });
    }

    public ExamAttempt findAttemptByExamId(Long studentId, Long examId) {
        EntityManager em = JpaUtil.getEntityManager();
        return JpaUtil.executeTransaction(em, () -> {

            return examRepository.findAttemptByExamId(em, studentId, examId);

        });
    }

    public void saveExamAttempt(ExamAttempt examAttempt) {

        EntityManager em = JpaUtil.getEntityManager();
        JpaUtil.executeTransaction(em, () -> {
            examRepository.saveExamAttemp(em, examAttempt);
            return null;
        });
    }

    public void saveExamStudent(ExamStudent examStudent) {

        EntityManager em = JpaUtil.getEntityManager();
        JpaUtil.executeTransaction(em, () -> {
            examRepository.saveExamStudent(em, examStudent);
            return null;
        });

    }

    public ExamStudent correctingPaper(long studentId, long examId) {


        ExamStudent examStudent = findExamStudentByStudentIdAndExamId(studentId, examId);

        List<Answer> response = findAnswersByStudentIdAndExamId(studentId, examId);
        response.forEach(System.out::println);
        ExamAttempt examAttempt = findAttemptByExamIdByStudentId(examId,studentId);
        System.out.println(examAttempt.getStatus());
        System.out.println(examAttempt.getCurrentQuestionIndex());
        List<Double> scors = new ArrayList<>();
        if (examAttempt.getStatus() == Status.FINISHED || examAttempt.getStatus() == Status.TIME_OUT) {

            for (Answer a : response) {

                Question q = a.getQuestion();
                System.out.println(q);

                double score = findScorByQuestionId(q.getId());
                if (q.getCorrectAnswer().equals(a.getResponse())) {
                    scors.add(score);

                } else {
                    scors.add(0.0);
                }

            }

        }
        double totalScore = scors.stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        examStudent.setTotalScore(totalScore);
        return examStudent;


    }

    public void updateExamStudent(ExamStudent examStudent) {

        EntityManager em = JpaUtil.getEntityManager();
        JpaUtil.executeTransaction(em, () -> {
            examRepository.updateExamStudent(em, examStudent);
            return null;
        });
    }

    public ExamStudent findExamStudentByStudentIdAndExamId(long studentId, long examId) {
        EntityManager em = JpaUtil.getEntityManager();
        return JpaUtil.executeTransaction(em, () -> {
            return examRepository.findExamStudentByStudentIdAndExamId(em, studentId, examId);

        });

    }


    public List<Answer> findAnswersByStudentIdAndExamId(long studentId, long examId) {

        EntityManager em = JpaUtil.getEntityManager();
        return JpaUtil.executeTransaction(em, () -> {

            List<Answer> answer = examRepository.findAnswersByStudentIdAndExamId(em, studentId, examId);
            if (answer.isEmpty()) {
                throw new RuntimeException("there isnot any answer for this student");
            }

            return answer;
        });

    }

    public double findScorByQuestionId(long questionId) {
        EntityManager em = JpaUtil.getEntityManager();
        return JpaUtil.executeTransaction(em, () -> {

            ExamQuestion examQuestion = examRepository.findExamQuestionByQuestionId(em, questionId);
            double score = examQuestion.getScore();

            return score;
        });

    }

    public Question findQuestionByQuestionId(long questionId) {
        EntityManager em = JpaUtil.getEntityManager();
        return JpaUtil.executeTransaction(em, () -> {
            Question q = examRepository.findQuestionByQuestionId(em, questionId);
            return q;
        });

    }

    public List<ExamStudent> findExamStudentsByExamId(long examid) {
        EntityManager em = JpaUtil.getEntityManager();
        return JpaUtil.executeTransaction(em, () -> {
            List<ExamStudent> examStudents = examRepository.findExamStudentsByExamId(em, examid);
            return examStudents;
        });
    }

    public void updateAnswer(Answer answer) {
        EntityManager em = JpaUtil.getEntityManager();

        JpaUtil.executeTransaction(em, () -> {
            examRepository.updateAnswer(em, answer);
            return null;
        });

    }

    public ExamAttempt findAttemptByExamIdByStudentId(long examid, long studentid) {
        EntityManager em = JpaUtil.getEntityManager();

        return JpaUtil.executeTransaction(em, () -> {

            return examRepository.findAttemptByExamIdByStudentId(em, examid, studentid);
        });
    }

    public void updateAttempt(ExamAttempt examAttempt) {
        EntityManager em = JpaUtil.getEntityManager();

        JpaUtil.executeTransaction(em, () -> {
            examRepository.updateAttempt(em, examAttempt);
            return null;
        });


    }


}
