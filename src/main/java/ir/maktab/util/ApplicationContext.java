/*
package ir.maktab.util;

import ir.maktab.repository.*;
import ir.maktab.repository.impl.*;
import ir.maktab.service.*;

public class ApplicationContext {


    private static ApplicationContext instant;

    private ApplicationContext() {}

    public static ApplicationContext getInstant() {
        if (instant == null) {
            instant = new ApplicationContext();
        }
        return instant;
    }

    private CourseRepository courseRepository;
    private UserRepository userRepository;
    private TeacherRepository teacherRepository;
    private StudentRepository studentRepository;
    private ExamRepository examRepository;


    private CourseService courseService;
    private UserService userService;
    private TeacherService teacherService;
    private StudentService studentService;
    private ExamService examService;
    private Initializer initializer;

    public CourseRepository getCourseRepository() {
        if (courseRepository == null)
            courseRepository = new CourseRepositoryImpl();
        return courseRepository;
    }

    public UserRepository getUserRepository() {
        if (userRepository == null)
            userRepository = new UserRepositoryImpl();
        return userRepository;
    }

    public TeacherRepository getTeacherRepository() {
        if (teacherRepository == null)
            teacherRepository = new TeacherRepositoryImpl();
        return teacherRepository;
    }

    public StudentRepository getStudentRepository() {
        if (studentRepository == null)
            studentRepository = new StudentRepositoryImpl();
        return studentRepository;
    }

    public ExamRepository getExamRepository() {
        if (examRepository == null)
            examRepository = new ExamRepositoryImpl();
        return examRepository;
    }


    public CourseService getCourseService() {
        if (courseService == null)
            courseService = new CourseService(getCourseRepository(),getStudentService());
        return courseService;
    }

    public UserService getUserService() {
        if (userService == null)
            userService = new UserService(getUserRepository());
        return userService;
    }

    public TeacherService getTeacherService() {
        if (teacherService == null)
            teacherService = new TeacherService(getTeacherRepository());
        return teacherService;
    }

    public StudentService getStudentService() {
        if (studentService == null)
            studentService = new StudentService(getStudentRepository());
        return studentService;
    }

    public ExamService getExamService() {
        if (examService == null)
            examService = new ExamService(getExamRepository(),getStudentService());
        return examService;
    }

    public Initializer getInitializer() {
        if(initializer == null)
            initializer = new Initializer(getUserService());
        return initializer;
    }


}
*/
