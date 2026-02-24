package ir.maktab.service;

import ir.maktab.model.Role;
import ir.maktab.model.User;
import ir.maktab.model.dtos.UserDto;
import ir.maktab.repository.UserRepository;
import ir.maktab.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;
    private EntityManager entityManager;

    private MockedStatic<JpaUtil> mockedJpaUtil;

    @BeforeEach
    void setUp() {

        userRepository = mock(UserRepository.class);
        entityManager = mock(EntityManager.class);

        userService = new UserService(userRepository);
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
    void testRegisterUser() {

        User user = new User();
        user.setUserName("ali");

        userService.registerUser(user);

        verify(userRepository, times(1))
                .save(entityManager, user);
    }

    @Test
    void testLogin() {
        User user = new User();
        user.setUserName("ali");
        user.setPassword("1234");
        user.setApproved(true);
        when(userRepository.findByUsername(entityManager, "ali"))
                .thenReturn(user);
        User result = userService.login(
                new UserDto("ali", "1234")
        );
        assertNotNull(result);
        assertEquals("ali", result.getUserName());

        verify(userRepository, times(1))
                .findByUsername(entityManager, "ali");

    }

    @Test
    void shouldReturnPendingUsers_whenExist() {

        List<User> mockUsers = List.of(
                new User("ali", "1234", "Ali Reza", Role.Student, false),
                new User("reza", "5678", "Reza Ahmadi", Role.Professor, true),
                new User("sara", "9999", "Sara Nik", Role.Student, false)
        );

        List<User> expected = mockUsers.stream()
                .filter(u -> !u.isApproved())
                .toList();

        when(userRepository.getPendingUsers(entityManager))
                .thenReturn(expected);

        List<User> result = userService.getPendingUsers();

        assertNotNull(result);
        assertEquals(expected.size(), result.size());
        assertEquals(expected, result);

        verify(userRepository, times(1))
                .getPendingUsers(entityManager);
    }

    @Test
    void approveUserAndUpdateUser() {
        User user = new User("ali", "1234", "Ali Reza", Role.Student, false);

        when(userRepository.findByUsername(entityManager, "ali")).thenReturn(user);
        userService.approveUser("ali");
        assertTrue(user.isApproved());

        verify(userRepository, times(1))
                .findByUsername(entityManager, "ali");

        verify(userRepository, times(1))
                .update(entityManager, "ali");
    }

    @Test
    void shouldReturnUsersByRole() {


        List<User> mockUsers = List.of(
                new User("ali", "1234", "Ali Reza", Role.Student, false),
                new User("reza", "5678", "Reza Ahmadi", Role.Professor, true),
                new User("sara", "9999", "Sara Nik", Role.Student, false)
        );

        List<User> expectedStudents = mockUsers.stream()
                .filter(user -> user.getRole().equals(Role.Student))
                .toList();

        when(userRepository.findUsersByRole(entityManager, Role.Student))
                .thenReturn(expectedStudents);


        List<User> users = userService.findUsersByRole(Role.Student);


        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals(expectedStudents, users);


        assertTrue(users.stream()
                .allMatch(user -> user.getRole().equals(Role.Student)));

        verify(userRepository, times(1))
                .findUsersByRole(entityManager, Role.Student);
    }
    @Test
    void shouldReturnUserwhenFullNameExists() {

        User mockUser = new User("ali", "1234", "Ali Reza", Role.Student, false);

        when(userRepository.findUserByFullName(entityManager, "Ali Reza"))
                .thenReturn(Optional.of(mockUser));

        Optional<User> result =
                userService.findUserByFullName("Ali Reza");

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(mockUser, result.get());

        verify(userRepository, times(1))
                .findUserByFullName(entityManager, "Ali Reza");
    }
}