package ir.maktab.repository.impl;

import ir.maktab.model.Role;
import ir.maktab.model.Student;
import ir.maktab.model.User;
import ir.maktab.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public class UserRepositoryImpl implements UserRepository {


    @Override
    public void save(EntityManager entityManager, User user) {
        entityManager.persist(user);
    }

    @Override
    public User findByUsername(EntityManager entityManager, String username) {
        try {
            return entityManager.createQuery(
                            "SELECT u FROM User u WHERE u.userName = :username",
                            User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<User> getPendingUsers(EntityManager em) {
        return em.createQuery(
                "SELECT u FROM User u WHERE u.isApproved = false",
                User.class
        ).getResultList();
    }

    @Override
    public void update(EntityManager em, String username) {
        em.createQuery(
                        "UPDATE User u SET u.isApproved = true WHERE u.userName = :username"
                )
                .setParameter("username", username)
                .executeUpdate();
    }

    @Override
    public List<User> findUsersByRole(EntityManager em, Role role) {
        return em.createQuery(
                        "SELECT u FROM User u WHERE u.role = :role", User.class)
                .setParameter("role", role)
                .getResultList();
    }

    public Optional<User> findUserByFullName(EntityManager em,String fullName) {
        return em.createQuery(
                        "select u from User u where u.fullName = :fullName", User.class)
                .setParameter("fullName", fullName)
                .getResultStream()
                .findFirst();
    }


}