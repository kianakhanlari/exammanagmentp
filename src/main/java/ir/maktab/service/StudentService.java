package ir.maktab.service;

import ir.maktab.model.Role;
import ir.maktab.model.Student;
import ir.maktab.model.dtos.StudentDto;
import ir.maktab.repository.StudentRepository;
import ir.maktab.util.JpaUtil;
import ir.maktab.util.ValidationUtil;
import jakarta.persistence.EntityManager;

public class StudentService {
private  final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {

        this.studentRepository = studentRepository;
    }
    public void registerStudent(StudentDto studentDto){
        ValidationUtil.validate(studentDto);

        EntityManager em = JpaUtil.getEntityManager();
        JpaUtil.executeTransaction(em, () -> {
            Student student=new Student();
            student.setRole(Role.Student);
            student.setStudentNumber(studentDto.studentNumber);
            student.setPassword(studentDto.passWord);
            student.setUserName(studentDto.userName);
            student.setFullName(studentDto.fullName);
           studentRepository.save(em,student);
            return null;
        });
    }
    public Student findStudentByUserName(String fullName) {
        EntityManager em = JpaUtil.getEntityManager();

            return JpaUtil.executeTransaction(em, () ->
                    studentRepository. findStudentByUsername(em,  fullName)
            );

    }
    public  Student findStudentByUserId(long userid){
        EntityManager em = JpaUtil.getEntityManager();

        return JpaUtil.executeTransaction(em, () ->
                studentRepository.findStudentByUserId(em,userid)
        );
    }

}
