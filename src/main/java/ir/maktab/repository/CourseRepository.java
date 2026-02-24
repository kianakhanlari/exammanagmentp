package ir.maktab.repository;

import ir.maktab.model.Course;
import jakarta.persistence.EntityManager;

import java.util.List;

public interface CourseRepository {
  void save(EntityManager em, Course course);
  List<Course>findAllCourse(EntityManager em);
  void removeCourse(EntityManager em,int id);
  void updateCourse(EntityManager em,Course course);
  List<Course>findCourseByTeacherId(EntityManager em,long teacherId);
  Course findCourseByTitle(EntityManager em, String title);
 List<Course> findCoursesByStudentId(EntityManager em, long studentId);

}
