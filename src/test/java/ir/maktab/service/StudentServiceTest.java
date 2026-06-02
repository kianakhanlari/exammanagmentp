/*
package ir.maktab.service;

import ir.maktab.model.Role;
import ir.maktab.model.Student;
import ir.maktab.model.dtos.StudentDto;
import ir.maktab.repository.StudentRepository;
import ir.maktab.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private StudentService studentService;

    private MockedStatic<JpaUtil> mockedJpaUtil;

    @BeforeEach
    void setUp() {

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
    void testregisterStudent() {

        StudentDto dto = new StudentDto();
        dto.fullName = "Ali Reza";
        dto.userName = "ali123";
        dto.passWord = "1234";
        dto.studentNumber = "ST-1001";


        doNothing().when(studentRepository)
                .save(any(EntityManager.class), any(Student.class));
        studentService.registerStudent(dto);


        ArgumentCaptor<Student> captor =
                ArgumentCaptor.forClass(Student.class);

        verify(studentRepository)
                .save(any(EntityManager.class), captor.capture());

        Student saved = captor.getValue();


        assertEquals("Ali Reza", saved.getFullName());
        assertEquals("ali123", saved.getUserName());
        assertEquals("1234", saved.getPassword());
        assertEquals("ST-1001", saved.getStudentNumber());
        assertEquals(Role.Student, saved.getRole());
    }


    @Test
    void returnStudentByUserName() {
        Student student=new Student();
        student.setUserName("ali");
        student.setFullName("Ali fifi");
        student.setPassword("ali123");

        when(studentRepository.findStudentByUsername(entityManager,"ali"))
                .thenReturn(student);
       Student student1= studentService.findStudentByUserName("ali");
        assertNotNull(student1);
        assertEquals("ali", student1.getUserName());
        assertEquals("Ali fifi", student1.getFullName());

        assertEquals(student,student1);

       verify(studentRepository,times(1))
               .findStudentByUsername(entityManager,"ali");

    }

    @Test
    void returnStudentByUserId() {
        Student student=new Student();
        student.setUserName("ali");
        student.setFullName("Ali fifi");
        student.setPassword("ali123");
        student.setUserID(1233);

        when(studentRepository.findStudentByUserId(entityManager,1233))
                .thenReturn(student);
        Student student1=studentService.findStudentByUserId(1233);
        assertNotNull(student1);

        assertEquals("ali", student1.getUserName());
        assertEquals("Ali fifi", student1.getFullName());

        assertEquals(student,student1);
        verify(studentRepository,times(1))
                .findStudentByUserId(entityManager,1233);

    }
}*/
