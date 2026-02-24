package ir.maktab.service;

import ir.maktab.model.*;
import ir.maktab.model.dtos.AnswerDto;
import ir.maktab.repository.ExamRepository;
import ir.maktab.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceTest {
    @Mock
    private ExamRepository examRepository;
    @Mock
    private EntityManager entityManager;
    @Mock
    private StudentService studentService;
    @InjectMocks
    private ExamService examService;
    private MockedStatic<JpaUtil> mockedJpaUtil;

    @BeforeEach
    void setUp() {

        mockedJpaUtil = mockStatic(JpaUtil.class);
        EntityManagerFactory factory = mock(EntityManagerFactory.class);

        JpaUtil.initForTest(factory);

        mockedJpaUtil.when(JpaUtil::getEntityManager)
                .thenReturn(entityManager);

        mockedJpaUtil.when(() ->
                        JpaUtil.executeTransaction(eq(entityManager), any(Supplier.class)))
                .thenAnswer(invocation -> {

                    Supplier<?> supplier = invocation.getArgument(1);

                    if (supplier == null) {
                        throw new IllegalStateException("Supplier is null");
                    }

                    return supplier.get();
                });
    }

    @AfterEach
    void tearDown() {
        mockedJpaUtil.close();
    }


    @Test
    void returnAllExamesByCourseTitle() {
        Course course = new Course();
        course.setTitle("Java");
        List<Exam> exams = List.of(

                new Exam(
                        "Midterm",
                        LocalDate.of(2024, 5, 10),
                        90
                ),

                new Exam(
                        "Final",
                        LocalDate.of(2024, 6, 20),
                        120
                )
        );

        course.setExams(exams);
        when(examRepository.findAllExamesByCourseTitle(entityManager, "Java"))
                .thenReturn(exams);
        List<Exam> exams1 = examService.findAllExamesByCourseTitle("Java");
        assertEquals(exams, exams1);
        verify(examRepository, times(1))
                .findAllExamesByCourseTitle(entityManager, "Java");


    }

    @Test
    void saveExam() {
        Exam exam = new Exam(
                "Final",
                LocalDate.of(2024, 6, 20),
                120
        );
        doNothing().when(examRepository)
                .saveExam(eq(entityManager), any(Exam.class));
        ArgumentCaptor<Exam> captor =
                ArgumentCaptor.forClass(Exam.class);

        examService.saveExam(exam);

        verify(examRepository).saveExam(eq(entityManager), captor.capture());

        Exam captured = captor.getValue();

        assertEquals("Final", captured.getTitle());

    }


    @Test
    void removeExamByTitle() {
        Exam exam = new Exam();
        exam.setTitle("final");
        examService.removeExamByTitle("final");

        verify(examRepository, times(1)).removeExamByTitle(entityManager, "final");

    }

    @Test
    void updateExamByTitle() {
        Exam exam = new Exam(
                "Final",
                LocalDate.of(2024, 6, 20),
                120
        );
        examService.updateExamByTitle(exam);
        verify(examRepository, times(1))
                .updateExamByTitle(any(EntityManager.class), eq(exam));
    }

    @Test
    void returnQuestionByTitle() {
        Question question = new Question();
        question.setTitle("oop");
        when(examRepository.findQuestionByTitle(entityManager, "oop"))
                .thenReturn(question);
        Question question1 = examService.findQuestionByTitle("oop");
        assertEquals(question, question1);
        verify(examRepository, times(1))
                .findQuestionByTitle(entityManager, "oop");
    }

    @Test
    void updateQuestion() {
        Question question = new Question();
        question.setTitle("oop");
        question.setCorrectAnswer("1");
        examService.updateQuestion(question);
        ArgumentCaptor<Question> captor = ArgumentCaptor.forClass(Question.class);
        verify(examRepository).updateQuestion(eq(entityManager), captor.capture());
        Question updateQuestion = captor.getValue();

        assertEquals("oop", updateQuestion.getTitle());
        assertEquals("1", updateQuestion.getCorrectAnswer());
    }

  /*  @Test
    void returnQuestionsByExamTitle() {
        Exam exam=new Exam();
        exam.setTitle("final");
        List<Question> questions=List.of(
                new Question("oop","1"),
                new Question("git","2")
        );
        exam.setExamQuestions();


    }*/

    @Test
    void returnExamByTitleExam() {
        Exam exam = new Exam();
        exam.setTitle("Java");

        when(examRepository.findExamByTitleExam(entityManager, "Java"))
                .thenReturn(exam);

        Exam result = examService.findExamByTitleExam("Java");

        assertNotNull(result);
        assertEquals("Java", result.getTitle());

        verify(examRepository, times(1))
                .findExamByTitleExam(entityManager, "Java");
    }

    @Test
    void returnQuestionsByExamId() {

        List<Question> questions = new ArrayList<>();
        Question question1 = new Question();
        Question question2 = new Question();
        questions.add(question1);
        questions.add(question2);

        when(examRepository.findQuestionsByExamId(entityManager, 123))
                .thenReturn(questions);

        List<Question> result = examService.findQuestionsByExamId(123);

        verify(examRepository, times(1))
                .findQuestionsByExamId(entityManager, 123);

        assertEquals(2, result.size());
        assertEquals(questions, result);
    }



    @Test
    void saveAnswer() {

        Exam exam = new Exam();
        exam.setId(122L);

        AnswerDto answerDto = new AnswerDto();
        answerDto.response = "123";
        answerDto.exam = exam;

        when(examRepository.findExamByExamId(any(), eq(122L)))
                .thenReturn(exam);

        examService.saveAnswer(answerDto);

        ArgumentCaptor<Answer> captor =
                ArgumentCaptor.forClass(Answer.class);

        verify(examRepository).saveAnswer(any(), captor.capture());

        Answer savedAnswer = captor.getValue();

        assertEquals("123", savedAnswer.getResponse());
        assertEquals(122L, savedAnswer.getExam().getId());
    }
    @Test
    void returnExamByExamId() {
        Exam exam=new Exam();
        exam.setId(234);
        when(examRepository.findExamByExamId(entityManager,234))
                .thenReturn(exam);
       Exam exam1= examService.findExamByExamId(234);
       assertEquals(exam,exam1);
       verify(examRepository,times(1))
               .findExamByExamId(entityManager,234);

    }

    @Test
    void saveExamAttempt() {

        ExamAttempt examAttempt = new ExamAttempt();

        Student student = new Student();
        student.setUserID(123);

        Exam exam = new Exam();
        exam.setId(1023);


        examService.saveExamAttempt(examAttempt);

        ArgumentCaptor<ExamAttempt> captor =
                ArgumentCaptor.forClass(ExamAttempt.class);

        verify(examRepository,times(1))
                .saveExamAttemp(any(EntityManager.class), captor.capture());
        ExamAttempt saved = captor.getValue();

        assertNotNull(saved);



    }

}