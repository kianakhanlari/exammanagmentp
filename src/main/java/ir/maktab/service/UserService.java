package ir.maktab.service;

import ir.maktab.model.Role;
import ir.maktab.model.User;
import ir.maktab.model.dtos.UserDto;
import ir.maktab.repository.UserRepository;
import ir.maktab.util.JpaUtil;
import ir.maktab.util.ValidationUtil;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }


    public void registerUser(User user) {
        EntityManager em = JpaUtil.getEntityManager();
        JpaUtil.executeTransaction(em, () -> {
            userRepository.save(em, user);
            return null;
        });
    }


    public User login(UserDto userDto) {
        ValidationUtil.validate(userDto);
        EntityManager em = JpaUtil.getEntityManager();

        return JpaUtil.executeTransaction(em, () -> {
            User user = userRepository.findByUsername(em, userDto.username());

            if (user == null) {
                throw new IllegalArgumentException("User not found");
            }

            if (!user.isApproved()) {
                throw new IllegalArgumentException("user is not approved by admin");
            }

            if (!user.getPassword().equals(userDto.password())) {
                throw new IllegalArgumentException("Invalid password");
            }

            return user;
        });


    }

    public List<User> getPendingUsers() {
        EntityManager em = JpaUtil.getEntityManager();
        try {

            return JpaUtil.executeTransaction(em, () -> {
                List<User> users = userRepository.getPendingUsers(em);
                if (users.isEmpty()) {

                    throw new RuntimeException("No pending users found!");
                }
                return users;
            });
        } catch (RuntimeException e) {
            System.out.println("Error fetching pending users: " + e.getMessage());
            return null;
        } finally {

            em.close();
        }
    }

    public void approveUser(String username) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            JpaUtil.executeTransaction(em, () -> {
                User user = userRepository.findByUsername(em, username);

                if (user == null) {
                    throw new IllegalArgumentException("User not found");
                }

                user.setApproved(true);
                userRepository.update(em, username);
                return null;
            });
        } finally {
            em.close();
        }
    }

    public List<User> findUsersByRole(Role role) {

        EntityManager em = JpaUtil.getEntityManager();
        try {
            return JpaUtil.executeTransaction(em, () -> {
                List<User> users = userRepository.findUsersByRole(em, role);
                if (users.isEmpty()) {
                    throw new RuntimeException("users with this role not found");
                }
                return users;
            });
        } finally {
            em.close();
        }

    }

    public Optional<User>  findUserByFullName(String fullName) {
        EntityManager em = JpaUtil.getEntityManager();
        return JpaUtil.executeTransaction(em, () -> {
            Optional<User> user = userRepository.findUserByFullName(em, fullName);
            if (user.isEmpty()) {
                System.out.println("User not found!");

                return Optional.empty();
            }

            return user;
        });

    }


}