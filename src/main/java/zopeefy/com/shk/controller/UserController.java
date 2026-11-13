package zopeefy.com.shk.controller;


import org.springframework.web.bind.annotation.*;
import zopeefy.com.shk.ExceptionHandler.GlobalExceptionHandler;
import zopeefy.com.shk.dto.LoginRequest;
import zopeefy.com.shk.dto.LoginResponse;
import zopeefy.com.shk.entity.UserModule;
import zopeefy.com.shk.service.UserServiceRepository;

import java.util.List;

@RestController
@RequestMapping("/api/users")

public class UserController {

    private final UserServiceRepository userServiceRepository;

    public UserController(UserServiceRepository userServiceRepository) {
        this.userServiceRepository = userServiceRepository;
    }

    @PostMapping("/CreateUser")
    public UserModule CreateUserNew(@RequestBody UserModule module) {
        return userServiceRepository.createUser(module);
    }

    @GetMapping("/GetAllUsers")
    public List<UserModule> GetAll() {
        return userServiceRepository.getAllUsers();
    }

    @GetMapping("/GetByid")
    public UserModule GetByid(@RequestParam Long id) {
        return userServiceRepository.getUserById(id);
    }
    @PutMapping("/UpdateUser/{id}")
    public UserModule UpdateUser(
            @PathVariable Long id,
            @RequestBody UserModule module) {

        return userServiceRepository.UpdateUser(id, module);
    }

    @DeleteMapping("/DeleteMapping")
    public String DeleteUser(@RequestParam Long id) {
        userServiceRepository.deleteUser(id);
        return "Deleted successfully";
    }

    @PostMapping("/Login")
    public LoginResponse login(@RequestBody LoginRequest request) throws GlobalExceptionHandler {
        return userServiceRepository.Login(request);
    }
}
