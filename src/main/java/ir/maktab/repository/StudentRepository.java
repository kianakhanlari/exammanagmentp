package ir.maktab.repository;

import ir.maktab.model.Student;
import jakarta.persistence.EntityManager;

public interface StudentRepository {
    void save(EntityManager em, Student student);

    Student findStudentByUsername(EntityManager em, String fullName);

    Student findStudentByUserId(EntityManager em, long userid);

}
