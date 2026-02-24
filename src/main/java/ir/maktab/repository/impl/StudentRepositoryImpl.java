package ir.maktab.repository.impl;

import ir.maktab.model.Student;
import ir.maktab.repository.StudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class StudentRepositoryImpl implements StudentRepository {

    @Override
    public void save(EntityManager em, Student student) {
        em.persist(student);
    }

    @Override
    public Student findStudentByUsername(EntityManager em, String userName) {
        try {
            return em.createQuery(
                            "SELECT s FROM Student s WHERE s.userName= :userName",
                            Student.class)

                    .setParameter("userName",userName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Student findStudentByUserId(EntityManager em, long userId) {

        try {
            return em.createQuery(
                            "select s from Student s where s.userID = :userID",
                            Student.class)
                    .setParameter("userID", userId)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }


}
