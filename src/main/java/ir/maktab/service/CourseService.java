package ir.maktab.service;

import ir.maktab.model.Course;
import ir.maktab.model.Exam;
import ir.maktab.model.Student;
import ir.maktab.model.dtos.CourseDto;
import ir.maktab.repository.CourseRepository;
import ir.maktab.util.JpaUtil;
import ir.maktab.util.ValidationUtil;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentService studentService;

    public CourseService(CourseRepository courseRepository, StudentService studentService) {

        this.courseRepository = courseRepository;
        this.studentService = studentService;
    }

    public void registerCourse(CourseDto courseDto) {
        ValidationUtil.validate(courseDto);

        EntityManager em = JpaUtil.getEntityManager();
        JpaUtil.executeTransaction(em, () -> {
            Course course = new Course();
            course.setTitle(courseDto.title);
            course.setStartDate(courseDto.startDate);
            course.setEndDate(courseDto.endDate);
            course.setTeacher(courseDto.teacher);
            for (String userName : courseDto.students) {
                Student student = studentService.findStudentByUserName(userName);

                if (student == null) {
                    throw new RuntimeException("Student " + userName + " not found");
                }

                if (!student.isApproved()) {
                    throw new RuntimeException(student.getFullName() + " is not approved");
                }
                course.getStudents().add(student);
            }
            courseRepository.save(em, course);
            return null;
        });
    }

    public List<Course> showAllCourse() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return JpaUtil.executeTransaction(em, () -> {
                List<Course> courses = courseRepository.findAllCourse(em);
                if (courses.isEmpty()) {
                    throw new RuntimeException(" there is not any courses");
                }

                return courses;
            });

        } finally {
            em.close();
        }
    }

    public void removeCourse(String title) {
        int courseId = findCourseIdByTitle(title);
        EntityManager em = JpaUtil.getEntityManager();
        JpaUtil.executeTransaction(em, () -> {
            courseRepository.removeCourse(em, courseId);
            return null;
        });
    }

    public void updateCourse(CourseDto courseDto) {
        ValidationUtil.validate(courseDto);
        Course course = new Course();
        int courseId = findCourseIdByTitle(courseDto.title);
        course.setEndDate(courseDto.endDate);
        course.setStartDate(courseDto.startDate);
        course.setId(courseId);
        EntityManager em = JpaUtil.getEntityManager();
        JpaUtil.executeTransaction(em, () -> {
            courseRepository.updateCourse(em, course);
            return null;
        });

    }

    public int findCourseIdByTitle(String title) {
        Course course = findCourseByTitle(title);
        if (course == null) {
            throw new RuntimeException("there is not course with this title");
        }
        int courseId = course.getId();
        return courseId;
    }

    public List<Course> findCourseByTeacherId(long teacherId) {

        EntityManager em = JpaUtil.getEntityManager();

        return JpaUtil.executeTransaction(em, () ->
                courseRepository.findCourseByTeacherId(em, teacherId)
        );
    }

    public Course findCourseByTitle(String title) {
        EntityManager em = JpaUtil.getEntityManager();

        return JpaUtil.executeTransaction(em, () ->
                courseRepository.findCourseByTitle(em, title)
        );

    }

    public List<Course> findCourseByStudentId(long studentId) {
        EntityManager em = JpaUtil.getEntityManager();

        return JpaUtil.executeTransaction(em, () ->
                courseRepository.findCoursesByStudentId(em, studentId)
        );

    }

    public List<Exam> findExamsByStudentId(Long studentId) {
        List<Course> courses = findCourseByStudentId(studentId);

        if (courses.isEmpty()) {

            throw new RuntimeException("You are not enrolled in any courses.");

        }

        List<Exam> allExams = new ArrayList<>();

        for (Course c : courses) {

            List<Exam> exams = c.getExams();

            if (!exams.isEmpty()) {
                allExams.addAll(exams);
            }
        }

        if (allExams.isEmpty()) {
            throw new RuntimeException("No exams found for your courses.");
        }

        return allExams;
    }

}


