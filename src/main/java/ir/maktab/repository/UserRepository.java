package ir.maktab.repository;

import ir.maktab.model.Role;
import ir.maktab.model.Student;
import ir.maktab.model.User;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
     void save(EntityManager entityManager, User user);
     User findByUsername(EntityManager entityManager,String username);
     List<User> getPendingUsers(EntityManager em);
     void update(EntityManager em, String username);
     List<User> findUsersByRole(EntityManager em, Role role);
     Optional<User> findUserByFullName(EntityManager em, String fullName);

}
