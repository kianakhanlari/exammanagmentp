/*
package ir.maktab.service;

import ir.maktab.model.Role;
import ir.maktab.model.Teacher;
import ir.maktab.model.dtos.TeacherDto;
import ir.maktab.repository.TeacherRepository;
import ir.maktab.repository.UserRepository;
import ir.maktab.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TeacherServiceTest {
    private TeacherService teacherService;
    private TeacherRepository teacherRepository;
    private EntityManager entityManager;
    private MockedStatic<JpaUtil> mockedJpaUtil;

    @BeforeEach
    void setUp() {

        teacherRepository = mock(TeacherRepository.class);
        entityManager = mock(EntityManager.class);

        teacherService = new TeacherService(teacherRepository);
        mockedJpaUtil = mockStatic(JpaUtil.class);

        EntityManagerFactory factory = mock(EntityManagerFactory.class);

        JpaUtil.initForTest(factory);
        mockedJpaUtil.when(JpaUtil::getEntityManager)
                .thenReturn(entityManager);

        mockedJpaUtil.when(() ->
                        JpaUtil.executeTransaction(eq(entityManager), any(Supplier.class)))
                .thenAnswer(invocation -> {

                    Supplier<?> supplier = invocation.getArgument(1);

                    if (supplier == null) {
                        throw new IllegalStateException("Supplier is null");
                    }

                    return supplier.get();
                });
    }


    @AfterEach
    void tearDown() {
        mockedJpaUtil.close();
    }

    @Test
    void testRegisterTeacher() {

        TeacherDto dto = new TeacherDto();
        dto.fullName = "Ali Reza";
        dto.userName = "ali123";
        dto.passWord = "1234";
        dto.specialty = "Math";

        teacherService.registerTeacher(dto);

        ArgumentCaptor<Teacher> captor =
                ArgumentCaptor.forClass(Teacher.class);

        verify(teacherRepository)
                .save(eq(entityManager), captor.capture());

        Teacher saved = captor.getValue();

        assertEquals("Ali Reza", saved.getFullName());
        assertEquals("Math", saved.getSpecialty());
        assertEquals(Role.Professor, saved.getRole());
    }

    @Test
    void returnTeacherByUsername() {
        Teacher teacher = new Teacher();
        teacher.setFullName("Ali Reza");
        teacher.setUserName("ali123");
        teacher.setPassword("1234");
        teacher.setSpecialty("Math");

        when(teacherRepository.findTeacherByUserName(entityManager,"ali123"))
                .thenReturn(teacher);
       Teacher teacher1= teacherService.findTeacherByUserName("ali123");
        assertEquals(teacher,teacher1);
        verify(teacherRepository,times(1))
                .findTeacherByUserName(entityManager,"ali123");

    }
}*/
