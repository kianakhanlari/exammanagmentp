package ir.maktab.service;

import ir.maktab.model.Course;
import ir.maktab.model.Student;
import ir.maktab.model.Teacher;
import ir.maktab.model.dtos.CourseDto;
import ir.maktab.repository.CourseRepository;
import ir.maktab.repository.TeacherRepository;
import ir.maktab.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CourseServiceTest {
    private CourseRepository courseRepository;
    private CourseService courseService;
    private EntityManager entityManager;
    private StudentService studentService;
    private MockedStatic<JpaUtil> mockedJpaUtil;

    @BeforeEach
    void setUp() {

        courseRepository = mock(CourseRepository.class);
        entityManager = mock(EntityManager.class);
        studentService = mock(StudentService.class);


        courseService = new CourseService(courseRepository, studentService);
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
    void testRegisterCourseWithCaptor() {

        CourseDto course = new CourseDto();
        course.title = "Java Course";
        course.startDate = LocalDate.of(1999, 10, 10);
        course.endDate = LocalDate.now().plusDays(10);

        courseService.registerCourse(course);

        ArgumentCaptor<Course> captor = ArgumentCaptor.forClass(Course.class);

        verify(courseRepository).save(eq(entityManager), captor.capture());

        Course capturedCourse = captor.getValue();

        assertEquals("Java Course", capturedCourse.getTitle());
        assertEquals(LocalDate.of(1999, 10, 10), capturedCourse.getStartDate());
    }

    @Test
    void returnAllCourse() {
        List<Course> courses = List.of(
                new Course("Java",
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2024, 6, 1)),

                new Course("Spring",
                        LocalDate.of(2024, 2, 1),
                        LocalDate.of(2024, 7, 1))
        );
        when(courseRepository.findAllCourse(entityManager))
                .thenReturn(courses);
        List<Course> courses1 = courseService.showAllCourse();
        assertEquals(courses, courses1);
        verify(courseRepository, times(1))
                .findAllCourse(entityManager);
    }

    @Test
    void removeCourseByTitle() {
        Course course = new Course();
        course.setTitle("Java");
        course.setStartDate(LocalDate.of(2024, 1, 1));
        course.setEndDate(LocalDate.of(2024, 6, 1));
        course.setId(123);
        when(courseRepository.findCourseByTitle(entityManager, "Java")).thenReturn(course);

        courseService.removeCourse("Java");


        verify(courseRepository, times(1))
                .removeCourse(any(EntityManager.class), eq(123));
    }


    @Test
    void updateCourse() {

        Course course = new Course();
        course.setTitle("Java");
        course.setStartDate(LocalDate.of(2025, 2, 11));
        course.setEndDate(LocalDate.of(2025, 2, 11));
        course.setId(123);

        when(courseRepository.findCourseByTitle(entityManager, "Java"))
                .thenReturn(course);

        CourseDto course1 = new CourseDto();
        course1.title = course.getTitle();
        course1.startDate = LocalDate.of(2026, 2, 11);
        course1.endDate = LocalDate.of(2026, 2, 28);
        course1.id = course.getId();

        courseService.updateCourse(course1);

        assertEquals(LocalDate.of(2026, 2, 11), course1.startDate);
        assertEquals(LocalDate.of(2026, 2, 28), course1.endDate);
        ArgumentCaptor<Course> captor =
                ArgumentCaptor.forClass(Course.class);

        verify(courseRepository)
                .updateCourse(eq(entityManager), captor.capture());

        Course updated = captor.getValue();

        assertEquals(LocalDate.of(2026, 2, 11), updated.getStartDate());
        assertEquals(LocalDate.of(2026, 2, 28), updated.getEndDate());
    }

    @Test
    void findCourseIdByTitle_shouldReturnId_whenCourseExists() {


        Course course = new Course();
        course.setId(123);
        course.setTitle("Java");

        when(courseRepository.findCourseByTitle(entityManager, "Java"))
                .thenReturn(course);

        int result = courseService.findCourseIdByTitle("Java");

        assertEquals(123, result);

        verify(courseRepository, times(1))
                .findCourseByTitle(entityManager, "Java");
    }

    @Test
    void findCourseIdByTitle_shouldThrowException_whenCourseNotFound() {

        when(courseRepository.findCourseByTitle(entityManager, "Java"))
                .thenReturn(null);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> courseService.findCourseIdByTitle("Java")
        );

        assertEquals("there is not course with this title", ex.getMessage());

        verify(courseRepository, times(1))
                .findCourseByTitle(entityManager, "Java");
    }

    @Test
    void returnCoursesByTeacherId() {

        Teacher teacher = new Teacher();
        teacher.setUserID(213);

        List<Course> courses = List.of(
                new Course(
                        "Java",
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2024, 6, 1),
                        teacher),

                new Course(
                        "Spring",
                        LocalDate.of(2024, 2, 1),
                        LocalDate.of(2024, 7, 1),
                        teacher),

                new Course(
                        "Hibernate",
                        LocalDate.of(2024, 3, 1),
                        LocalDate.of(2024, 8, 1),
                        teacher)
        );

        when(courseRepository.findCourseByTeacherId(entityManager, 213))
                .thenReturn(courses);

        List<Course> result =
                courseService.findCourseByTeacherId(213);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(courses, result);

        verify(courseRepository, times(1))
                .findCourseByTeacherId(entityManager, 213);
    }

    @Test
    void returnCourseByTitle() {

        Course course = new Course();
        course.setId(123);
        course.setTitle("Java");
        when(courseRepository.findCourseByTitle(entityManager, "Java"))
                .thenReturn(course);
        Course course1 = courseService.findCourseByTitle("Java");
        assertEquals(course, course1);
        verify(courseRepository, times(1))
                .findCourseByTitle(entityManager, "Java");

    }

    @Test
    void returnCoursesByStudentId() {

        Student student = new Student();
        student.setUserID(1222);

        Student otherStudent = new Student();
        otherStudent.setUserID(999);

        List<Student> students1 = List.of(student);

        Course course1 = new Course(
                "Java",
                LocalDate.of(2024,1,1),
                LocalDate.of(2024,6,1),
                students1
        );


        List<Student> students2 = List.of(student, otherStudent);

        Course course2 = new Course(
                "Spring",
                LocalDate.of(2024,2,1),
                LocalDate.of(2024,7,1),
                students2
        );

        List<Course> courses = List.of(course1, course2);
        when(courseRepository.findCoursesByStudentId(entityManager, 1222))
                .thenReturn(courses);

        List<Course> result =
                courseService.findCourseByStudentId(1222);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(courses, result);

        verify(courseRepository, times(1))
                .findCoursesByStudentId(entityManager, 1222);
    }


}