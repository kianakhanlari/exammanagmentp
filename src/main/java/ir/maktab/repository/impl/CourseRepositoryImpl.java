package ir.maktab.repository.impl;

import ir.maktab.model.Course;
import ir.maktab.repository.CourseRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;

import java.util.List;

public class CourseRepositoryImpl implements CourseRepository {

    @Override
    public void save(EntityManager em,Course course) {
        em.persist(course);
    }

    @Override
    public List<Course> findAllCourse(EntityManager em) {
        return em.createQuery("""
        SELECT  c
        FROM Course c
        LEFT JOIN FETCH c.students
    """, Course.class).getResultList();
    }

    @Override
    public void removeCourse(EntityManager em, int id) {
        em.createQuery(
        "DELETE FROM Course c WHERE c.id = :id")
       .setParameter("id", id)
        .executeUpdate();

    }
    @Override
    public void updateCourse(EntityManager em, Course course) {
        em.createQuery(
                        "UPDATE Course c " +
                                "SET c.startDate = :startDate, c.endDate = :endDate " +
                                "WHERE c.id = :id "
                )
                .setParameter("startDate", course.getStartDate())
                .setParameter("endDate", course.getEndDate())
                .setParameter("id", course.getId())
                .executeUpdate();
    }
    public List<Course> findCourseByTeacherId(EntityManager em, long teacherId) {

        return em.createQuery(
                        "SELECT c FROM Course c WHERE c.teacher.userID = :teacherId",
                        Course.class
                )
                .setParameter("teacherId", teacherId)
                .getResultList();
    }

    @Override
    public Course findCourseByTitle(EntityManager em, String title) {
        return em.createQuery("select c from Course c where c.title=:title" ,
                Course.class
                )
                .setParameter("title",title)
                .getSingleResult();
    }

    @Override
    public List<Course> findCoursesByStudentId(EntityManager em, long studentId) {

        EntityGraph<?> graph = em.getEntityGraph("course.withExams");

        return em.createQuery(
                        "select distinct c from Course c join c.students s where s.id = :id",
                        Course.class
                )
                .setParameter("id", studentId)
                .setHint("jakarta.persistence.fetchgraph", graph)
                .getResultList();
    }



}
