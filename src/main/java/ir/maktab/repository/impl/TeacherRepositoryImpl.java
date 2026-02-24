package ir.maktab.repository.impl;

import ir.maktab.model.Teacher;
import ir.maktab.repository.TeacherRepository;
import jakarta.persistence.EntityManager;

public class TeacherRepositoryImpl implements TeacherRepository {

    @Override
    public void save(EntityManager em, Teacher teacher) {
        em.persist(teacher);
    }


    @Override
    public Teacher findTeacherByUserName(EntityManager em, String userName) {

        return  em.createQuery(
                        "SELECT t FROM Teacher t WHERE t.userName = :userName",
                        Teacher.class)
                .setParameter("userName",userName)
                .getSingleResult();

    }

}
