package ir.maktab.util;

import ir.maktab.model.Role;
import ir.maktab.model.User;
import ir.maktab.service.UserService;

import java.util.Optional;


public class Initializer {
    private  final UserService userService;

    public Initializer(UserService userService) {
        this.userService = userService;
    }

    public  void initialize(){
       Optional<User>  adminSystem = userService.findUserByFullName("Admin System");
        if(adminSystem.isPresent()){
            return ;
        }

        User admin = new User();
        admin.setFullName("Admin System");
        admin.setUserName("admin");
        admin.setPassword("admin123");
        admin.setRole(Role.Admin);
        admin.setApproved(true);

        userService.registerUser(admin);
    }
}
