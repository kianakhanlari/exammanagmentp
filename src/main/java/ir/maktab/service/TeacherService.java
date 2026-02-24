package ir.maktab.service;

import ir.maktab.Exception.TeacherNotFoundException;
import ir.maktab.model.Role;
import ir.maktab.model.Teacher;
import ir.maktab.model.dtos.TeacherDto;
import ir.maktab.repository.TeacherRepository;
import ir.maktab.util.JpaUtil;
import ir.maktab.util.ValidationUtil;
import jakarta.persistence.EntityManager;

public class TeacherService {
    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {

        this.teacherRepository = teacherRepository;
    }

    public void registerTeacher(TeacherDto teacherDto) {
        ValidationUtil.validate(teacherDto);

        EntityManager em = JpaUtil.getEntityManager();
        JpaUtil.executeTransaction(em, () -> {
            Teacher teacher = new Teacher();
            teacher.setFullName(teacherDto.fullName);
            teacher.setUserName(teacherDto.userName);
            teacher.setPassword(teacherDto.passWord);
            teacher.setSpecialty(teacherDto.specialty);
            teacher.setRole(Role.Professor);
            teacherRepository.save(em, teacher);
            return null;
        });
    }

    public Teacher findTeacherByUserName(String userName) {

        EntityManager em = JpaUtil.getEntityManager();

        return JpaUtil.executeTransaction(em, () -> {
            Teacher teacher = teacherRepository.findTeacherByUserName(em, userName);
            if (teacher == null) {
                throw new TeacherNotFoundException("Teacher with this username not found");
            }
            return teacher;
        });


    }

    public Long findTeacherIdByUserName(String userName) {

        Teacher teacher = findTeacherByUserName(userName);
        Long userId = teacher.getUserID();
        return userId;

    }
}