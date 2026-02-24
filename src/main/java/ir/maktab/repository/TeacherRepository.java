package ir.maktab.repository;

import ir.maktab.model.Teacher;
import jakarta.persistence.EntityManager;

public interface TeacherRepository {
    void save(EntityManager em, Teacher teacher);
    Teacher  findTeacherByUserName(EntityManager em, String userName);
}
